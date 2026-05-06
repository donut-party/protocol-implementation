(ns donut.ns-protocol
  "This namespace exists to show that extend-type-with-protocol-implementation
  succeeds when called from a different namespace"
  (:require
   [clojure.set :as set]
   [donut.protocol-implementation :as dbi]))

(defprotocol NSProtocol
  (foo [_]))

(dbi/def-protocol-implementation
  NSProtocol
  :test
  (foo [_] (set/union #{:a} #{:b} #{::set/foo})))
