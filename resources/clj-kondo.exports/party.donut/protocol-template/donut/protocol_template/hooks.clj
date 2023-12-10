(ns donut.protocol-template.hooks
  (:require
   [clj-kondo.hooks-api :as api]))

(defn def-protocol-template
  [{:keys [node]}]
  (let [[protocol-name _template-name & methods] (rest (:children node))]
    {:node (api/list-node
            (list* (api/token-node 'clojure.core/extend-protocol)
                   protocol-name
                   (api/token-node 'Object)
                   methods))}))
