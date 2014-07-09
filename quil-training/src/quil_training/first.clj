(ns quil-training.first
  (:use [overtone.core])
  (:require [quil.core :as q]))

(q/sketch
  :setup (fn []
          (q/background 20)
          (q/ellipse 50 50 80 80)
          (connect-external-server 57110)
          (demo (sin-osc)))
  :title "The moon delights the night")
