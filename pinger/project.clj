(defproject pinger "0.1.0-SNAPSHOT"
  :description "Pinger: sample from clojure 2nd edition"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.logging "0.3.0"]
                 [log4j "1.2.17"]
                 [javax.mail/mail "1.4.6"]]
  :main pinger.core
  :aot :all
  :profiles
  {
   :dev {:resource-paths ["."]}})
