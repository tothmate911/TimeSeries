# Build stage
FROM maven:3-openjdk-17 AS build
COPY . /dockerTemp
RUN mvn -f /dockerTemp/pom.xml clean install

# Run stage
FROM openjdk:17-oracle
WORKDIR /
COPY --from=build /dockerTemp/target/TimeSeries*.jar TimeSeries.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","TimeSeries.jar"]