(ns pinger.repl)

(def hello "Hello, REPL!")

(defn class-paths
  "class path on each line"
  []
  (->
   (into {} (System/getProperties))
   (get "java.class.path")
   (clojure.string/split #":")))

(defn print-class-paths
  "print class path sep by newline"
  []
  (doseq [x (class-paths)] (println x)))

