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


;; #122 Read a binary number
;; まずは実直に
(defn bin [coll]
  (let [ordered (map #(- (int %) 48) (reverse coll))]
    (loop [sum 0 col ordered base 1]
      (println sum base col)
      (if (empty? col)
        sum
        (recur (+ sum (* base (first col))) (rest col) (* base 2))
        ))))

;; code golf狙い
(defn bin [c]
  (reduce + (map-indexed #(* (Math/pow 2 %1) (- (int %2) 48)) (reverse c))))

;; もっと
(defn bin [c]
  (->> (reverse c)
       (map-indexed #(* (Math/pow 2 %1) (- (int %2) 48)))
       (reduce +)
       (int)))

;; 回答
;; #(read-string (str 2 \r %))
;; reduce #(+ %1 %1 (- (int %2) 48)) 0
;; #(Integer/parseInt % 2)

;; #88 symmetric difference
(defn mydiff [l r]
  (into (filter #(not (l %)) r) (filter #(not (r %)) l))) ; これだと結果がlistなので、setしないといけない

(defn mydiff [l r]
  (reduce #(if (not (% %2)) (conj % %2) (disj % %2)) l r))

;; #(set (concat (apply disj %1 %2) (apply disj %2 %1))) ; (apply disj %1 %2) で、differenceと同じことをやっている!
;; applyが複数引数をとるときは、それがそのまま残る (apply f #{1 2 3} #{4 5 6}) -> (f #{1 2 3} 4 5 6)


;; #135 infix notation

(defn infix [l & r]
  (println l ":" r)
  (if (next r)
    ((first r) l (apply infix (rest r)))
    l))

;; 上だと「ちゃんとした」中間置法になってしまう。

(defn infix [x & col]
  (reduce (fn [v [o a]] (o v a)) x (partition 2 col)))

;; #97 パスカルの三角形
(defn pascal [x]
  (->
   (fn c [i n]
     (if (or (= i 0) (= i (dec n)))
       1
       (+ (c (dec i) (dec n)) (c i (dec n)))))
   (map-indexed (repeat x x))
   (vec)))


;; (fn [n]
;;   (nth
;;     (iterate #(map +' (concat [0] %) (concat % [0])) [1])
;;    (dec n)

;;    )) ; これはすごい。頭良い。

;; #157 indexing sequences
(defn idx [coll]
  (map #(list % %2) coll (range)))

;; もしくは
(fn [c]
  (map-indexed #(list %2 %) c))

;; map-indexed #(vector %2 %1) ; ほぼ同じ。確かに。
;; #(map vector % (range)) ; 確かにこれでいい。

;; #118 Re-implement map
(defn m [f c]
  (if (empty? c)
    c
    (cons (f (first c)) (lazy-seq (m f (rest c))))))

;; (fn [f x] (rest (reductions #(f %2) nil x))) ; あーなるほど。

;; #120 sum of square

(defn filt [i]
  (->> (map #(Math/pow (- (int %) 48) 2) (str i))
       (apply +)
       (< i)))


(fn [c]
  (->
   (fn [i]
     (->> (map #(Math/pow (- (int %) 48) 2) (str i))
          (apply +)
          (< i)))
   (filter c)
   (count)))

;; (fn [xs]
;;   (count
;;     (filter
;;       (fn [x] (< x (reduce + (map #(* % %) (map (comp read-string str) (str x))))))
;;       xs)))

;; 桁毎にばらすのは (map (comp read-string str) (str x)) イディオムにしてもいいかも

;; #95 to Tree, or not to tree

(defn node? [c]
  (try
    (if (nil? c)
      true
      (and (node? (first (rest c))) (node? (second (rest c)))))
    (catch Exception e false)))

;; catchは使ってはいけない!
(defn node? [c]
  (if (nil? c)
    true
    (if (and (sequential? c) (= (count c) 3))
      (and (node? (first (rest c))) (node? (second (rest c))))
      false)))

;; 同じような回答
(fn tree? [x]
   (or
     (nil? x)
     (and
       (sequential? x)
       (= 3 (count x))
       (not (sequential? (first x)))
       (tree? (second x))
       (tree? (nth x 2))
       )))

;; letfn を使う例
(letfn [(binary-tree?  [t]
            (and (= (count t) 3)
                 (every? node? (rest t))))
          (node? [x]
            (or (nil? x)
                (and (coll? x)
                     (binary-tree? x))))]
    binary-tree?)

;; #128 recognize playing cards

(defn card [s]
  (let [cards (merge {\D :diamond \H :heart \C :club \S :spade \A 12 \K 11 \Q 10 \J 9 \T 8} (zipmap (map char (range 50 58)) (range)))]
    (apply #(hash-map :suit (cards %) :rank (cards %2)) s)))

;; 圧縮
;; #(let [cards (merge {\D :diamond \H :heart \C :club \S :spade \A 12 \K 11 \Q 10 \J 9 \T 8} (zipmap (map char (range 50 58)) (range)))]
;;     (apply (fn [l r] {:suit (cards l) :rank (cards r)}) %))

;; (#(fn [[a b]]
;;   {:suit (% a) :rank (if (% b) (% b) (- (int b) 50))}
;;   )
;; (zipmap "DHCTJQKA" [:diamond :heart :club 8 9 10 11 12]))
;; 2文字固定ならapply使わなくても引数で分割できるし、数値の部分はこのようにelse部分に書いてもよい

;; (fn [lp]
;;   (let [suit (condp = (first lp)
;;                \S :spade
;;                \H :heart
;;                \D :diamond
;;                \C :club)
;;         rank (condp = (second lp)
;;                \2 0
;;                \3 1
;;                \4 2
;;                \5 3
;;                \6 4
;;                \7 5
;;                \8 6
;;                \9 7
;;                \T 8
;;                \J 9
;;                \Q 10
;;                \K 11
;;                \A 12)]
;;     {:suit suit :rank rank}))
;; condp の利用例。condp はpredと値をとって、case文を実現する

;; # 96 Beauty of Symmetry
(defn sym [a b]
  (println a ":" b)
  (or
   (= a b nil)
   (and
    (sequential? a)
    (sequential? b)
    (sym (nth a 1) (nth b 2))
    (sym (nth a 2) (nth b 1)))
   false
  ))

;; (fn [[_ l r]]
;;     (letfn [(mirror [t]
;;               (when-let [[v l r] t]
;;                 [v (mirror r) (mirror l)]))]
;;       (= l (mirror r))))
;; ミラー関数を作成するやりかた。 (= [...] '(...)) を使っている

;; #100 Least Common Multiple
(defn lcm [& args]
  (letfn [(gcd [x y] (if (= 0 y) x (gcd y (rem x y))))]
    (let [nums (map #(if (ratio? %) (numerator %) %) args)
          divs (map #(if (ratio? %) (denominator %) 1) args)
          n (reduce gcd nums)
          d (reduce gcd divs)]
      (/ (reduce #(* % (/ %2 n)) 1 nums) d)
      )))


(fn [n & nums]
    (first
      (filter
        (fn [m] (every? #(zero? (rem m %)) nums))
        (iterate #(+ % n) n))))

(fn [& xs]
  (/ (apply * xs)
    (reduce #(if (zero? %2) % (recur %2 (mod % %2))) xs)))

;; #147 Pascal's Trapezoid
;; (fn [n]
;;   (nth
;;     (iterate #(map +' (concat [0] %) (concat % [0])) [1])
;;    (dec n) ; 参考
(defn trapezoid [c]
  (iterate #(vec (map +' (concat [0] %) (concat % [0]))) c))

(partial iterate
           (fn [nums]
             (vec
               (map +' (conj nums 0) (cons 0 nums)))))

;; なかなか短い

;; #146 Tree into Tables
(defn flatmap [m]
  (letfn [(mkmap [c k v]
                 (if (map? v)
                   (reduce-kv #(assoc % (vector k %2) %3) c v)
                   (assoc c k v)))]
    (reduce-kv mkmap {} m)))

;; 短いバージョン
(defn fltmap [m]
  (reduce-kv
   (fn [c k v]
     (reduce-kv #(assoc % (vector k %2) %3) c v))
   {} m))

;; (fn [x] (into {} (for [[k v] x [vk vv] v] [[k vk] vv])))
;; forを使ってmapを捜査する。[k v] x が元のMap (Level1), [vk vv] vがvの中身（Level2)

;; #153 Pairwise Disjoint Sets
(defn pdisj [s]
  (let [elem (reduce #(+ % (count %2)) 0 s)
        merged (count (reduce into s))]
    (= elem merged)))

;; golf用
(fn [s]
  (= (reduce #(+ % (count %2)) 0 s) (count (reduce into s))))

;; #(apply distinct? (apply concat %))


;; 44 rotate sequecne
(defn rotate [n c]
  (let [l (count c)]
    (cond (= n 0) c
          (> n 0) (recur (rem (- n l) l) c)
          (< n 0) (recur (inc n) (cons (last c) (butlast c))))))

;; modは負数に対しても使える
#(let [n (mod % (count %2))]
         (concat (drop n %2)
                 (take n %2)
                 ))

;; #43 reverse interleave
(defn revint [c n]
  (->> (partition n c)
       (apply interleave)
       (partition (/ (count c) n))))

;; #(apply (partial map list) (partition %2 %1)) ;; (partial map list) で、 (a b) (c d) (e f) -> (a c e) (b d f)

;; #55 Count Occurances
(defn freq [c]
  (reduce #(assoc % %2 (inc (% %2 0))) c))

;; #56 Find distinct items
(defn d [c]
  (if-not (empty? c)
    (let [a (first c)]
      (cons a (d (remove #(= a %) (rest c)))))))

;; reduce (fn [s e]
;;   (if (some #(= % e) s)
;;     s
;;     (conj s e)))
;; []

;; reduceを使ってdistinctを実現するうまいやり方。ベクタ中に既に要素があればそのまま、なければconj.
;; ちなみにcontains? はベクタに対してはインデックスがあるかないかを調べるため使えない。

(defn dis [c]
  (reduce (fn [s e]
            (if (some #(= % e) s) s (conj s e))) [] c))

;; #58 Function Composition
(defn c [f & fs]
  (println f)
  (if (empty? fs) (fn [& a] (apply f a)) (fn [& a] (f (apply (apply c fs) a)))))
;; 長い!!

(defn mycomp [& fs]
  (fn [& args]
    (reduce #(%2 %) (apply (last fs) args) (rest (reverse fs)))))

(fn [& x] (reduce (fn [f g] (fn [& a] (f (apply g a)))) x))

;; #59 Juxtaposition 併記
(defn myjuxt [& fs]
  (fn [& args]
    (reduce #(conj % (apply %2 args)) [] fs)))

;; (fn [& x] (fn [& a] (map #(apply % a) x))) ; 別にReduceする必要はない。関数の数と一緒だから。

;; #54 Partition a sequence
(fn [n c]
  (filter #(= n (count %)) (vals (group-by #(quot % n) c))))

;; まともな解法
(fn f [n v]
  (if (>= (count v) n)
    (cons (take n v) (f n (drop n v)))))

;; #70 Word Sorting

(fn [c]
	(sort #(compare (.toLowerCase %) (.toLowerCase %2)) (re-seq #"\w+" c)))

#(sort-by (fn [v](.toLowerCase v))  (re-seq #"\w+" %))

;; #67 Prime Numbers
;; (defn prime [ps ds]
;;   (println ps ":" ds)
;;   (lazy-seq
;;    (let [x (first ds)]
;;      (println class x ":" x)
;;      (if (some #(= 0 (/ x %)) ps)
;;        (prime ps (rest ds))
;;        (cons x (prime (conj ps x) (rest ds)))
;;        ))))

;; これ動かない。なんでだろ? (NullPointerException)
;; (defn prime [ps ds]
;; ;;   (println ps ":" ds)
;;   (when-let [x (first ds)]
;;     (lazy-seq
;;      (if (some #(= 0 (mod x %)) ps)
;;        (prime ps (rest ds))
;;        (cons x (prime (conj ps x) (rest ds)))
;;        ))))

;; lazy-seq は seqが呼ばれた時に評価される。この場合は (take 1 (prime [1] (drop 2 (range 10)))) のように呼び出していただめ、
;; 最初の要素が出るまでprimeを再帰的に呼び出してしまっていた
;; trace-form で発見できる
;; (take 1 (prime [2] (drop 2 (range 10)))) ならOKだが、これだと 出力は 3 になり、まちがい。

(defn prime [n]
  (take n (cons 2
        (lazy-seq
         (letfn [(prime [ps ds]
                        (when-let [x (first ds)]
                          (if (some #(= 0 (mod x %)) ps)
                            (recur ps (rest ds))
                            (lazy-seq (cons x (prime (conj ps x) (rest ds)))))))]
           (prime [2] (drop 3 (range))))
         ))))

;; コードゴルフ 短くなくてもいいなら、n-1 までの自然数で割れば良い
(fn [n]
  (->>
  (range)
  (drop 2)
  (filter (fn [x] (every? #(< 0 (mod x %)) (range 2 x))))
  (take n)))


;; #74 Filter Perfect Squares

(defn filsqr [s]
  (->> (re-seq #"\d+" s)
       (map #(Integer/valueOf %))
       (filter (fn [x]
                 ((set (map #(* % %) (range (inc x)))) x)))
       (interpose \,)
       (apply str)))

(fn [strs]
  (apply str
    (interpose ","
      (filter
        #(= 0.0 (mod (Math/sqrt %) 1))
        (map #(Integer/parseInt %) (.split strs ",")))))) ; Javaの関数をうまく利用

;; #65 Black box testing
(defn mytype [s]
  (let [x [:x 2]
        l (conj (conj (conj s [:x 1]) x) x)
        i (count s)
        j (count l)]
    (cond
     (= j (inc i)) :map
     (= j (+ 2 i)) :set
     (= x (first l)) :list
     true :vector)))

;; これは結構いいアイディアだと思ったんだが

;; #({{} :map #{} :set} (empty %) (if (reversible? %) :vector :list))
;; emptyは同一カテゴリの空を返す () = [] なので、reversible で判定している。リストはreversibleではない

;; #(condp = (nth (str %) 0)
;;    \{ :map
;;    \c :list
;;    \[ :vector
;;    \# :set)

;; インチキだ


;; 102 Capitalize
(fn [s]
  (reduce #(str % (clojure.string/capitalize %2)) (.split s "-")))

;; #(clojure.string/replace % #"-(\w)" (fn [[a b]] (clojure.string/capitalize b))) ; 正規表現による置換

;; #80 perfect number
(defn perfect [x]
  (let [s (filter #(= 0 (mod x %)) (range 1 x))]
    (= x (apply + s))))

;; #85 Power set
;; (defn pows [s]
;;   (conj
;;    (set
;;     (reduce #(for [x % y %2] (conj x y)) #{#{}} (repeat (count s) s)))
;;    #{}))

;; (pows (into #{} (range 10))) で帰ってこなくなるくらい遅い
;; これは途中の計算で重複が許されてしまっているため。

(defn pows [s]
  (conj
    (reduce #(set (for [x % y %2] (conj x y))) #{#{}} (repeat (count s) s))
   #{}))

;; 改良版。途中の重複をsetで消している。

(fn pow [s] (if (empty? s) #{s} (let [
    s1 (first s)
    sn (disj s s1)
    pp (pow sn)
    pn (for [i pp] (conj i s1))]
    (into pp pn))))

;; 再帰による解法

;; #77 Anagram Finder
(defn ana [c]
  (reduce
   #(let [v (val %2)] (if (< 1 (count v)) (conj % (set v)) %)) #{}
   (group-by frequencies c)))

(fn [words]
  (set (map set (filter #(< 1 (count %))
                        (vals (group-by sort words))))))

;; sortで良かった。。。

;; #60 Sequence reductions
(defn myred
  ([f c] (myred f (first c) (rest c)))
  ([f i c]
   (lazy-seq
    (cons i
          (lazy-seq (if-not (empty? c) (myred f (f i (first c)) (rest c)) ))))))

