# Data Replication troubles - example

This is example code for the talk _The Trouble with Data Replication_.
Core objective is to provide example of data sharing via HTTP Pull replication.


## Modules

`producer-app` is user management domain, that produces information about users and their subscriptions.
Important is the replication endpoint `/producer-app/replication?id=&size=` that returns up to `size` events with id greater than `id`.

`consumer-app` periodically polls this endpoint and constructs a replicated copy of user data.

`pleasync` is a draft API for possible library for common concerns around publishing and consuming these kind of pull APIs.

`ui` will hopefully be a simple UI for demoing the functionality.

## Building and running

The modules are Microprofile + JPA applications run with Payara Micro.
Hopefully there are no SNAPSHOT dependencies.
The demo is done shortly before Payara 5.192 release and depends on release, so you might need to upgrade that dependency.

`mvn clean install` build both modules and runs all tests against Payara Micro.

Individual modules can be run `mvn payara-micro:start`