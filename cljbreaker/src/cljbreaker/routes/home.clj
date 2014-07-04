(ns cljbreaker.routes.home
  (:require [compojure.core :refer :all]
            [cljbreaker.views.layout :as layout]
            [noir.session :as session]
            [cljbreaker.models.game :as game]))

(defn home []
  (when-not (session/get :game)
    (session/put! :game (apply str(game/create))))
  (layout/common
   [:p "Welcome to clojurebreaker. Your current game solution is " (session/get :game)]))

(defroutes home-routes
  (GET "/" [] (home)))
