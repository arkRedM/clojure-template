(ns scratch.routes.core
  (:require
    [clj-time.coerce :as time-co]
    [clj-time.core :as time-c]
    [com.rpl.specter :as s]
    [cognitect.transit :as transit]
    [compojure.core :refer (defroutes GET POST)]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.defaults :as ring-defaults]
    [ring.middleware.format :refer [wrap-restful-format]]
    [ring.util.http-response :as response]
    [taoensso.timbre :as log])
  (:import (org.joda.time ReadableInstant)))



(defroutes ring-routes
  (GET "/" ring-req (fn [req] "<h1>Hello</h1>")))


(def joda-time-writer
  (transit/write-handler
    (constantly "m")
    (fn [^ReadableInstant v]
      (.getMillis v))
    (fn [^ReadableInstant v]
      (-> v .getMillis .toString))))


(def transit-custom-handlers
  {org.joda.time.DateTime joda-time-writer})


(def main-ring-handler
  (-> ring-routes
      wrap-reload
      (wrap-restful-format
        {:formats [:json-kw :transit-json :transit-msgpack]
         :response-options
         {:transit-json {:handlers transit-custom-handlers}}})
      (ring-defaults/wrap-defaults ring-defaults/site-defaults)))

