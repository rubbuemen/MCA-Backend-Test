# MCA Backend Test

## Requeriments

For local run:
- Java 21
- Maven 3.9.6

For dockerization:
- Docker Desktop

## Instructions

Run provided containers using next command:

```sh
docker-compose up -d
```

Run app container using next command:

```sh
docker-compose -f app-compose.yml up -d
```

This includes the dockerized application. If you want manage only this one:

Run the next command to start it:

```sh
docker start mca-backend
```

Run the next command to stop it:

```sh
docker stop mca-backend
```

## Swagger access

To access the API, follow the next [endpoint](http://localhost:8080/swagger-ui/index.html).

## Description
- **Process stock changes:**

  A [listener](src/main/java/com/mca/infrastructure/event/KafkaMessageListener.java) captures stock updates from the [file](src/main/resources/events.csv) that stores events. In case that an event comes with update-time that is not later than the date established as current in the properties provided and there already exists in the database such stock with an update time prior to the one that comes at the moment, it will be updated; otherwise nothing will be done with the event received.


- **Expose the service API for consumption by front-ends:**

  A Rest API with Swagger is available (see below for access instructions) to consume video games with the established settings. This search is cached, but the cache will be reset if there is a change in the stock of the process described above.


- **Enable the extraction of video game information from the database:**

  Using an in-memory database implementation with predefined data, the necessary information is extracted and updated based on the two points described above.
