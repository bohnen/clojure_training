(ns simple-java.core
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main [& args]
  (->> args
    (interpose "")
    (apply str)
    (foo)))

;; (-main)
