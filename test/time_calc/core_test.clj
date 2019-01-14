(ns time-calc.core-test
  (:require time-calc.core)
  (:use clojure.test))

(deftest canary
        (testing "Passes a canary test."
          (is (= (+ 2 2) 4))))

(deftest words
  (testing "Verify the behavior of the words function."
    (is (empty? (time-calc.core/words [])))
    (is (empty? (time-calc.core/words "")))
    (is (= ["philitrum"] (time-calc.core/words "philitrum")))
    (is (= ["philitrum" "atque" "strepitus"]
           (time-calc.core/words (str "philitrum" " " "atque" "\f" "strepitus"))))
    (is (= ["philitrum" "atque" "strepitus"]
           (time-calc.core/words (str "philitrum" "\r\n" "atque" "\f" "strepitus"))))))

(deftest tokens
  (testing "Verify the behavior of the token function."
    (is (= [:start-day "#"] (time-calc.core/token "#")))
    (is (= [:day-of-year [2019 3 21]]  (time-calc.core/token "21-Mar")))
    (is (= [:day-of-year [2019 12 31]]  (time-calc.core/token "31-Dec")))
    (is (= [:day-of-year [2018 12 31]]  (time-calc.core/token "31-Dec-2018")))
    (is (= [:time [14 53]  (time-calc.core/token "1453")]))
    (is (= [:word "evanui"]  (time-calc.core/token "evanui")))
    (is (= [:word "41-Mar"]  (time-calc.core/token "41-Mar")))
    (is (= [:word "21-Mat"]  (time-calc.core/token "21-Mat")))
    (is (= [:word "2a-Mar"]  (time-calc.core/token "2a-Mar")))
    (is (= [:word "32-Mar"]  (time-calc.core/token "32-Mar")))
    (is (= [:word "3453"]  (time-calc.core/token "3453")))
    (is (= [:word "2453"]  (time-calc.core/token "2453")))
    (is (= [:word "1a53"]  (time-calc.core/token "1a53")))
    (is (= [:word "1463"]  (time-calc.core/token "1463")))
    (is (= [:word "145a"]  (time-calc.core/token "145a")))
    (is (= [:word "31-Dec-3018"]  (time-calc.core/token "31-Dec-3018")))))








