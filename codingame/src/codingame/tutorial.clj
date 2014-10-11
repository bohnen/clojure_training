(ns Player
  (:gen-class))

; The code below will read all the game information for you.
; On each game turn, information will be available on the standard input, you will be sent:
; -> the total number of visible enemies
; -> for each enemy, its name and distance from you
; The system will wait for you to write an enemy name on the standard output.
; Once you have designated a target:
; -> the cannon will shoot
; -> the enemies will move
; -> new info will be available for you to read on the standard input.

(defn- en-name []
    (let [n (read)]
         (->> (repeatedly (* n 2) read)
              (partition 2)
              (map #(zipmap [:name :dist] %))
              (apply min-key :dist)
              :name)))

(defn -main [& args]
  (while true
    (println (en-name))))
