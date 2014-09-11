(ns clojure-stg.enemy
  (:require [quil.core :as q]))

(defrecord Enemy [x y w h vx vy])

(defn enemy-move [enemy]
  (let [vx (:vx enemy)
        vy (:vy enemy)]
    (-> enemy
        (update-in [:x] #(+ % vx))
        (update-in [:y] #(+ % vy)))))

(defn draw-enemy [e]
  "敵を描画する"
  (q/rect (:x e) (:y e) (:w e) (:h e)))

(defn draw-enemies [es]
  "敵のリストを描画する"
  (doseq [e es] (draw-enemy e)))

(defn move-enemies [es]
  "敵のリストを動かす"
  (map enemy-move es))


