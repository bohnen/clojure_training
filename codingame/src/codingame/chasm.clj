(ns Player
  (:gen-class))

; Auto-generated code below aims at helping you parse
; the standard input according to the problem statement.

(defn -main [& args]
  (let [R (read) G (read) L (read)]
    ; R: the length of the road before the gap.
    ; G: the length of the gap.
    ; L: the length of the landing platform.
    (while true
      (let [S (read) X (read)]
        (->>
         (cond
          (>= X (+ R G)) "SLOW"
          (> (+ X S) R) "JUMP"
          (<= S G) "SPEED"
          (> S (G + 1)) "SLOW"
          true "WAIT")
         (println))))))
