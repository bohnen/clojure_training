(ns pinger.core
  (:import (java.net URL HttpURLConnection))
  (:require [pinger.scheduler :as scheduler]
            [clojure.tools.logging :as logger]
            [pinger.config :as config])
  (:gen-class))

(defn response-code [address]
  (let [conn ^HttpURLConnection (.openConnection (URL. address))
        code (.getResponseCode conn)]
    (when (< code 400)
      (-> conn .getInputStream .close))
    code))

(defn available? [address]
  (= 200 (response-code address)))

(defn record-availability [address]
  (if (available? address)
    (logger/info (str address " is responding normally"))
    (logger/error (str address " is not available"))))


;; (response-code "http://www.google.com")
;; (available? "http://www.google.com")
;; (available? "http://www.google.com/badurl")

(defn check []
  (println "check called!")
  ;; if exception had occured below, just die the thread and nothing out on stdout!
  (println (str (count (config/urls (config/config))) " urls in file."))
  (doseq [address (config/urls (config/config))]
    (record-availability address)))

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
  (println "--- main start ---")
  (start (scheduler/scheduled-executor 1)))

