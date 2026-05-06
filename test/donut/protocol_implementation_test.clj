(ns donut.protocol-implementation-test
  (:require
   [clojure.test :refer [deftest is]]
   [donut.protocol-implementation :as dbpt]
   [donut.ns-protocol :as nsp]))


(defprotocol ExampleProtocol
  (foo [_]))

(dbpt/def-protocol-implementation
  ExampleProtocol
  :test
  (foo [_] "hi"))

(deftest implements-implementation
  (dbpt/extend-type-with-protocol-implementation
   java.util.Date
   ExampleProtocol
   :test)
  (is (= "hi" (foo (java.util.Date.)))))

(deftest implements-implementation-with-overrides
  (dbpt/extend-type-with-protocol-implementation
   java.util.Date
   ExampleProtocol
   :test
   {"hi" "hello"})
  (is (= "hello" (foo (java.util.Date.)))))


(deftest implements-implementation-with-ns-alias
  (dbpt/extend-type-with-protocol-implementation
   java.util.Date
   nsp/NSProtocol
   :test)
  (is (= #{:b :a :clojure.set/foo} (nsp/foo (java.util.Date.)))))
