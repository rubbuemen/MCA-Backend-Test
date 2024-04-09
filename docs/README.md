# Backend Access Test
For some time now, MCA has maintained a website where we list and sell video games. These were organized alphabetically, but for a while, we have seen that it would be interesting for us to reorganize them by series. Therefore, we have met with the front-end team and have decided to carry out the following operation defined in the [contract](../src/main/resources/static/videoGames.yaml).

For this reason, the following [operation in the REST API](../src/main/resources/gameSagaApi.yaml) was created, in which you can see the IDs of the video games from a series.

We also have a database that contains the information of each of the video games, which follows the following [schema](../src/main/resources/schema.sql).

Finally, to process data and stay updated due to possible sales, we have provided a real-time messaging system in which there is [a queue](../src/main/resources/application.properties) where stock changes are notified as of today. For this, [here](../src/main/resources/schema.sql) is the current day and the name of the queue.

**Ultimately, a Spring Boot application is required that contains the following points:**

- Process stock changes.
- Expose the service API for consumption by front-ends.
- Enable the extraction of video game information from the database.

## Before you start...
You should use the following command on the [root folder](../docker-compose.yml):

`docker-compose up -d`

## The game begins! 🎮