
FROM maven:3.8.3-openjdk-17 AS build


WORKDIR /Monitoring


COPY pom.xml .


RUN mvn dependency:go-offline -B


COPY . .


RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim


WORKDIR /app


COPY --from=build /Monitoring/target/*.jar /app/app.jar


EXPOSE 8082


ENTRYPOINT ["java", "-jar","/app/app.jar"]