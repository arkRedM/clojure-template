(defproject scratch "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.10.2"]
                 [org.clojure/core.async "1.3.610"]

                 ;; spec ++
                 [expound "0.8.9"]

                 ;; states/config mgmt
                 [mount "0.1.16"]
                 [cprop "0.1.6"]

                 ;; system cli
                 [org.clojure/tools.cli "0.3.3"]

                 ;; ws
                 [com.taoensso/sente "1.16.2"]

                 ;; logging
                 [com.taoensso/timbre "5.1.2"]
                 [org.clojure/tools.logging "0.4.0"]

                 ;; http basics
                 [http-kit "2.5.3"]
                 [ring "1.9.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-middleware-format "0.7.2"]
                 [metosin/ring-http-response "0.6.5"]
                 [compojure "1.6.2"]

                 ;; ds works
                 [com.rpl/specter "1.1.2"]

                 ;; time
                 [clj-time "0.15.2"]

                 ;; format
                 [com.cognitect/transit-cljs "0.8.256"]
                 [com.cognitect/transit-clj "0.8.313"]]

  :min-lein-version "2.5.0"
  :min-java-version "1.8"

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot scratch.core

  :plugins [[lein-pprint "1.3.2"]
            [lein-ancient "0.7.0"]
            [cider/cider-nrepl "0.25.9"]
            [lein-nsort "0.1.14"]]

  :nsort {:require      :asc
          :import       :desc
          :source-paths ["src"]}

  :profiles
  {:uberjar       {:omit-source    true
                   :aot            :all
                   :uberjar-name   "scratch.jar"
                   :source-paths   ["env/prod/clj"]
                   :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev   {:jvm-opts       ["-Dconf=dev-config.edn" "-Xmx1g" "-server"]
                   :source-paths   ["env/dev/clj"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user
                                    :timeout 120000}

                   :dependencies   [[vvvvalvalval/scope-capture "0.3.2"]]}
   :project/test  {:jvm-opts       ["-Dconf=test-config.edn"]
                   :resource-paths ["env/test/resources"]}
   :profiles/dev  {}
   :profiles/test {}})
