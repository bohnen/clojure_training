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

;; #22 sum of list
;; (reduce + '(1 2 3))
;; (apply + '(1 2 3))


;; #22 Write a function which returns the total number of elements in a sequence.
(defn mycount [coll]
  (reduce (fn [x y] (+ x 1)) 0 coll))

;; #(reduce (fn [x _] (+ 1 x)) 0 %)

;; #21 Write a function which returns the Nth element from a sequence.
(fn [coll n]
  (if (zero? n) (first coll) (recur (rest coll) (dec n))))

;; #(first (drop %2 %))
;; #((vec %1) %2)  ;; これはvectorが数値を引数として [i] を返す特性を利用したもの! すごい。
