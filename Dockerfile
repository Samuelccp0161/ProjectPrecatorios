#FROM maven:3.8-adoptopenjdk-11 as maven
#WORKDIR /app
#COPY . .
#RUN ["mvn", "clean", "package"]
#
#FROM adoptopenjdk:11 as runStage
#COPY --from=maven /app/backend/target/backend-1.0-SNAPSHOT.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM adoptopenjdk:11
COPY  backend/target/backend-1.0-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]