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

(deftest time-of-day
  (testing "Given happy path values, return correct TimeSpan"
    (are [to-test expected]
      (= (time-calc.core/time-of-day to-test) expected)
      "1727" (TimeSpan. 17 27 0)
      "0343" (TimeSpan. 3 43 0)
      "0105" (TimeSpan. 1 5 0)
      "2400" (TimeSpan/FromDays 1)))
  (testing "Given invalid values, return nil"
    (are [to-test]
      (nil? (time-calc.core/time-of-day to-test))
      ""
      "343"
      "15"
      "7")))


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

(deftest date
  (testing "Given happy path value, returns correct date"
    (is ((complement nil?) (time-calc.core/date "# 06-Jun"))))
  (testing "Given invalid values, returns nil"
    (are [to-test]
      (nil? (time-calc.core/date to-test))
      "#01-Jan" ; No separator between date indicator and day of month
      "08-Aug"))) ; No date indicator

(defn- current-year []
  "Return the year of today"
  (.Year (DateTime/Now)))

(deftest day
  (testing "Given a sequence of lines representing a day's activities, return the day (map)"
    (are [to-test expected]
      (= (time-calc.core/day to-test) expected)
      [["# 23-Jan"] ["1259 intestinus"]] {:date (DateTime. (current-year) 1 23)
                                          :activities ["1259 intestinus"]}
      [["# 01-Jun"] ["0141 deportas" "1232 condigna"]] {:date (DateTime. (current-year) 6 1)
                                                        :activities ["0141 deportas" "1232 condigna"]}))
  (testing "Given an invalid day sequence, return nil"
    (are [to-test]
      (nil? (time-calc.core/day to-test))
      []
      [nil ["2306 frater"]]
      [["# 02-Jan"]]
      [["0451 caelum"]])))

(defn- date-time [^DateTime date time-span]
  (.Add date time-span))

(deftest activity
  (let [date (DateTime. 2015 8 8)]
    (testing "Given a date and valid activity text, return the correct activity"
      (are [to-test expected]
        (= (time-calc.core/activity date to-test) expected)
        "0720 email" {:start (date-time date (TimeSpan. 7 20 0))
                      :details "email"}
        "0800 plan day" {:start (date-time date (TimeSpan. 8 0 0))
                         :details "plan day"}
        "0855 work very hard" {:start (date-time date (TimeSpan. 8 55 0))
                               :details "work very hard"}
        "2400 out" {:start (date-time date (TimeSpan/FromDays 1))
                    :details "out"}))
    (testing "Given a date and invalid activity text, return nil"
      (are [to-test]
        (nil? (time-calc.core/activity date to-test))
        "0720"
        "720 email")))
  (testing "Given an invalid date, return nil"
    (is (nil? (time-calc.core/activity nil "0720 email")))))
