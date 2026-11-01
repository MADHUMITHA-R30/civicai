# Multi-stage Dockerfile for CivicAI Spring Boot Backend (Unified Full-Stack)
# Stage 1: Build JAR using Maven and OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code (includes pre-built Angular static assets in src/main/resources/static)
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal JRE Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create upload directory for persistent storage
RUN mkdir -p /app/uploads

# Copy built jar from builder
COPY --from=builder /workspace/target/civicai-backend-*.jar app.jar

# Cloud environment variable defaults
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV UPLOAD_DIR=/app/uploads

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
