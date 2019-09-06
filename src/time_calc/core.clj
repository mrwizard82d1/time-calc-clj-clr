(ns time-calc.core
  (:require clojure.string)
  (:gen-class))

(defn words
  "Split text into words (characters separated by whitespace)."
  [text]
  (if (seq text)
    (clojure.string/split text #"\s+")
    nil))

(defn day-number
  "Extract the day of the month from day-text."
  [day-text]
  (let [candidate-day (System.Int32/Parse day-text)]
    (if (<= candidate-day 31)
      candidate-day)))

(defn month-number
  "Extract the day of the month from month-text."
  [month-text]
  (if-let [month (case month-text
                   "Jan" 1 "Feb" 2 "Mar" 3 "Apr" 4 "May" 5 "Jun" 6
                   "Jul" 7 "Aug" 8 "Sep" 9 "Oct" 10 "Nov" 11 "Dec" 12
                   nil)]
    month))

(defn year-number
  "Extract the year from the year-text."
  [year-text]
  (if (seq year-text)
    (let [candidate-year (System.Int32/Parse year-text)]
      candidate-year)
    2019))

(defn day-of-year
  "Extract the components of the day of the month from `word`."
  [word]
  (if-let [matches (re-matches #"([0-3]\d)-([A-Za-z]{3})(-(2\d{3}))?" word)]
    (if-let [day (day-number (second matches))]
      (if-let [month (month-number (nth matches 2))]
        (if-let [year (year-number (nth matches 4))]
          [year month day])))))

(defn time-of-day
  "Extract the time of day from `word`."
  [word]
  (if-let [matches (re-matches #"([0-2][0-9])([0-5][0-9])" word)]
    (let [candidate-hour (second matches)]
      (if (< candidate-hour 24)
        [candidate-hour (nth matches 2)]))))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (println (->> (slurp (first args) :encoding "ISO-8859-1"))))
