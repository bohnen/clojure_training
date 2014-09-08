(ns clojure-stg.character
  (:require [quil.core :as q]))

(def player-speed 5)
(def player-width 50)
(def player-height 50)

;; util
(def sqrt #(. Math sqrt %))

(defrecord Player [x y])
(defn- move [pyr dx dy speed]
  (-> pyr
    (update-in [:x] #(+ % (* dx speed)))
    (update-in [:y] #(+ % (* dy speed)))))

;; TODO 汎用にするには、(/ dx (sqrt (+ (* dx dx) (* dy dy))))
(defn- ch-move [pyr dx dy speed]
  (if (and (not= dx 0) (not= dy 0))
    (let [l (sqrt (+ (* dx dx) (* dy dy)))]
      (move pyr (/ dx l) (/ dy l) speed))
    (move pyr dx dy speed)))

;; 画面からはみ出しているかどうか
(defn- fix-position [pyr]
  (let [x (:x pyr)
        hw (/ player-width 2)
        dx (cond
            (< (- x hw) 0) hw
            (> (+ x hw) (q/width)) (- (q/width) hw)
            :else x)
        y (:y pyr)
        hh (/ player-height 2)
        dy (cond
            (< (- y hh) 0) hh
            (> (+ y hh) (q/height)) (- (q/height) hh)
            :else y)]
    (-> pyr
        (assoc :x dx)
        (assoc :y dy))))

(defn update-player [state]
  (let [dx (cond
            (:left state) -1
            (:right state) 1
            :else 0)
        dy (cond
            (:up state) -1
            (:down state) 1
            :else 0)]
    (-> state
      (update-in [:player] ch-move dx dy player-speed)
      (update-in [:player] fix-position))))

(defn draw-player [state]
  (let [p (:player state)]
    (q/ellipse (:x p) (:y p) player-width player-height)))
