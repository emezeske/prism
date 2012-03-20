# prism

Prism is a set of tools for effectively writing platform-independent Clojure code.

It is far from being finished.  This is very much alpha software!  Use at your own risk.

## Installation

Add prism to your `project.clj` file in the `:dependencies` section:

```clj
(defproject prism-example "1.2.3"
  :dependencies [[prism "0.0.1-SNAPSHOT"]])
```

## Usage

Currently, prism only supports two platforms: the JVM, and JavaScript.  The `prism.core` namespace
provides three macros: `case-platform`, `if-jvm`, and `if-javascript`.  

Unfortunately, there are differences in the `ns` macro between the JVM and JavaScript.  Prism
hopes to support these eventually, but right now you will need to use use an outside tool
to handle them.  One such tool is
[lein-cljsbuild] (https://github.com/emezeske/lein-cljsbuild/blob/master/README.md),
which has a
[crossovers] (https://github.com/emezeske/lein-cljsbuild/blob/master/doc/CROSSOVERS.md)
feature that can be used to deal with the `ns` macro.

The following example assumes that you are using it as part of a lein-cljsbuild crossover:

```clj
(ns example.hello
  (:require;*CLJSBUILD-REMOVE*;-macros
    [prism.core :as prism]))

(defn log-message [message]
  (prism/if-javascript
    (.log js/console message)
    (println message)))

(log-message "Hello!")
; => Will use println when running on the JVM, and console.log on JavaScript.
```

##  License

Source Copyright © Evan Mezeske, 2012.
Released under the Eclipse Public License - v 1.0.
See the file COPYING.

## Contributors

* Evan Mezeske **(Author)** (evan@mezeske.com)
