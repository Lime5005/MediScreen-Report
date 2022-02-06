FROM openjdk:11
MAINTAINER Liping
COPY ./target/mediscreen-report-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]