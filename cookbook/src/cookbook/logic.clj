(ns cookbook.logic
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))

(run* [q]
      (membero q [3 4 5])
      (membero q [3 4 1])
      (membero q [7 4 3])
      )

(run* [q]
      (fresh [a]
             (membero a [1 2 3])
             (membero q [3 4 5])
             (== a q)))

