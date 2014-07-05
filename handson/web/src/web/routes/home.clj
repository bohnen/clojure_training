(ns web.routes.home
  (:use compojure.core)
  (:require [web.layout :as layout]
            [web.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/hoge" [] (about-page))
  (GET "/howto" [] (about-page)))


