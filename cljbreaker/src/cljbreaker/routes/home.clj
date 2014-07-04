(ns cljbreaker.routes.home
  (:use [hiccup.form])
  (:require [compojure.core :refer :all]
            [cljbreaker.views.layout :as layout]
            [noir.session :as session]
            [cljbreaker.models.game :as game]))

(defn board [{:keys [one two three four exact unordered]}]
  (list
    (when (and exact unordered)
      [:div "Exact: " exact " Unordered: " unordered])
    (form-to [:post "/guess"]
             (text-field "one" one)
             (text-field "two" two)
             (text-field "three" three)
             (text-field "four" four)
             (submit-button "Guess"))))

(defn home []
  (when-not (session/get :game)
    (session/put! :game (game/create)))
  (layout/common
    (board nil)))
;;    [:p "Welcome to clojurebreaker. Your current game solution is " (session/get :game)]))

(defn guess [one two three four])

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/guess" [one two three four]
        (let [result (game/score (session/get :game) [one two three four])]
          (if (= (:exact result) 4)
            (do (session/remove! :game)
                (layout/common
                 [:h2 "Congraturations, you have solved the puzzle!"]
                 (form-to [:get "/"]
                          (submit-button "Start A New Game"))))
            (do (println one two three four (session/get :game))
                (layout/common
                 (board {:one one
                         :two two
                         :three three
                         :four four
                         :exact (:exact result)
                         :unordered (:unordered result)})))))))
