(ns cljscript.hello
  (:require [dommy.core :as dom]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close! >! <! map< alts! timeout]])
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))

(defn http-get-chan [url]
  (let [out-ch (chan)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! out-ch res)
                      (close! out-ch))))
              "GET")
    out-ch))

(defn setup []
;;   (js/alert "hello world3"))
  (go (let [res (<! (http-get-chan "index.html"))]
        (js/alert res))))

(dom/listen! js/window :load setup)
