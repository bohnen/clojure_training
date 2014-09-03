(ns clojure-stg.character)

(def player-speed 5)

;; util
(def sqrt #(. Math sqrt %))

(defrecord Player [x y])
(defn- move [chr dx dy speed]
  (-> chr
    (assoc :x (* dx speed))
    (assoc :y (* dy speed))))

;; TODO 汎用にするには、(/ dx (sqrt (+ (* dx dx) (* dy dy))))
(defn ch-move [chr dx dy speed]
  (if (and (not= dx 0) (not= dy 0))
    (let [l (sqrt (+ (* dx dx) (* dy dy)))]
      (move chr (/ dx l) (/ dy l) speed))
    (move chr dx dy speed)))



