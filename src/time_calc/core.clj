(ns time-calc.core 
  (:gen-class))

(defn words
  "Split text into words (characters separated by whitespace)."
  [text]
  (if (seq text)
    (clojure.string/split text #"\s+")
    nil))

(defn token
  "Tokenize a sequences of 'words.'"
  [word]
  (cond
    (= word "#") [:start-day word]
    (re-matches #"\d{2}-[A-Z][a-z]{2}" word) [:day-of-month word]
    (re-matches #"\d{4}" word) [:hour word]
    :else [:word word]))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (println (->> (slurp (first args) :encoding "ISO-8859-1")
                words
                (map token))))
