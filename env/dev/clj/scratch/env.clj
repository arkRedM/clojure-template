(ns scratch.env
  (:require
   [taoensso.timbre :as log]
   [ring.middleware.reload :as reload]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[scratch started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[scratch has shut down successfully]=-"))
   :middleware reload/wrap-reload})
