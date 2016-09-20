(ns rta-client.core-test
  (:require [clojure.test :refer :all]
            [rta-client.globals :refer :all]
            [rta-client.strings :as s]
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
  (testing "Converting a keymap to a string."
    (is (= "one,two"
           (s/hashmap->keystring {:one 1 :two 2})))))

;(deftest query-test
  ;(testing "Connection to psql."
    ;(is (nil? (query! "Select * From SfsStreams;")))))
