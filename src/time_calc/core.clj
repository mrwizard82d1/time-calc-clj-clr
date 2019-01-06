(ns time-calc.core 
  (:gen-class))

(defn lines
  "Return a `seq` of lines read from a text file."
  [filename]
  (line-seq (clojure.clr.io/text-reader filename)))

(defn -main
  [& args]
  (apply println "Received args:" args)
  (println (lines (first args))))
