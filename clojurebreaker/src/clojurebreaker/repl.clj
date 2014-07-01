(ns clojurebreaker.repl
  (:require [clojurebreaker.game :as game]
            [clojure.java.io :as io])
  (:use clojure.pprint))


(def example-secret [:r :g :g :b])
(def example-guess  [:y :y :y :g])

(defn write-test-table
  "Write out to a file with test data (p. 227)"
  []
  (with-open [w (io/writer "scoring-table")]
    (binding [*out* w]
      (print-table (->> (game/generate-turn-inputs [:r :g :b :y] 4)
                        (game/score-inputs))))))

