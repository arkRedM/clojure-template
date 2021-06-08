(ns scratch.core
  (:require
    [scratch.config :refer [env]]
    [scratch.nrepl :as nrepl]
    [scratch.routes.core :as sync]
    [clojure.tools.cli :refer [parse-opts]]
    [mount.core :as mount]
    [org.httpkit.server :as http-kit]
    [taoensso.timbre :as log])
  (:gen-class))


;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (log/error {:what :uncaught-exception
                  :exception ex
                  :where (str "Uncaught exception on" (.getName thread))}))))


(def cli-options
  [["-p" "--http-port HTTP_PORT" "HTTP Port number"
    :default 3000
    :parse-fn #(Integer/parseInt %)]
   ["-n" "--nrepl-port NREPL_PORT" "nREPL Port number"
    :default 7000
    :parse-fn #(Integer/parseInt %)]])


(mount/defstate ^{:on-reload :noop} http-server
  :start
  (http-kit/run-server
    (var sync/main-ring-handler)
    {:port (get-in env [:options :http-port])})
  :stop
  (http-server))



(mount/defstate ^{:on-reload :noop} repl-server
  :start
  (when-let [nrepl-port (get-in env [:options :nrepl-port])]
    (nrepl/start {:port nrepl-port}))
  :stop
  (when repl-server
    (nrepl/stop repl-server)))


(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))


(defn start-app [args]
  (let [parsed-args (parse-opts args cli-options)]
    (log/info :parsed-args (pr-str parsed-args))
    (doseq [component (-> parsed-args
                          mount/start-with-args
                          :started)]
      (log/info component "started")))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))


(defn -main [& args]
  (start-app args))

