# 1. Start met een lichte OpenJDK base image
FROM openjdk:17-jdk-slim

# 2. Installeer ffmpeg en andere benodigde tools
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# 3. Zet werkdirectory
WORKDIR /app

# 4. Kopieer het jar-bestand (pas pad aan als nodig)
COPY target/lesson-platform.jar app.jar

# 5. Exposeer poort (optioneel, Cloud Run zet dit automatisch)
EXPOSE 8080

# 6. Start de app
ENTRYPOINT ["java","-jar","/app/app.jar"]
