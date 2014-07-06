(ns quiz.forclojure)
;; quiz from http://www.4clojure.com

;; #19 Write a function which returns the last element in a sequence.

(defn laste [coll]
  (fn [coll]
    (if-not (next coll)
      (first coll)
      (recur (rest coll)))))

;; (comp first reverse)


;; #20 Write a function which returns the second to last element from a sequence.

(defn q20 [coll]
  ((comp first (partial take-last 2)) coll))

;; (comp peek pop vec)
;; #(second (reverse %))

;; #21 Write a function which reverses a sequence.

(defn q21 [coll]
  (loop [ret [] coll coll]
    (if (empty? coll)
      ret
      (recur (conj ret (last coll)) (butlast coll)))))

;; リストの場合、conjは逆順になるという性質を利用できる
;; #(into () %)
;; #(reduce conj () %)

