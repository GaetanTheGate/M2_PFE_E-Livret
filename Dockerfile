# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package spring-boot:repackage -DskipTests



# Use an official OpenJDK image as the base image
FROM openjdk:20

# Set the name of the created war file
ENV WAR_FILE=M2_PFE-E_LIVRET-1.0-SNAPSHOT.war
RUN echo "WarFile : "$WAR_FILE


# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/$WAR_FILE ./
RUN ls

# Expose the port this application is listen on
EXPOSE 8081


# Set the command to run the application
CMD java -jar $WAR_FILE