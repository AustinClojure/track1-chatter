(ns chatter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [hiccup.page :as page]
            [ring.middleware.params :as params]
            [ring.util.response :as response]))

;; ----------------------------------------

(def chat-messages (atom nil))

(defn make-message [name message]
  {:name name :message message})

(defn save-message!
  "This will update a message list atom"
  [messages new-chat-message]
  (swap! messages conj new-chat-message))

;; ----------------------------------------

(defn message-to-row [message]
  [:tr
   [:td (hiccup/h (:name message))]
   [:td (hiccup/h (:message message))]])

(defn message-view
  "This generates the HTML for displaying messages"
  [messages]
  (page/html5
   [:head
    [:title "chatter"]]
   [:body
    [:h1 "Our Chat App"]
    [:p
     [:form {:action "/" :method "POST"}
      "Name: "     [:input {:type "text" :name "name"}]
      "Message: "  [:input {:type "text" :name "message"}]
      [:input {:type "submit"} "Submit"]]]
    [:p
     [:table
      (map message-to-row messages)]]]))

(defn post-new-message [chat-messages name message]
  (save-message! chat-messages (make-message name message))
  (response/redirect "/"))

(defroutes app-routes
  (GET "/" []
       (message-view @chat-messages))
  (POST "/" [name message]
        (post-new-message chat-messages name message))
  (route/not-found "Not Found"))

(def app (params/wrap-params app-routes))

