(ns web.db.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))

(def db-store "site.db")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :make-pool? true
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".mv.db"))))

(defn create-users-table
  []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :users
      [:id "varchar(20) PRIMARY KEY"]
      [:first_name "varchar(30)"]
      [:last_name "varchar(30)"]
      [:email "varchar(30)"]
      [:admin :boolean]
      [:last_login :time]
      [:is_active :boolean]
      [:pass "varchar(100)"])))

(defn create-links-table
  []
  (sql/db-do-commands
   db-spec
   (sql/create-table-ddl
    :links
    [:id "INTEGER PRIMARY KEY AUTO_INCREAMENT"]
    [:url "varchar(512)"]
    [:timestamp :timestamp])))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-users-table)
  (create-links-table))

(defn drop-tables
  []
  (sql/db-do-commands
   db-spec
   (sql/drop-table-ddl :users)
   (sql/drop-table-ddl :links)))




