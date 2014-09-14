(ns httpkit.core
  (:use org.httpkit.server)
  (:gen-class))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "hello world"})


(defn -main
  []
  (run-server app {}))
