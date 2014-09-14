(ns leiningen.stock
  ; to parse xml
  (:use clojure.xml)
  (:use clojure.java.io))

(def base "http://www.google.com/ig/api?stock=")

(defn get-quote [key]
  (->
   (get (-> (parse (input-stream (str base key))) :content first :content) 10)
   :attrs
   :data))

;; method called from leiningen
(defn stock [project & args]
  (println (get-quote (first args))))
