(ns clojurebreaker.game-test
  (:use [clojure.test.generative :only (defspec) :as test])
  (:require [clojure.data.generators :as gen]
            [clojurebreaker.game :as game]
            [clojure.math.combinatorics :as comb]))

(defn random-secret
  []
  (gen/vec #(gen/one-of :r :g :b :y) 4))

(defn matches
  "Given a score, returns total number of exact picks
   unordered matches."
  [score]
  (+ (:exact score) (:unordered score)))

(defn scoring-is-symmetric
  [secret guess score]
  (= score (game/score guess secret)))

(defn scoring-is-bounded-by-number-of-pages
  [secret guess score]
  (< 0 (matches score) (count secret)))

(defn reordering-the-guess-does-not-chage-matches
  [secret guess score]
  (=  #{(matches score)}
      ;; into #{} erase duplicated key!
      (into #{} (map
                #(matches (game/score secret %))
                (comb/permutations guess)))))

