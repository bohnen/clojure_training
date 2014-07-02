(ns cljbreaker.routes.home
  (:require [compojure.core :refer :all]
            [cljbreaker.views.layout :as layout]
            [noir.session :as session]))

(defn home []
  (when-not (session/get :game)
    (session/put! :game (.nextInt (java.util.Random.) 1000000)))
  (layout/common
   [:p "Welcome to clojurebreaker. Your current game id is " (session/get :game)]))

(defroutes home-routes
  (GET "/" [] (home)))
