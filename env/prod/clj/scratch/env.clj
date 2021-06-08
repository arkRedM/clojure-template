(ns scratch.env
  (:require [taoensso.timbre :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[scratch started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[scratch has shut down successfully]=-"))
   :middleware identity})
