# Use the official Maven image with OpenJDK
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /UserManagement

# Copy the pom.xml file and download the dependencies (better caching)
COPY pom.xml .

# Download all dependencies
RUN mvn dependency:go-offline -B

# Copy the entire project
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Use the OpenJDK runtime for running the packaged application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /UserManagement/target/*.jar /app/app.jar

# Expose the port on which the app runs (typically 8080 for Spring Boot)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar","/app/app.jar"]