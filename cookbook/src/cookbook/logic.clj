(ns cookbook.logic
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))

(run* [q]
      (membero q [3 4 5])
      (membero q [3 4 1])
      (membero q [7 4 3])
      )

(run* [q]
      ;; freshは新しいlogical var (lvar) を導入する
      ;; 下の例は、〜であるような a と考えれば良い
      (fresh [a]
             (membero a [1 2 3])
             (membero q [3 4 5])
             (== a q)))

(run* [q]
      (== q 1))

(run* [q]
      (== {:a q :b 2} {:a 1 :b 2}))
