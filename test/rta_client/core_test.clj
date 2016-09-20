(ns rta-client.core-test
  (:require [clojure.test :refer :all]
            [rta-client.globals :refer :all]
            [rta-client.generate :as gen]
            [rta-client.utils :as utils]))

(deftest globals-set
  (testing "Making sure globals are set properly."
    (is (= HOST "localhost"))
    (is (= PORT 8888))))

(deftest utility-functions
  (testing "Converting an arraylist to a map"
    (is (= [1 2 3]
           (utils/arraylist->vector (java.util.ArrayList. [1 2 3])))))
  (testing "Converting a java hashmap into a hashmap."
    (is (= {:one 1 :two 2}
           (utils/jhashmap->hashmap (java.util.HashMap. {:one 1 :two 2})))))
  (testing "Converting a java response into a clojure response."
    (is (= [{:one 1 :two 2}]
           (java.util.ArrayList. [(java.util.HashMap. {:one 1 :two 2})])))))

(deftest query-string-generation-functions
  (testing "Converting a keymap to a csv string."
    (is (= "one,two"
           (gen/key-val-csv :key {:one 1 :two 2})))
    (is (= "'1','2'"
           (gen/key-val-csv :val {:one 1 :two 2}))))
  (testing "Converting a keymap to an key=value string."
    (is (= "one='1',two='2'"
           (gen/key-val-equ {:one 1 :two 2} ","))))
  (testing "Generation of an insert query."
    (is (= "INSERT INTO Table (one,two,Commit) VALUES ('1','2','1');"
           (gen/insert-query "Table" {:one 1 :two 2 :Commit 1}))))
  (testing "Generation of an update query."
    (is (= "UPDATE Table SET one='1',two='2';"
           (gen/update-query "Table" {:one 1 :two 2})))
    (is (= "UPDATE Table SET one='1',two='2' WHERE three='3';"
           (gen/update-query "Table" {:one 1 :two 2} {:three 3})))
    (is (= "UPDATE Table SET one='1',two='2' WHERE three='3' AND four='4';"
           (gen/update-query "Table" {:one 1 :two 2} {:three 3 :four 4}))))
  (testing "Generation of a delete query."
    (is (= "DELETE FROM Table WHERE one='1';"
           (gen/delete-query "Table" {:one 1})))
    (is (= "DELETE FROM Table WHERE one='1' AND two='2';"
           (gen/delete-query "Table" {:one 1 :two 2}))))
  (testing "Generation of a select query."
    (is (= "SELECT * FROM Table;"
           (gen/select-query "Table")))
    (is (= "SELECT * FROM Table WHERE one='1';"
           (gen/select-query "Table" {:one 1})))
    (is (= "SELECT * FROM Table WHERE one='1';"
           (gen/select-query "Table" #{:*} {:one 1})))
    (is (= "SELECT Name FROM Table WHERE one='1';"
           (gen/select-query "Table" #{:Name} {:one 1})))
    (is (= "SELECT Name,Age FROM Table WHERE one='1';"
           (gen/select-query "Table" #{:Name :Age} {:one 1})))
    (is (= "SELECT Name,Age FROM Table WHERE one='1' AND two='2';"
           (gen/select-query "Table" #{:Name :Age} {:one 1 :two 2})))))

;(deftest query-test
  ;(testing "Connection to psql."
    ;(is (nil? (query! "Select * From SfsStreams;")))))
