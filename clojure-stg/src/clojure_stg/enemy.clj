(ns clojure-stg.enemy
  (:require [quil.core :as q]))

(defrecord Enemy [x y w h vx vy])

(defn enemy-move [{:keys [vx vy] :as enemy}]
    (-> enemy
        (update-in [:x] #(+ % vx))
        (update-in [:y] #(+ % vy))))

(defn draw-enemy [{:keys [x y w h]}]
  "敵を描画する"
  (let [w2 (/ w 2) h2 (/ h 2)]
    (q/rect (- x w2) (- y h2) w h)))

(defn draw-enemies [es]
  "敵のリストを描画する"
  (doseq [e es] (draw-enemy e)))

(defn move-enemies [es]
  "敵のリストを動かす"
  (map enemy-move es))

(defn out-of-field? [{:keys [x y w h]}]
  (let [w2 (/ w 2)
        h2 (/ h 2)]
    (or (< (+ x w2) 0)
        (> (- x w2) (q/width))
        (< (+ y h2) 0)
        (> (- y h2) (q/height)))))

(defn gc-enemies [es]
  "はみでた敵を削除する"
  (println (count es))
  (remove out-of-field? es))
