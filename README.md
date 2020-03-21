# order-service [WIP]
A event-sourcing &amp; CQRS online store, based on Akka

## How to run it
* You will need docker & docker-composed installed

* From cassandra-docker directory, start cassandra db: 
  * docker-compose up

* From root dir, build the project: 
  * gradle build
  * As the result of the build, a jar file should show up at ./target/

* Run it: 
  * java -jar ./target/order-service.jar

* Access cassandra container shell: 
    * docker exec -it cassandra bash

* Useful cqlsh commands:
    * cqlsh> DESCRIBE KEYSPACES

* References: 
    * https://doc.akka.io/docs/akka/2.5.27/typed/cluster-sharding.html
    * https://doc.akka.io/docs/akka/2.5.27/typed/persistence.html
    * https://doc.akka.io/docs/akka/current/typed/persistence.html
    * https://hub.docker.com/_/cassandra