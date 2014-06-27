(ns pinger.core
  (:import (java.net URL HttpURLConnection))
  (:gen-class))

(defn response-code [address]
  (let [conn ^HttpURLConnection (.openConnection (URL. address))
        code (.getResponseCode conn)]
    (when (< code 400)
      (-> conn .getInputStream .close))
    code))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn available? [address]
  (= 200 (response-code address)))

;; (response-code "http://www.google.com")
;; (available? "http://www.google.com")
;; (available? "http://www.google.com/badurl")

(defn -main []
  (let [addresses '("http://www.google.com"
                    "http://www.amazon.com"
                    "http://www.google.com/badurl")]
    (while true
      (doseq [address addresses]
        (println (available? address)))
      (Thread/sleep (* 1000 60)))))
