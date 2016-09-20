(ns rta-client.strings)

(defn hashmap->keystring
  "Converts a hashmap into a string with keys seperated by commas."
  [hmap]
  (let [keyword-list (map #(name (first %)) hmap)]
    (clojure.string/join "," keyword-list)))

(defn generate-insert-query
  "Generates a string that creates an insert query based upon the keyword fields that are passed in"
  [table where keywords kmap]
  (loop [keywds keywords
         generated-query (str "INSERT INTO " table " (" (hashmap->keystring kmap) ",Commit) VALUES (")]
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

;(defn diff-keywords
  ;"Gets a list of keywords that are different in two maps"
  ;[m1 m2]
  ;(let [difference (diff m1 m2)]
    ;(if (nil? (second difference))
      ;{}
      ;(keys (second difference)))))

(defn insert-update
  "Runs an insert query on the table with keys and values specified by a map"
  [table kmap]
  (loop []))

;(defn diff-update
  ;"Runs a diff on the database and updates any values that are different"
  ;([new-data where]\
   ;(diff-update (query! (generate-select-query where new-data)) new-data where))
  ;([old-data new-data where]
   ;(let [different-keys (diff-keywords old-data new-data)]
     ;(query! (generate-update-query where different-keys new-data)))))
