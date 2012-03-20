(ns prism.core)

(defn- try-require
  "Try to require a namespace, returning true if it was loaded."
  [ns]
  (try
    (require ns)
    true
    (catch java.io.FileNotFoundException _
      false))) 

(defn- is-javascript?
  "Attempts to determine if this code is being run as part of the ClojureScript
   compiler, by looking at a Var that is normally only defined and non-nil when
   this is so."
  []
  (if (try-require 'cljs.compiler)
    (if-let [cljs-file-var (resolve 'cljs.compiler/*cljs-file*)]
      (not (nil? @cljs-file-var))
      false)
    false))

(defn- is-jvm?
  "Attempts to determine if this code is being run via Clojure (JVM), by attempting
   to do some Java interop, which will obviously fail on other platforms."
  [] 
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

(defmacro case-platform
  "Like case, but the expression is always set to the platform."
  [& cases]
  `(case platform ~@cases))

(defmacro if-jvm
  "If the platform is the JVM, yields then, otherwise yields else."
  ([then]
    `(if-jvm ~then nil))
  ([then else]
    (case-platform :jvm then else)))

(defmacro if-javascript
  "If the platform is Javascript, yields then, otherwise yields else."
  ([then]
    `(if-javascript ~then nil))
  ([then else]
    (case-platform :javascript then else)))
