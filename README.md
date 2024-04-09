## Instructions
As requested, a Spring Boot application has been created that meets the following requirements:
- **Process stock changes:**

  A [listener](src/main/java/com/mca/infrastructure/event/KafkaMessageListener.java) captures stock updates from the [file](src/main/resources/events.csv) that stores events. An asynchronous schedule was configured to send the file contents for consumption if there are any changes (this is a simulation, so there will be none after first one).
- **Expose the service API for consumption by front-ends:**

  A Rest API with Swagger is available (see below for access instructions) to consume video games with the established settings. This search is cached, but the cache will be reset if there is a change in the stock of the process described above.
- **Enable the extraction of video game information from the database:**

  Using an in-memory database implementation with predefined data, the necessary information is extracted and updated based on the two points described above.

### Starting the service:
Assuming you have Docker installed on your computer, simply run the following command from the project root folder:

`docker-compose up -d`

This will start the pre-defined containers for the exercise and an additional one to mount the project (mca-backend).
Make sure that the ports are not occupied. Once started, the Kafka listener will be listening for possible stock updates.

### Swagger access:
To access the API, simply enter the following [endpoint](http://localhost:8081/swagger-ui/index.html).
You can search videogames related to an ID that you enter. In case of non-existence or other errors, it will be detailed.


NOTE: only if you want to run the application locally (running via IDE for example):
you will need to change in the docker-compose.yml a configuration that you need to uncomment and comment out the other one: KAFKA_CFG_ADVERTISED_LISTENERS. After that, you can run the `docker-compose up -d`. After that, start the application and go to the [endpoint](http://localhost:8080/swagger-ui/index.html) (note the port is different).
