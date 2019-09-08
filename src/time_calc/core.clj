(ns time-calc.core
  (:require clojure.string clojure.pprint)
  (:gen-class))

(defmacro when-let*
  "Variant of when-let that requires multiple bindings.

  Found in the v49 clojure cheat sheet https://clojure.org/api/cheatsheet."
  ([bindings & body]
   (if (seq bindings)
     `(when-let [~(first bindings) ~(second bindings)]
        (when-let* ~(drop 2 bindings) ~@body))
     `(do ~@body))))

(defn words
  "Split text into words (characters separated by whitespace)."
  [text]
  (if (seq text)
    (clojure.string/split text #"\s+")
    nil))

(defn day-number
  "Extract the day of the month from day-text."
  [day-text]
  (let [candidate-day (int day-text)]
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
    (let [candidate-year (int year-text)]
      candidate-year)
    2019))

(defn day-of-year
  "Extract the components of the day of the month from `word`."
  [word]
  (if-let [matches (re-matches #"([0-3]\d)-([A-Za-z]{3})(-(2\d{3}))?" word)]
    (if-let [day (day-number (second matches))]
      (if-let [month (month-number (nth matches 2))]
        (if-let [year (year-number (nth matches 4))]
          (DateTime. year month day))))))

(defn time-of-day
  "Extract the time of day from `word`."
  [word]
  (when-let* [matches (re-matches #"([0-2][0-9])([0-5][0-9])" word)
              hour (int (second matches))
              minute (int (nth matches 2))]
             (let [typical? (fn [h m] (and (< h 24) (<= m 59)))
                   special? (fn [h m] (and (= h 24) (= m 0)))]
               (cond (typical? hour minute) (TimeSpan. hour minute 0)
                     (special? hour minute) (TimeSpan/FromDays 1)))))

(defn content-filled-lines [s]
  "Transform a string into a sequence of content filled lines.

  A content filled line is a line that is neither blank nor whitespace only.
  Additionally, each line has extraneous whitespace trimmed from the ends."
  (->> s
       clojure.string/split-lines
       (map clojure.string/trim)
       (filter (complement clojure.string/blank?))))

(defn date [l]
  "Determine if l is a line of text containing information about a day"
  (let [words (time-calc.core/words l)]
    (if (= "#" (first words))
        (time-calc.core/day-of-year (second words)))))

(defn activity [^DateTime date text]
  "Transform date and text to an activity"
  (if (not (nil? date))
    (when-let* [words (words text)
                time-of-day (time-of-day (first words))
                detail-words (rest words)]
               (when (seq detail-words)
                 {:start (.Add date time-of-day)
                  :description (apply str (interpose " " detail-words))}))))

(defn day [s]
  "Parses a sequence into information for a single day"
  (when-let* [date (date (ffirst s))
              activities (second s)]
             (map (partial activity date) activities)))

(defn days [lines]
  "Convert a sequence of lines into a sequence of days"
  (->> lines
       (partition-by date)
       (partition-all 2)
       (map day)))

(defn subtract-time-stamps [^DateTime subtrahend ^DateTime minuend]
  "Subtract DateTime, `minuend`, from DateTime, `subtrahend"
  (.Subtract subtrahend minuend))

(defn summarize-day [day]
  "Summarize the time spent on activities for a single day."
  day)

(defn print-summary [summary]
  "Print a single day activity summary."
  (clojure.pprint/pprint summary))

(defn file-content
  "Read the complete contents of the specified file"
  ([filename]
   (file-content filename "ISO-8859-1"))
  ([filename encoding]
   (->> (slurp filename :encoding encoding))))

(defn summarize-activities-by-day [pathname]
  "Summarize the daily activities in pathname"
  (->> pathname
       file-content
       content-filled-lines
       days
       (map summarize-day)))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (->> args
       first
       summarize-activities-by-day
       (map print-summary)
       dorun))
