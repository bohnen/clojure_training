(ns clojure-stg.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure-stg.character :as ch]
            [clojure.pprint :as pp])
  (:gen-class))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :hsb)
  {:player (ch/->Player (quot (q/width) 2) (quot (q/height) 2)) :color 0})

(defn update [state]
  (let [dx (cond
            (:left state) -1
            (:right state) 1
            :else 0)
        dy (cond
            (:up state) -1
            (:down state) 1
            :else 0)]
    (update-in state [:player] ch/ch-move dx dy ch/player-speed)))

(defn draw [state]
  (let [p (:player state)]
    (q/background 240)
;;     (pp/pprint state)
    (q/fill (:color state) 255 255)
    (q/ellipse (:x p) (:y p) 50 50)))

;; (defn defn-from [str args & body]
;;   `(defn ~(symbol str) ~args ~@body))

;; (defmacro key-state [& keys]
;;   (let [f #(list '= % 'key)
;;         g #(list 'assoc 'state % 'key-press)
;;         l (mapcat #(list (f %) (g %)) keys)]
;;     (defn-from "update-key-state" '[key key-press state]
;;       `(cond ~@l :else ~'state))))

;; マクロで関数を作成するのはかなりトリッキー
;; 関数の引数はquoteする必要がある（名前空間で修飾されたくない）が、
;; ` リーダーマクロ中で　' を使うと(quote ) に展開されてしまう。
;; この場合、~' とする必要があった。

(defmacro key-state [& keys]
  (let [f #(list '= % 'key)
        g #(list 'assoc 'state % 'key-press)
        l (mapcat #(list (f %) (g %)) keys)]
    `(defn ~(symbol "update-key-state") ~'[key key-press state]
      (cond ~@l :else ~'state))))

;; 別の回答

;; STG本ではCLOSとしてクラスとメソッドを定義しているが、
;; clojureではmapと関数で十分だし、スコープを狭めたメソッドを定義することもないような
;; 必要なら名前空間分けて、defn- するか？
;; (defn update-key-state
;;   [key key-press state]
;;   (cond
;;    (= :left key) (assoc state :left key-press)
;;    (= :right key) (assoc state :right key-press)
;;    :else state))

(key-state :left :right :up :down)

(defn keypressed
  [state {:keys [key key-code raw-key]}]
  ;; ここで、キーの同時押しに対応しよう
  ;; 必要なキーの情報を保持するstateを考えなければいけない。ただのmapでいいか。
;;   (println key)
  (update-key-state key true state))

(defn keyreleased
  [state]
  ;; quilのkey-releasedがkeyイベントを扱わないためのhack
  (update-key-state (q/key-as-keyword) nil state))

(defn -main
  [& args]
  (q/defsketch clojure-stg
    :title "テスト"
    :size [640 480]
    :setup setup
    :update update
    :draw draw
    :key-pressed keypressed
    :key-released keyreleased
    :middleware [m/fun-mode]))
;; fun-modeは、関数型でquilを利用できるようにする
;; fun-modeを使うことで、state管理が暗黙的に行われるようになっている
;; setupで返す値がinitial state, updateで返すのがstateの更新、
;; draw関数は値を返さない。
;; 全てのイベントハンドラはstateを引数として、eventをとるものもある。
;;
