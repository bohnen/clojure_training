(ns clojurebreaker.game
  (:require [clojure.math.combinatorics :as comb]
            [clojurebreaker.core :as core]))

;; (comb/selections [:r :g :b] 2)

(defn generate-turn-inputs
  "Generate all possible turn inputs for a clojurebreaker game
   with colors and n columns."
  [colors n]
  (-> (comb/selections colors n)
      (comb/selections 2)))

(defn score-inputs
  "Given a sequence of turn inputs, return a lazy sequence of
   maps with :secret, :guess, and :score."
  [inputs]
  (map
   (fn [[secret guess]]
     {:secret (seq secret)
      :guess (seq guess)
      :score (core/score secret guess)})
   inputs))
