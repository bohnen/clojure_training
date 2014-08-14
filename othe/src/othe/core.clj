(ns othe.core
  (:use
   othe.view
   othe.model)
  (:gen-class))

(defn on-command
  [[cmd pos]]
  (cond
   (= cmd :move) (play-move pos)
   (= cmd :exit) (System/exit 0)
   :else nil))

(defn -main
  "entry point"
  [& args]
  (init-view on-command)
  (init-game on-stage-changed)
  (start-ui))
