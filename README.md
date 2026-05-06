[![Clojars Project](https://img.shields.io/clojars/v/party.donut/protocol-implementation.svg)](https://clojars.org/party.donut/protocol-implementation)

# donut.protocol-implementation

This is a weird one. The idea is to allow library developers to package
protocol implementations which consumers can then instantiate to _actually_
implement the protocol as predefined in the library.

Example where an implementation is defined:

``` clojure
(ns donut.box.identity.identity-store
  (:require
   [donut.protocol-implementation :as dpi]
   [next.jdbc.sql :as jsql]))
   
(defprotocol IdentityStore
  (-user-by-email [datasource email])
  (-user-by-reset-password-token [datasource token])
  (-insert-user! [datasource user])
  (-update-user! [datasource user]))

(dpi/def-protocol-implementation
  IdentityStore
  :donut.dbeasy.next-jdbc

  (-user-by-email
   [datasource email]
   (donut.dbeasy.next-jdbc/query-one datasource {:select [:*]
                               :from   [:user]
                               :where  [:= :email email]}))

  (-user-by-reset-password-token
   [datasource token]
   (donut.dbeasy.next-jdbc/query-one datasource {:select [:*]
                               :from   [:user]
                               :where  [:= :reset-password-token token]}))

  (-insert-user!
   [datasource user]
   (donut.dbeasy.next-jdbc/insert! datasource user))

  (-update-user!
   [datasource user]
   (jsql/update! datasource :user user (select-keys user [:user/id]))))
```

`donut.box.identity.identity-store` is part of a library. Your application might
want to use that and use the provided implementation for the `IdentityStore`
protocol. It does that like this:

``` clojure
(ns your-app.vendor.identity-store
  (:require
    [donut.box.identity.identity-store :as is]
    [donut.protocol-implementation :as dpi]))

(dpi/extend-type-with-protocol-implementation
  Object
  is/IdentityStore
  :donut.dbeasy.next-jdbc)
```

Thereafter, calling `(-insert-user! {} {:email "xyz@abc.com"})` will use the
implementation defined in `donut.box.identity.identity-store`.

Is this a good idea?? Seems iffy! A better approach is probably to have some way
to define a code generator, or even just document example implementations that
people can copy/paste.
