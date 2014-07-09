(ns quil-training.first
  (:use [quil.core]))

(sketch
  :setup (fn []
          (background 20)
          (ellipse 50 50 80 80))
  :title "The moon delights the night")
