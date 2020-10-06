FROM openjdk:12-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/budget-tracker-*.jar
ADD ${JAR_FILE} app.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
ENTRYPOINT ["java", "-jar", "/app.jar"]