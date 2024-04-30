FROM maven:3.9.6 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN cd target; jar -xf *.jar

FROM openjdk:21-slim

COPY --from=builder /app/target/BOOT-INF/lib /app/lib
COPY --from=builder /app/target/META-INF /app/META-INF
COPY --from=builder /app/target/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.mca.McaApplication"]