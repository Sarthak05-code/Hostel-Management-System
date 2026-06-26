# Stage 1: Build the application using a pre-installed Maven image
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the application using global Maven (no wrapper needed!)
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the compiled .jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Tell Docker which port your Spring Boot app listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


#docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/Main -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=123456 my-springboot-app