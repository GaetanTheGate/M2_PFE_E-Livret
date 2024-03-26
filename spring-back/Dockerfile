# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package spring-boot:repackage -DskipTests -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:hsqldb:hsql://localhost/db



# Use an official OpenJDK image as the base image
FROM openjdk:20

# Expose the port this application is listen on
EXPOSE 8081

# Set the working directory in the container
WORKDIR /app

# Copy the hsql database and it's properties
COPY hsqldb ./hsqldb
COPY database ./database
COPY server.properties .

# Show everything that has been copied
RUN ls

# Setup the database once if not done already
#RUN java -classpath hsqldb/lib/hsqldb.jar org.hsqldb.server.Server

# Set the name of the created war file
ENV WAR_FILE=M2_PFE-E_LIVRET-1.0-SNAPSHOT.war
RUN echo "WarFile : "$WAR_FILE

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/$WAR_FILE ./

# Show everything that has been copied
RUN ls

# Set the command to run the application
CMD java -classpath hsqldb/lib/hsqldb.jar org.hsqldb.server.Server & \
    java -jar -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:hsqldb:hsql://localhost/db $WAR_FILE
