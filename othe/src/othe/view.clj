(ns othe.view
  (:use othe.model
        [clojure.string :only (join)]))

(def code-a 97)
(def code-curly 123)
(def col-headers
  "カラムヘッダを返す。A-Zまでのアルファベット"
  (take b-size
        (map (comp str char)
             (range code-a code-curly))))

(def col-header-str
  (str "  " (join " " col-headers)))

(defn- st-str
  "マスの状態を表す文字列"
  [st]
  (cond
   (= st :b) "x"
   (= st :w) "o"
   :else " "))

(defn- board-strs
  "ボードの各行を書いたもの"
  [brd]
  (for [row (partition b-size brd)]
    (join "  " (map st-str row))))

(defn- board-strs-with-row
  "行番号つき board-str"
  [brd]
  (map str
       (range (inc first-row) (inc last-row))
       (repeat b-size " ")
       (board-strs brd)))


(def separator (join (repeat 50 \-)))

(defn- score-str
  "スコア文字列"
  [bs ws]
  (let [s (str "BLACK(x):" bs ",WHITE(o):" ws)]
    (format "%50s" s)))

(defn- winner-str
  "勝者文字列"
  [bs ws]
  (cond
   (> bs ws) "Winner is BLACK. Congratulations!"
   (> ws bs) "Yeah, WHITE won!!!"
   :else "It's a draw game."))

(defn- redraw-board
  "盤面を表示"
  []
  (println col-header-str)
  (dorun
   (map println
        (board-strs-with-row (retrieve-board)))))

(defn on-state-changed
  "Model変化時のハンドラ"
  [& e]
  (if e
    (print "You can't move there. Input again:")
    (let [bs (count-blacks)
          ws (count-whites)]
      (println separator)
      (println (score-str bs ws))
      (redraw-board)
      (when (is-game-over?)
        (println (str "GAME OVER: " (winner-str bs ws)))
        (command-handler [:exit])))))

(defn init-view
  "viewを初期化. handlerは、ユーザーコマンドのハンドラ"
  [handler]
  (println "Welcome to the Battle Zone!")
  (println "'x' is Black and 'o' is white.")
  (println "Input the column name first, like 'a1' or 'b2'")
  (println "Just hit Enter to exit.")
  (def command-handler handler))
