(ns clj-rta.core
  (:import [com.skyline.CameraControlServer.Utility.Communications RTAClient])
  (:require [clojure.data :refer :all]))
;(import com.skyline.CameraControlServer.Utility.Communications.RTAClient)

;;; Globals
(def host "localhost")
(def port 8888)

;;; Functions
(defn arraylist->map
  "Converts an arraylist java object to a list"
  [al]
  (loop [finished-list {}
         counter 0]
    (if (>= counter (.size al))
      finished-list
      (recur (into finished-list (.get al counter))
             (inc counter)))))

(defn stringmap->keymap
  "Converts a map with strings as keys into a map with keywords as keys"
  [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))

(defn keymap->string
  "Converts a map with keywords into a comma seperated string"
  [kmap]
  (loop [keymap kmap
         out-str (str "")]
    (if (empty? keymap)
      out-str
      (if (= (count keymap) 1)
       (recur (rest keymap) (str out-str (name (key (first keymap)))))
       (recur (rest keymap) (str out-str (name (key (first keymap))) ","))))))

(defn connect
  "Creates a connection to the RTA client"
  [host port]
  (try
    (let [client (RTAClient. host port)]
      (.connect client)
      client)
    (catch Exception e (println "ERROR!:" (str "RTA connection failure: " (.getMessage e))))))

(defn string-query!
  "Queries the database and returns a string"
  [s]
  (try
    (let [client (connect host port)
          response (.issueQueryForResponse client s)]
      (.disconnect client)
      response)
    (catch Exception e (println "ERROR!:" (str "Query failure: " (.getMessage e))))))


(defn query!
  "Queries the database based upon the given string"
  [s]
  (try
    (let [client (connect host port)
          response (.issueQueryForResponse client s)]
      (.disconnect client)
      (stringmap->keymap (arraylist->map response)))
    (catch Exception e (println "ERROR!:" (str "Query failure: " (.getMessage e))))))

(defn table-headers
  "Gets the headers of the table"
  [t]
  (into #{}
    (keys (query! (str "select * from " t)))))

(defn generate-insert-query
  "Generates a string that creates an insert query based upon the keyword fields that are passed in"
  [table where keywords kmap]
  (loop [keywds keywords
         generated-query (str "INSERT INTO " table " (" (keymap->string kmap) ",Commit) VALUES (")]
    (if (empty? keywds)
      (str generated-query ",1);")
      (if (= (count keywds) (count keywords))
        (recur (rest keywds) (str generated-query ((first keywds) kmap)))
        (recur (rest keywds) (str generated-query "," ((first keywds) kmap)))))))

(defn generate-update-query
  "Generates a string that creates an update query based upon the keyword fields that are passed in"
  [table where keywords update-map]
  (loop [keywds keywords
         generated-query (str "UPDATE " table " SET ")]
    (if (empty? keywds)
      (str generated-query ", Commit='1' WHERE " (name where) "='" (where update-map) "';")
      (if (= (count keywds) (count keywords))
          (recur (rest keywds) (str generated-query (name (first keywds)) "='" ((first keywds) update-map) "'"))
          (recur (rest keywds) (str generated-query ", " (name (first keywds)) "='" ((first keywds) update-map) "'"))))))

(defn generate-delete-query
  "Generates a string that creates a delete query based upon the table name and keyword"
  [table pkey kmap]
  (str "DELETE FROM " table " WHERE " (name pkey) "='" (pkey kmap) "';"))

(defn generate-select-query
  "Generates a string that selects a specific row. takes a table keyword and a update map"
  [table k kmap]
  (str "select * from " table " where " (name k) "='" (k kmap) "';"))

(defn diff-keywords
  "Gets a list of keywords that are different in two maps"
  [m1 m2]
  (let [difference (diff m1 m2)]
    (if (nil? (second difference))
      {}
      (keys (second difference)))))

(defn insert-update
  "Runs an insert query on the table with keys and values specified by a map"
  [table kmap]
  (loop []))

(defn diff-update
  "Runs a diff on the database and updates any values that are different"
  ([new-data where]\
   (diff-update (query! (generate-select-query where new-data)) new-data where))
  ([old-data new-data where]
   (let [different-keys (diff-keywords old-data new-data)]
     (query! (generate-update-query where different-keys new-data)))))
