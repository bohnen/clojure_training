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

;; #26 fibonatti sequence
(fn [n]
  (loop [i 1 j 1 c [] n n]
    (if (zero? n) c (recur j (+ i j) (conj c i) (dec n)))))

;; lazy-sequenceをうまく使った例。これだと必要なシーケンスしか計算しない
;; (fn fibb [n] (take n (map first (iterate (fn [[l r]] [r (+ l r)]) [1 1]))))
;; (fn [i] (take i '(1 1 2 3 5 8 13 21))) ;; これはひどい


;; #38 Write a function which takes a variable number of parameters and returns the maximum value.

(defn mymax [& args]
  (first (sort > args)))

;; (fn [x & xs] (reduce #(if (> %2 %1) %2 %1) x xs)) ;; lazy-seq だしこちらの方が早そう

;; #28 flatten
;; (seq? []) => false  ;; なんだってー
;; (defn flat [coll]
;;   reduce
;;     (fn [l r] (if (or (seq? r) (vector? r)) (apply conj l (flat r)) (conj l r)))
;;     [] coll)

(defn myflat [coll]
  (fn treewalk [res col]
    (if (empty? col) res
      (let [l (first col) r (rest col)]
        (if (sequential? l) (treewalk (treewalk res l) r) (treewalk (conj res l) r)))))
  [] coll)

(defn treeprint [acc coll]
  (let [l (first coll) r (rest coll)]
    (if (sequential? l) (treeprint acc l) (println l))
    (when-not (empty? r) (treeprint r))))

;; 上記は全然ダメだった
;; 他の回答例
;; reduce
;; (fn self [xs e] (concat xs (cond (coll? e) (reduce self '() e) true (list e))))
;; '()

;; (fn _flatten [items]
;;   (if (empty? items)
;;     '()
;;     (concat (if (sequential? (first items))
;;               (_flatten (first items))
;;               (list (first items)))
;;             (_flatten (rest items)))))
