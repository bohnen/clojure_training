(ns clojure-stg.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:gen-class))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :hsb)
  {:left nil :right nil :color 0})

(defn update [state]
  state)

(defn draw [state]
  (q/background 240)
  (q/fill (:color state) 255 255)
  (q/text (str "left: " (:left state) " right: " (:right state)) 300,200))

(defn update-key-state
  [key key-press state]
  (cond
   (= :left key) (assoc state :left key-press)
   (= :right key) (assoc state :right key-press)
   :else state))


(defn keypressed
  [state {:keys [key key-code raw-key]}]
  ;; ここで、キーの同時押しに対応しよう
  ;; 必要なキーの情報を保持するstateを考えなければいけない。ただのmapでいいか。
  (println key)
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