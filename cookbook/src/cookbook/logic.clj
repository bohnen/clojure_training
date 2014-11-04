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

(run* [q]
      (conde
       [succeed]))

;; cond
;; この例では、(1 2)となる。
(run* [q]
      (conde
       [(== q 1)]
       [(== q 2)]))

;; cond
;; それぞれが真となるようなフォームのOR
;; この例は空になる。(qが 1かつ 2になるようなqは存在しない)
(run* [q]
      (conde
       [(== q 1) (== q 2)]))


;; logic版 cons
;; 連結となるような関係を表すゴール
(run* [q]
      (conso 1 [2 3] q))
