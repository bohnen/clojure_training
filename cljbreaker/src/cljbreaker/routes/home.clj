(ns cljbreaker.routes.home
  (:use [hiccup.form])
  (:require [compojure.core :refer :all]
            [cljbreaker.views.layout :as layout]
            [noir.session :as session]
            [cljbreaker.models.game :as game]))

(defn board []
  (form-to [:post "/guess"]
           (text-field "one")
           (text-field "two")
           (text-field "three")
           (text-field "four")
           (submit-button "Guess")))

(defn home []
  (when-not (session/get :game)
    (session/put! :game (apply str(game/create))))
  (layout/common
   [:p "Welcome to clojurebreaker. Your current game solution is " (session/get :game)]))

(defroutes home-routes
  (GET "/" [] (home)))
