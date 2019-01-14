(ns time-calc.core
  (:require clojure.string)
  (:gen-class))

(defn words
  "Split text into words (characters separated by whitespace)."
  [text]
  (if (seq text)
    (clojure.string/split text #"\s+")
    nil))

(defn day-of-month
  "Extract the components of the day of the month from `word`."
  [word]
  (if-let [matches (re-matches #"([012]\d)-([A-Za-z]{3})" word)]
    (let [candidate-month (clojure.string/capitalize (nth matches 2))]
      (if-let [month (case candidate-month
                       "Jan" 1 "Feb" 2 "Mar" 3 "Apr" 4 "May" 5 "Jun" 6
                       "Jul" 7 "Aug" 8 "Sep" 9 "Oct" 10 "Nov" 11 "Dec" 12
                       nil)]
        [month (int (second matches))]))))

(defn time-of-day
  "Extract the time of day from `word`."
  [word]
  (if-let [matches (re-matches #"([0-2][0-9])([0-5][0-9])" word)]
    [(second matches) (nth matches 2)]))

(defn token
  "Tokenize a sequences of 'words.'"
  [word]
  (cond
    (= word "#") [:start-day word]
    (day-of-month word) [:day-of-month (day-of-month word)]
    (time-of-day word) [:time (time-of-day word)]
    :else [:word word]))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (println (->> (slurp (first args) :encoding "ISO-8859-1")
                words
                (map token))))
