# Use a maven image with java 25 to build the springboot app

FROM eclipse-temurin:25-jdk AS build

# set the working directory
WORKDIR /app

#copy the maven wrapper and pom.xml
COPY mvnw ./
COPY .mvn/ .mvn/

#ensure the maven wrapper is executable
RUN chmod +x mvnw

# copy the pom.xml and install dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

#use java 25 runtime image to run the application
FROM eclipse-temurin:25-jre

#set the working directory
WORKDIR /app

#copy the build jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

#export port 8080
EXPOSE 8080

#SPECIFY THE COMMAND TO run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]



