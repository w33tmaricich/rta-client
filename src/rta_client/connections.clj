(ns rta-client.connections
  (:import [com.skyline.CameraControlServer.Utility.Communications RTAClient])
  (:require [rta-client.globals :refer :all]
            [rta-client.utils :as utils]))

; Connection Functions
(defn connect!
  "Creates a connection to the RTA client"
  [host port]
  (try
    (let [client (RTAClient. host port)]
      (.connect client)
      client)
    (catch Exception e (println "ERROR!:" (str "RTA connection failure: " (.getMessage e))))))

(defn quick-query!
  "Queries the database based upon the given string"
  [s]
  (try
    (let [client (connect! HOST PORT)
          response (.issueQueryForResponse client s)]
      (.disconnect client)
      (utils/jreq->creq response))
    (catch Exception e (println "ERROR!:" (str "Query failure: " (.getMessage e))))))

(defn table-headers!
  "Gets the headers of the table"
  [t]
  (let [result (quick-query! (str "select * from " t))]
    (if (empty? result)
      #{}
      (into #{}
            (-> result first keys)))))
