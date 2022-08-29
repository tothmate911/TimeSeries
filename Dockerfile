# Build stage
# FROM maven:3-openjdk-17 AS build
# COPY . /dockerTemp
# RUN mvn -f /dockerTemp/pom.xml clean install

# Run stage
FROM openjdk:17-oracle
ADD target/TimeSeries*.jar TimeSeriesApplication.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","TimeSeriesApplication.jar"]