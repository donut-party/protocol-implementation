(ns donut.protocol-implementation.hooks
  (:require
   [clj-kondo.hooks-api :as api]))

(defn def-protocol-implementation
  [{:keys [node]}]
  (let [[protocol-name _implementation-name & methods] (rest (:children node))]
    {:node (api/list-node
            (list* (api/token-node 'clojure.core/extend-protocol)
                   protocol-name
                   (api/token-node 'Object)
                   methods))}))
