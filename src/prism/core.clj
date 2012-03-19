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
  `(case platform ~@cases))

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
