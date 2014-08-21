(ns othe.model)

(def b-size 8)
(def first-pos 0)
(def last-pos (* b-size b-size))
(def all-pos (range first-pos last-pos))
(def first-col 0)
(def last-col b-size)
(def first-row 0)
(def last-row b-size)

(defn col-from-pos [pos] (mod pos b-size))
(defn row-from-pos [pos] (quot pos b-size))
(defn pos-from-rowcol [r c] (+ (* r b-size) c))

(def dirs #{:n :ne :e :se :s :sw :w :nw})

(def board (ref []))
(def player (ref nil))

(def successor
  "各方向の隣接マスを返す関数のマップを返す"
  (let [north (fn [pos] (- pos b-size))
        east inc
        south (fn [pos] (+ pos b-size))
        west dec]
    {:n north
     :ne (comp north east)
     :e east
     :se (comp south east)
     :s south
     :sw (comp south west)
     :w west
     :nw (comp north west)}))

(def not-wrapped?
  "盤の端に到達していないか"
  (let [east? (fn [pos] (> (col-from-pos pos) first-col))
        west? (fn [pos] (< (col-from-pos pos) (dec last-col)))]
    {:n identity
     :ne east?
     :e east?
     :se east?
     :s identity
     :sw west?
     :w west?
     :nw west?}))

(defn- in-board? [pos]
  "盤に存在するか"
  (and (>= pos first-pos) (< pos last-pos)))

(defn- posline-for-dir
  "posにおけるdir方向へのposline"
  [pos dir]
  (let [suc (successor dir)
        nwrap? (not-wrapped? dir)]
    (take-while
     (fn [pos]
       (and (nwrap? pos) (in-board? pos)))
     (iterate suc (suc pos)))))

;; 小道具
(defn- free? [brd pos] (= (brd pos) :free))

;; (= (brd pos) bw) だけでもいい気がするのだが
(defn- self? [brd pos bw]
  (and (not (free? brd pos)) (= (brd pos) bw)))

;; not self? にできないものか?
(defn- opponent? [brd pos bw]
  (and (not (free? brd pos)) (not= (brd pos) bw)))

(defn- all-poslines
  "posにおける、各方向へのposlineを集めたシーケンス"
  [pos]
  (filter not-empty
          (for [dir dirs] (posline-for-dir pos dir))))

(defn- clamping?
  "bwにとって、poslineは、挟めるか"
  [brd posline bw]
  (and
   (opponent? brd (first posline) bw)
   (if-let
     [fst
      (first
       (filter
        (fn [pos] (not (opponent? brd pos bw)))
        (rest posline)))]
     (self? brd fst bw)
     nil)))

(defn- playable?
  "bwにとって、posは打てる場所か?"
  [brd pos bw]
  (and
   (free? brd pos)
   (some
    (fn [pl] (clamping? brd pl bw))
    (all-poslines pos))))


