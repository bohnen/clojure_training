(ns clojurebreaker.core
  (:require [clojure.data :as data]))

(defn exact-matches
  "Given two collections, return the number of positions where
   the collections contain equal items."
  [c1, c2]
  (let [[_ _ matches] (data/diff c1 c2)]
    (count (remove nil? matches))))

(defn unordered-matches
  "Given two collections, return a map where each key is an item
   in both collections, and each value is the number of times the
   value occurs in the collection with fewest occurances."
  [c1 c2]
  (let [f1 (select-keys (frequencies c1) c2)
        f2 (select-keys (frequencies c2) c1)]
    (merge-with min f1 f2)))




