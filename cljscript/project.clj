(defproject cljscript "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.8.4"]
                 [compojure "1.1.8"]
                 [org.clojure/clojurescript "0.0-2261"]
                 [org.clojure/tools.cli "0.3.1"]
                 [hiccup "1.0.5"]
                 [prismatic/dommy "0.1.2"]
                 [http-kit "2.1.16"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]
  :main ^:skip-aot cljscript.core
  :profiles {:uberjar {:aot :all}}
  :source-paths ["src/clj"]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["src/cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "resources/public/js/main.js"  ; default: target/cljsbuild-main.js
          :optimizations :whitespace
          :pretty-print true}}]})
