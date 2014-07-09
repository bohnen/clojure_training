(ns quil-training.mirror
  (:use [quil.core]))

(defn setup []
  (background 0)
  (rect-mode :center))

(defn _line [px,py,x,y,deg]
  (let [w (width) h (height)]
    (rotate (radians deg))
    (line (- px (/ w 2)) (- py (/ h 2)) (- x (/ w 2)) (- y (/ h 2)))))

(defn mouse-dragged []
  (let [w (width) h (height)]
    (stroke 0 255 0 255)
    (fill 0 10)
    (rect (/ w 2) (/ h 2) w h)
    (push-matrix)
    (translate (/ w 2) (/ h 2))
    (rect 0 0 50 50)
    (doseq [i (range 8)]
      (_line (pmouse-x) (pmouse-y) (mouse-x) (mouse-y) (* i 45)))
    (pop-matrix)))

(defn draw [])

(defsketch example
  :title "sample"
  :size [480 640]
  :setup setup
  :mouse-dragged mouse-dragged
  :draw draw)
