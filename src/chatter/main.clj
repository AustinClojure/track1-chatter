(ns chatter.main
  (:require [chatter.handler :as chatter]
            [environ.core :as env]
            [ring.adapter.jetty :as jetty]))

(defn -main [& [port]]
  (let [port (Integer. (or port (env/env :port) 5000))]
    (jetty/run-jetty #'chatter/app {:port port :join? false})))
