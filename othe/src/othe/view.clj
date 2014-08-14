(ns othe.view
  (:use othe.model
        [clojure.string :only (join)]))

(def code-a 97)
(def code-curly 123)
(def col-headers
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
