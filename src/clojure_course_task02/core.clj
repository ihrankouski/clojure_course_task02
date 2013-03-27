(ns clojure-course-task02.core
  (:gen-class)
  (:require [clojure.core.reducers :as r]))

(defn make-filesearch [suitable-file?]
  (fn search [f]
    (let [N 10
          cf (r/monoid into vector)
          rf #(r/reduce conj %1 (search %2))]
      (concat (r/fold N cf rf (vec (.listFiles f)))
              (when (suitable-file? f) [(.getName f)])))))  

(defn find-files [file-name path]
  "TODO: Implement searching for a file using his name as a regexp."
  (let [ptrn (re-pattern file-name)
        pred #(->> % .getName (re-find ptrn) nil? not)
        search (make-filesearch pred)]
    (search (clojure.java.io/file path))))

(defn usage []
  (println "Usage: $ run.sh file_name path"))

(defn -main [file-name path]
  (if (or (nil? file-name)
          (nil? path))
    (usage)
    (do
      (println "Searching for " file-name " in " path "...")
      (dorun (map println (find-files file-name path))))))
