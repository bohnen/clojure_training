(defproject
  web
  "0.1.0-SNAPSHOT"
  :repl-options
  {:init-ns web.repl  ;; おおっ、これで初期のnsを指定できるのか
   :nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]} ;; nrepl に必要
  :dependencies
  [[ring-server "0.3.1"]
   [noir-exception "0.2.2"]
   [com.h2database/h2 "1.4.178"]
   [environ "0.5.0"]
   [com.taoensso/timbre "3.2.1"]
   [markdown-clj "0.9.44"]
   [org.clojure/clojure "1.6.0"]
   [com.taoensso/tower "2.0.2"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [korma "0.3.2"]
   [selmer "0.6.8"]
   [lib-noir "0.8.4"]
   [lein-light-nrepl "0.0.18"]] ;; nrepl に必要
  :ring
  {:handler web.handler/app,
   :init web.handler/init,
   :destroy web.handler/destroy}
  :profiles
  {:uberjar {:aot :all},
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies
    [[ring-mock "0.1.5"]
     [ring/ring-devel "1.3.0"]
     [pjstadig/humane-test-output "0.6.0"]],
    :injections
    [(require 'pjstadig.humane-test-output)
     (pjstadig.humane-test-output/activate!)],
    :env {:dev true}}}
  :url
  "http://example.com/FIXME"
  :plugins
  ;; ここがあるおかげで lein ringが使える
  [[lein-ring "0.8.10"] [lein-environ "0.5.0"]]
  :description
  "FIXME: write description"
  :min-lein-version "2.0.0")
