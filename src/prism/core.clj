(ns prism.core)

(defn- try-require [ns]
  (try
    (require ns)
    true
    (catch java.io.FileNotFoundException _
      false))) 

(defn- is-javascript? []
  (if (try-require 'cljs.compiler)
    (if-let [cljs-file-var (resolve 'cljs.compiler/*cljs-file*)]
      (not (nil? @cljs-file-var))
      false)
    false))

(defn- is-jvm? []
  (try
    (new java.lang.Object)
    true
    (catch java.io.FileNotFoundException _
      false)))

(def platform 
  (cond
    (is-javascript?) :javascript
    (is-jvm?) :jvm
    :else (throw (Exception. "Platform not yet supported"))))

(defmacro case-platform [& cases]
  (let [case-map (into {} (map vec (partition 2 cases)))]
    (if (contains? case-map platform)
      (platform case-map)
      (if (odd? (count cases))
        (last cases)
        (throw (java.lang.IllegalArgumentException. "No matching clause"))))))

(defmacro if-jvm
  ([then]
    (if-jvm then nil))
  ([then else]
    (case-platform :jvm then else)))

(defmacro if-javascript
  ([then]
    (if-javascript then nil))
  ([then else]
    (case-platform :javascript then else)))

(load (str "core_" (name platform)))
