(ns clj-rta.core-test
  (:require [clojure.test :refer :all]
            [clj-rta.core :refer :all]))

(deftest a-test
  (testing "Connection to psql."
    (query "Select * From DataTable;")
    (is (nil? (query "Select * From DataTable;")))))
