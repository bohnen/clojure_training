(ns quiz.fizzbuzz)

;; You can code in Clojure
(use '[clojure.string :only (join)])

(defn fizzbuzz [n]
  (let [fizz (zero? (mod n 3)) buzz (zero? (mod n 5))]
    (cond
      (and fizz buzz) "FizzBuzz"
      fizz "Fizz"
      buzz "Buzz"
      :else n
    )
  ))

(print (join "\n" (map fizzbuzz (range 1 101))))
