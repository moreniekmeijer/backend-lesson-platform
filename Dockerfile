# ---- Stage 1: build ----
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# ---- Stage 2: runtime ----
FROM eclipse-temurin:21-jre

# Install ffmpeg (kleine layer, belangrijk)
RUN apt-get update && apt-get install -y --no-install-recommends ffmpeg && rm -rf /var/lib/apt/lists/*

# Create app directory
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose port for Cloud Run
ENV PORT=8080
EXPOSE 8080

# Start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
