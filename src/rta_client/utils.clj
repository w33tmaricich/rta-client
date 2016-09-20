(ns rta-client.utils)

;;; Utility Functions
(defn arraylist->vector
  "Converts an arraylist to a vector."
  [al]
  (into [] al))

(defn jhashmap->hashmap
  "Converts a java hashmap into a clojure hashmap."
  [jhm]
  (into {} 
        (for [[k v] jhm]
          [(keyword k) v])))

(defn jreq->creq
  "Converts a java response into a clojure response."
  [data]
  (let [vector-data (arraylist->vector data)]
    (map jhashmap->hashmap vector-data)))
