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

;; #134
;; #(and (contains? %2 %1) (nil? (%2 %1)))

;; #(not (%2 % 1)) ; ({:a nil :b "a"} :a 1 ) とすることで、:a が無いときのデフォルトを1 にすることができる
;; notは false/nil の時true, それ以外はfalseになる。

;; #156 Map defaults
(fn [x coll] (reduce #(assoc %1 %2 x) {} coll))

;; #(into {} (map vector %2 (repeat %))) ;; into を利用して作成
;; #(zipmap %2 (repeat %1)) ;; zipmap 関数を使う。zipmapは便利だな。
;; repeatなどの遅延コレクションを使うことで 合成系関数が使える。全部を初期化したいときなどに便利。たとえば二次元配列とか。

;; #27 Palindrome detector
(fn [coll]
  (every? #(apply = %) (map vector coll (reverse coll))))

;; #(= (reverse %) (seq %)) ; そりゃそうだ。コレクションへの変換 (seq, vec) などを活用したいところ。


;; #32 Write a function which duplicates each element of a sequence
(fn [col]
 (reduce #(conj %1 %2 %2) [] col))

;; #(interleave % %) ; なるほど
;; mapcat #(list % %)

;; #61 map collection コレクションからマップを作成

;; #(apply hash-map (interleave % %2))
;; #(into {} (map vector % %2)) ; map にintoできるのは、([a b][c d])

;; #66 Greatest common divisor

(fn [x y]
  (last (filter #(and (zero? (mod x %)) (zero? (mod y %))) (range 1 (inc (min x y))))))

;; (fn gcd [x y]
;;   (if (= y 0)
;;     x
;;     (gcd y (rem x y))))  ;; ユークリッドの互除法

;; #81 Set Intersection

#(set (remove nil? (map % %2)))
;; (comp set filter) ; #{...} は関数として使えるので、setに存在するものだけを返すのは filterでできる。そこにsetを合成するという技。

;; #62 re-implement iterate
(fn t [f i]
  (cons i (lazy-seq (t f (f i)))))

;; #(reductions (fn [i _] (%1 i)) (repeat %2)) ; reductionを使えば繰り返し関数を適用することができる

;; #107 Simple closures0

#(fn [x] (int (Math/pow x %)))

;; (fn [n] #(Math/pow % n)) ; 関数を返す関数リテラルよりも、関数リテラルを返す関数の方がコードは少ない
;; partial #(Math/pow %2 %1) ; partialを使うことで、変数束縛した関数を返すことができる

;; #99 Product Digits  (= (__ 99 9) [8 9 1])
#(loop [ret '() n (* % %2)]
   (if (= n 0)
     (vec ret)
     (recur (cons (mod n 10) ret) (quot n 10))))

;; (comp (partial map #(- (int %) 48)) str *)  計算結果を文字列にして、それぞれの文字をintにする。
;; int関数だと文字のASCIIコードがかえるため、それを (- (int %) 48) で調整している。

;; #63 Group a sequence group-byのような関数を作れ
(defn myg [f c]
  (->> (map vector (map f c) c)
       (reduce (fn [m [k v]] (assoc m k (conj (m k []) v))) {})))

;; (fn [f s]
;;   (apply merge-with concat (map #(hash-map (f %) [%]) s)))

;; #(reduce (fn [m v] (assoc m (% v) (conj (m (% v) []) v))) {} %2)
