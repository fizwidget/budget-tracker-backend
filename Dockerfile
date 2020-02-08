FROM openjdk:12-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/budgettracker-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]