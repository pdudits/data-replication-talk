# Data Replication troubles - example

This is example code for the talk _The Trouble with Data Replication_.
Core objective is to provide example of data sharing via HTTP Pull replication.

SNAPSHOT WARNING: This project requires Payara 5.192.
At time of writing this release is few days prior to release.
Once it's released, it is possible to use `payara.version` 5.192, and `payara-micro-plugin` 1.0.4.

## Modules

`producer-app` is user management domain, that produces information about users and their subscriptions.
Important is the replication endpoint `/producer-app/replication?id=&size=` that returns up to `size` events with id greater than `id`.

`consumer-app` periodically polls this endpoint and constructs a replicated copy of user data.

`pleasync` is a draft API for possible library for common concerns around publishing and consuming these kind of pull APIs.

`demo` is Vue=based UI that demonstrates the two application modules.

## Building and running

The modules are Microprofile + JPA applications run with Payara Micro.
Hopefully there are no SNAPSHOT dependencies.
The demo is done shortly before Payara 5.192 release and depends on release, so you might need to upgrade that dependency.

`mvn clean install` build both modules, ui and runs all tests against Payara Micro.

Individual modules can be run `mvn payara-micro:start`.
Demo will run all of the modules in single Payara Micro instance.

# Running UI via node tooling

Since demo is vue-cli project (in `src/main/node`) it can be also run with vue tooling.
To deploy both backend applications to payara micro run `mvn -pl :demo -Dbackend`.
To start ui dev run `vue serve` or `yarn run vue-cli-service serve` in `demo/src/main/node`.