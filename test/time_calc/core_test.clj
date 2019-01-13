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
    (is (= [:day-of-month "21-Mar"]  (time-calc.core/token "21-Mar")))
    (is (= [:hour "1453"]  (time-calc.core/token "1453")))
    (is (= [:word "evanui"]  (time-calc.core/token "evanui")))))








