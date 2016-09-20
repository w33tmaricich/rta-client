(ns rta-client.generate)

(defn key-csv
  "Converts a set to a csv string.

   eg:
    #{:Name :Age :Occupation} => 'Name,Age,Occupation'"
  [s]
  (clojure.string/join "," (map name s)))

(defn key-val-csv
  "Converts a hashmap into a string with either keys or values seperated by
   commas.

  opts:
    :key
    :val

   eg:
    {:key 'value' :key2 'value2'} => 'key,key2' OR 'value,value2'"
  [mode hmap]
  (let [selector (if (= :val mode) (comp #(str "'" % "'") second) (comp name first))
        keyword-list (map #(selector %) hmap)]
    (clojure.string/join "," keyword-list)))

(defn key-val-equ
  "Converts a hashmap into a string with keys and values seperated by an =.

   eg:
    {:key 'value'} => 'key=value'"
  [hmap delimiter]
  (let [keywordless-list (into {} (map #(hash-map (name (first %))
                                                  (second %)) hmap))
        string-list (map #(str (first %) "='" (second %) "'") keywordless-list)]
    (clojure.string/join delimiter string-list)))

(defn insert-query
  "Generates a query string based upon the keyword field."
  [table hmap]
  (str "INSERT INTO " table " ("
       (key-val-csv :key hmap) 
       ") VALUES (" 
       (key-val-csv :val hmap) 
       ");"))

(defn update-query
  "Generates a query string based upon update values and a where clause."
  ([table values]
   (update-query table values nil))
  ([table values where]
   (str "UPDATE " table " SET "
        (key-val-equ values ",")
        (if where
          (str " WHERE " (key-val-equ where " AND "))
          "")
        ";")))

(defn delete-query
  "Generates a string that creates a delete query based upon the table name and keyword"
  [table hmap]
  (str "DELETE FROM "
       table 
       " WHERE "
       (key-val-equ hmap " AND ")
       ";"))

(defn select-query
  "Generates a select query string based upon a given input."
  ([table]
   (select-query table nil))
  ([table where]
   (select-query table #{:*} where))
  ([table select where]
   (str "SELECT "
        (if (= #{:*} select)
          "*"
          (key-csv select))
        " FROM "
        table
        (if where
          (str " WHERE "
               (key-val-equ where " AND "))
          "")
        ";")))
