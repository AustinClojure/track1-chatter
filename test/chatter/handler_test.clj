(ns chatter.handler-test
  (:use [kerodon.core]
        [kerodon.test]
        [clojure.test])
  (:require [chatter.handler :refer [app]]))

(deftest test-app
  (testing "main route"
    (-> (session app)
        (visit "/")
		(has (status? 200) "page is found")
		(has (some-text? "Our Chat App"))
		(fill-in [:input.form-control] "Hooman")
		(fill-in [:input.form-control] "Greetings")
		(press [:input.btn.btn-primary])
		(follow-redirect)
		(has (status? 200) "message submitted successfully")
		(has (some-text? "Greetings"))))

  (testing "not-found route"
    (-> (session app)
        (visit "/invalid")
		(has (status? 404) "page not found"))))