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


(def initial-oprs
  "ゲームの初期状態(:bと:wが2個ずつ)を表すoprのマップ"
  (let [cntr (dec (quot b-size 2))
        pos (pos-from-rowcol cntr cntr)]
    {pos :b
     ((successor :se) pos) :b
     ((successor :e) pos) :w
     ((successor :s) pos) :w}))

(defn- board-manipulator
  "oprのマップに基づいて、盤面を変更するラムダ"
  [oprs]
  (fn [pos st] (if-let [s (oprs pos)]
                 s st)))

(defn- manipulated-board
  "manipulatorを盤面に対して呼んだ後の新しい盤面"
  [brd manip]
  (vec (map-indexed manip brd)))

;; ゲーム初期状態
;; (let
;;   [blank
;;    (vec (repeat (- last-pos first-pos) :free))
;;    manip (board-manipulator initial-oprs)]
;;   (manipulated-board blank manip))

(defn- make-oprs
  "poslineに関して、bwにとってのoprを計算する"
  [brd posline bw]
  (reduce (fn [m pos] (assoc m pos bw)) {}
          (take-while
           (fn [pos] (opponent? brd pos bw))
           posline)))

(defn- make-all-oprs
  "posにおける全poslineに関して、bwにとってのoprを計算する"
  [brd pos bw]
  (apply merge
         (cons {pos bw}
               (for [posline
                     (filter
                      (fn [pos] (clamping? brd pos bw))
                      (all-poslines pos))]
                 (make-oprs brd posline bw)))))


(defn init-game
  "新しいゲームを始める"
  [ob]
  (dosync
   (let [blank (vec (repeat (- last-pos first-pos) :free))
         manip (board-manipulator initial-oprs)]
     (ref-set board (manipulated-board blank manip)))
   (ref-set player :b)
   (def observer ob))
  (observer))

(defn- opponent [bw] (if (= bw :b) :w :b))

(defn- has-pos-to-play?
  "bwにとって、打てる場所はあるか?"
  [brd bw]
  (not-empty
   (filter (fn [pos] (playable? brd pos bw) all-pos))))

(defn- next-player
  "bwの次は誰の番か決める"
  [bw brd]
  (let [nbw (opponent bw)]
    (if (has-pos-to-play? brd nbw) nbw bw)))

(defn play-move
  "posへうつ"
  [pos]
  (dosync
   (if (not (playable? (deref board) pos (deref player)))
     (observer :err)
     (do
       (let
         [manip (board-manipulator (make-all-oprs (deref board) pos (deref player)))]
         (alter board manipulated-board manip))
       (alter player next-player (deref board))
       (observer)))))


