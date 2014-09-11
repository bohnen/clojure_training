;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(require '(clojure-stg [enemy :as en]))
(use '[clojure.tools.namespace.repl :only (refresh)])

(def e1 (en/->Enemy 0 0 50 30 5 3))

(en/enemy-move e1)


