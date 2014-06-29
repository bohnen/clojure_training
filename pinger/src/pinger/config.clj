(ns pinger.config
  (:use [clojure.java.io :only (reader resource)])
  (:require [clojure.string :as str])
  (:import (java.util Properties)))

(defn load-properties [src]
  (with-open [rdr (reader src)]
    (doto (Properties.)
      (.load rdr))))

(defn config
  []
  (if-let [src (resource "pinger.properties")]
    (load-properties src)))

(defn urls [^Properties conf]
  (if conf
    (str/split (.get conf "urls") #",")))
