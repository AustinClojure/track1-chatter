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

(defn message-view
 "This generates the HTML for displaying a single message"
 [message]
 [:div.panel.panel-default
   [:div.panel-heading (hiccup/h (:name message))]
   [:div.panel-body (hiccup/h (:message message))]])

(defn form-view
 "This generates the HTML for new messages"
  []
  [:div.panel.panel-default
   [:div.panel-body
    [:form.form-horizontal {:action "/" :method "POST"}
     [:div.form-group
      [:label.col-sm-2  "Name:"]
      [:div.col-sm-10
       [:input.form-control {:type "text" :name "name"}]]]
     [:div.form-group
      [:label.col-sm-2 "Message:"]
      [:div.col-sm-10
       [:input.form-control {:type "text" :name "message"}]]]
     [:input.btn.btn-primary {:type "submit" :value "Save"}]]]])

(defn index-view
  "This generates the HTML for the index page"
  [messages]
  (page/html5
   [:head
    [:title "chatter"]
    (page/include-css "/css/bootstrap.min.css")]
   [:body
    [:div.container
     [:h1 "Our Chat App"]
     [:div.row (form-view)]
     [:div.row (map message-view messages)]]]))

(defn save-new-message
  "Add the message as map into our vector of messages in the atom"
  [chat-messages name message]
  (save-message! chat-messages (make-message name message))
  (response/redirect "/"))

(defroutes app-routes
  (GET "/" []
       (index-view @chat-messages))
  (POST "/" [name message]
        (save-new-message chat-messages name message))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app (params/wrap-params app-routes))
