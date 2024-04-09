FROM maven:3.9.6 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src/
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=builder /app/target/*.jar /mcabackend.jar
COPY src/main/resources/events.csv /events.csv
EXPOSE 8080
ENTRYPOINT ["java","-jar","/mcabackend.jar"]