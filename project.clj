(defproject time-calc-clj-clr "0.1.0-SNAPSHOT"
  :description "Application to summarize daily activities."
  :url "https://github.com/mrwizard82d1/time-calc-clj-clr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :warn-on-reflection true
  :min-lein-version "2.0.0"
  :plugins [[lein-clr "0.2.2"]]
  :main time-calc.core
  :aot [time-calc.core]
  :clr {:cmd-templates  {:clj-exe   [[?PATH "mono"] [CLJCLR %1]]
                         :clj-dep   [[?PATH "mono"] ["target/clr/clj/Debug 4.0" %1]]
                         :clj-url   "http://sourceforge.net/projects/clojureclr/files/clojure-clr-1.8.0-Debug-4.0.zip/download"
                         :clj-zip   "clojure-clr-1.8-Debug-4.0.zip"
                         :curl      ["curl" "--insecure" "-f" "-L" "-o" %1 %2]
                         :nuget-ver [[?PATH "mono"] [*PATH "nuget.exe"] "install" %1 "-Version" %2]
                         :nuget-any [[?PATH "mono"] [*PATH "nuget.exe"] "install" %1]
                         :unzip     ["unzip" "-d" %1 %2]
                         :wget      ["wget" "--no-check-certificate" "--no-clobber" "-O" %1 %2]}
        ;; for automatic download/unzip of ClojureCLR,
        ;; 1. make sure you have curl or wget installed and on PATH,
        ;; 2. uncomment deps in :deps-cmds, and
        ;; 3. use :clj-dep instead of :clj-exe in :main-cmd and :compile-cmd
        :deps-cmds      []; [:wget  :clj-zip :clj-url] ; edit to use :curl instead of :wget
                         ; [:unzip "../clj" :clj-zip]
                         
        :main-cmd      [:clj-exe "Clojure.Main.exe"]
        :compile-cmd   [:clj-exe "Clojure.Compile.exe"]})
