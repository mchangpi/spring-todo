FROM openjdk:17-jdk-alpine
EXPOSE 8080
ARG name
ENV DB_PW $name
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]