(defproject clojure-poi "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.apache.poi/poi "3.10-FINAL"]
                 [org.apache.poi/poi-ooxml "3.10-FINAL"]]


  :main ^:skip-aot clojure-poi.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
