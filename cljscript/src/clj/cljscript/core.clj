(ns cljscript.core
  (:use clojure.stacktrace
        [org.httpkit.server :only [run-server]]
        [compojure.core :only [routes]])
  (:require [clojure.tools.cli :refer [parse-opts]]
            [compojure.route :as route]
            [compojure.handler :as handler])
  (:gen-class))

(def default-routes
  [(route/resources "/")
   (route/not-found "Not Found")])

(defn get-routes [options]
  (->> default-routes
       (apply routes)
       handler/api)) ;; 引数の展開とか

(defn start-server [{:keys [port] :as params}]
  (let [routes (get-routes params)
        close-fn (run-server routes params)]
    (println "server stated at port: " port)
    close-fn))


(defn -main [& args]
  (let [{:keys [options arguments summary errors]}
        (parse-opts args
                    [["-p" "--port PORT" "Port number"
                      :default 8080
                      :parse-fn #(Integer/parseInt %)
                      :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
                     ["-h" "--help"]])
        {:keys [help]} options]
    (cond
     help (println summary)
     errors (println (clojure.string/join \newline errors))
     :else
     (try
       (start-server options)
       (catch Exception e (do
                            (println e)
                            (print-stack-trace e))))
     )))

;; (def cli-options
;;   ;; An option with a required argument
;;   [["-p" "--port PORT" "Port number"
;;     :default 80
;;     :parse-fn #(Integer/parseInt %)
;;     :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
;;    ;; A non-idempotent option
;;    ["-v" nil "Verbosity level"
;;     :id :verbosity
;;     :default 0
;;     :assoc-fn (fn [m k _] (update-in m [k] inc))]
;;    ;; A boolean option defaulting to nil
;;    ["-h" "--help"]])
