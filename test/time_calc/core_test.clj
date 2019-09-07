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

(deftest day-of-year
  (testing "Given happy path values, returns correct date"
    (let [current-year (.Year (DateTime/Now))]
      (are [to-test expected]
        (= (time-calc.core/day-of-year to-test) expected)
        "23-Jun" (DateTime. current-year 6 23)
        "17-Jun-2017" (DateTime. 2017 6 17))))
  (testing "Given invalid values, returns nil"
    (are [to-test]
      (nil? (time-calc.core/day-of-year to-test))
      "#01Jan" ; No dash between day and month
      "# 08-Auf" ; Unrecognized month
      "# 31-Nov"))) ; No such date

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

(deftest day-line?
  (testing "Given happy path value, returns correct date"
    (is ((complement nil?) (time-calc.core/day-line? "# 06-Jun"))))
  (testing "Given invalid values, returns nil"
    (are [to-test]
      (nil? (time-calc.core/day-line? to-test))
      "#01-Jan" ; No separator between date indicator and day of month
      "08-Aug"))) ; No date indicator

#_(deftest days
    (testing "Returns an empty sequence for an empty sequence."
      (is (empty? (time-calc.core/days []))))
    (testing "Returns an single item sequence for a single day sequence."
      (are [day-lines expected]
        (= (time-calc.core/days day-lines) expected)
        ["# 06-Jun" "0307 clamor"] [["# 06-Jun" "0307 clamor"]])))
