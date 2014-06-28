(ns pinger.core
  (:import (java.net URL HttpURLConnection))
  (:require [pinger.scheduler :as scheduler])
  (:gen-class))

(defn response-code [address]
  (let [conn ^HttpURLConnection (.openConnection (URL. address))
        code (.getResponseCode conn)]
    (when (< code 400)
      (-> conn .getInputStream .close))
    code))

(defn available? [address]
  (= 200 (response-code address)))

;; (response-code "http://www.google.com")
;; (available? "http://www.google.com")
;; (available? "http://www.google.com/badurl")

(defn check []
  (let [addresses '("http://www.google.com"
                    "http://www.amazon.com"
                    "http://www.google.com/badurl")]
    (doseq [address addresses]
        (println (available? address)))))

(def immediately 0)
(def every-minute (* 60 1000))
(def fifteen-second (* 15 1000))

(defn start [e]
  "REPL helper. Start pinger on executor e."
  (scheduler/periodically e check
                          :initial-delay immediately
                          :delay fifteen-second))

(defn stop [e]
  "REPL helper. Stop executor e"
  (scheduler/shutdown-executor e))

(defn -main []
  (start (scheduler/scheduled-executor 1)))

