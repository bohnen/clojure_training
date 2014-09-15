(ns cookbook.clisk
  (:use clisk.live))

(def vblocks
  (v* 5.0
      (voronoi-function
       `(Math/sqrt (- (* ~'y ~'y) (* ~'x ~'x))))))

(defn sample1
  []
  (show (render-lit
         (seamless vplasma)
         (v+ (v* 0.2 (seamless 0.2 (rotate 0.1 plasma)))
             (v* 0.6 vblocks)))))
