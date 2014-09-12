(ns clojure-stg.hit)

;; 円形のアタリ判定
(defrecord Hit-region [x y r])

(defn hit? [reg1 reg2]
  (let [dx (- (:x reg1) (:x reg2))
        dy (- (:y reg1) (:y reg2))
        rs (+ (:r reg1) (:r reg2))]
    (<= (+ (* dx dx) (* dy dy)) (* rs rs))))
