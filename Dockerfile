FROM openjdk:12-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/budget-tracker-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]