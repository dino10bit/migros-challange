# Case Study

This is a Java 11 project.

### Details

This microservice is a Spring Boot Application. In addition, it uses a dockerized MongoDB as database and a dockerized
Kafka as broker.

Because of the limited time and limited information in the case document some assumptions made;

* Courier location information is updated by REST API.
* Total distance is in kilometers.
* Courier location information can be unordered.
* Amount of Store and Courier are reasonable. If they increase current implementation will fail.
* No information about courier details present in the document that's why no additional table is created and also no
  CRUD operation implemented for it.
* Even test coverage is not mentioned in the document unit and integration tests are implemented using embeddedMongo.

### Usage

- To run the application first use `docker-compose up -d`. [docker-compose](docker-compose.yaml) will create all the
  necessary resources like zookeeper, kafka and mongodb as dockerized.
- Then application can be run using below commands.\
  `./mvnw clean install`
  `./mvnw spring-boot:run`
- [Postman Collection](Migros%20One.postman_collection.json) is provided to easy access to the implemented endpoints.

### Flow Chart

![alt text](https://github.com/sarpuzunkusak/migros-challange/blob/master/flow_chart.png?raw=true)