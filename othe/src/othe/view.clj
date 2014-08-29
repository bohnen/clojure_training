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

(defn- view-thread
  "ユーザー入力を監視"
  []
  (loop [pos (wait-for-cmd)]
    (when pos
      (command-handler [:move pos])
      (recur (wait-for-cmd))))
  (command-handler [:exit]))

(defn start-ui
  "ユーザーインタラクション"
  []
  (.start (Thread. view-thread)))

(defn- wait-for-cmd
  "ユーザー入力を待ち、nilかposを返す"
  []
  (loop [line (read-cmd)]
    (if (empty? line)
      (println "Exiting...")
      (if-let [pos (pos-from-line line)]
        pos
        (do
          (print "Input should be like a1 or b2. Or Enter to exit: ")
          (flush)
          (recur (read-cmd)))))))

(defn- read-cmd
  "stdinから、コマンドを読む"
  []
  (print (if (is-black-turn?)
           "It's BLACK's turn: "
           "HEY WHITE, your turn: "))
  (flush)
  (read-line))

(defn- col-from-line
  "ユーザー入力から桁を解読"
  [line]
  (.indexOf col-headers (subs line 0 1)))

(defn- row-from-line
  "ユーザー入力から行を解読"
  [line]
  (dec (read-string (subs line 1))))

(defn- pos-from-line
  "ユーザー入力からposを解読。不正な入力値ならnil"
  [line]
  (when (re-find #"^[a-h][1-8]$" line)
    (let [r (row-from-line line)
          c (col-from-line line)]
      (pos-from-rowcol r c))))

;; TODO 実行

