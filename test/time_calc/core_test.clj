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

(deftest content-filled-lines
  (testing "Returns an empty sequence for empty string"
    (are [string-to-test]
      (empty? (time-calc.core/content-filled-lines string-to-test))
      "" " " "\t" "\n" "\n\r\n"))
  (testing "Returns a single item sequence for zero, one or many contiguous whitespace characters."
        (are [string-to-test]
          (= (count (time-calc.core/content-filled-lines string-to-test)) 1)
          "jurgo" "jurgas" "jurgat\t" "jurgamus\n" "jurgatis\n\r\n"))
  (testing "Returns the correct sequence for zero, one or many contiguous whitespace characters."
            (are [string-to-test expected]
              (= (time-calc.core/content-filled-lines string-to-test) expected)
              " mensam gluto" ["mensam gluto"]
              "mensam glutis " ["mensam glutis"]
              "mensam glutis " ["mensam glutis"]
              "mensam glutimus\n mensam glutitis\t\n mensam glutint\n" ["mensam glutimus"
                                                                        "mensam glutitis"
                                                                        "mensam glutint"])))
