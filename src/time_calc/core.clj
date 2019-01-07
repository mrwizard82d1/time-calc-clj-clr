(ns time-calc.core 
  (:gen-class))

(defn words
  "Split text into words (characters separated by whitespace)."
  [text]
  (if (seq text)
    (clojure.string/split text #"\s+")
    nil))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (println (->> (slurp (first args) :encoding "ISO-8859-1")
                words)))
