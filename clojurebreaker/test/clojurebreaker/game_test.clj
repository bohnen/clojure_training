(ns clojurebreaker.game-test
  (:use [clojure.test.generative :only (defspec) :as test])
  (:require [clojure.data.generators :as gen]
            [clojurebreaker.game :as game]
            [clojure.math.combinatorics :as comb]
            [clojure.test.generative.runner :as runner]))

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
  (<= 0 (matches score) (count secret)))

(defn reordering-the-guess-does-not-chage-matches
  [secret guess score]
  (=  #{(matches score)}
      ;; into #{} erase duplicated key!
      (into #{} (map
                #(matches (game/score secret %))
                (comb/permutations guess)))))

(defspec score-invariants
  game/score
  [^{:tag `random-secret} secret
   ^{:tag `random-secret} guess]
  (assert (scoring-is-symmetric secret guess %))
  (assert (scoring-is-bounded-by-number-of-pages secret guess %))
  (assert (reordering-the-guess-does-not-chage-matches secret guess %)))

;; (runner/run 2 1000 #'score-invariants)
;; (ex-data *e)

;; #42 calc fact
(fn [n] (reduce * (range 1 (inc n))))

;; #(reduce * (range % 0 -1)) ;; rangeはstartを含んでendを含まないので、順番が気にならないようなケースではこれも有り


;; #30 compress sequence
;; reduce #(if (= (last %) %2) % (conj % %2)) []

;; #(map first (partition-by identity %)) ;; partition-by はまさにこれと同じ動きをして、ある関数fが連続して同じ値のものをベクタにする。

;; #33 replicat a sequence
;; (fn [xs n] (mapcat #(repeat n %) xs))

;; #80 A half truth
;; どれか一つがtrueであること。全部false, trueではダメ

(fn [& c] (and (not-every? true? c) (not-every? false? c)))

;; not=   ; 確かに、全部同じものだけを除外なので、合っている！ すごい。

