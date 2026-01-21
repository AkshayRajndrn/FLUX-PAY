# --- STAGE 1: Build (The "Heavy" part) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Copy ONLY the pom.xml first.
# This is the secret to speed! Docker caches the dependencies.
COPY pom.xml .

# 2. Download all dependencies (This takes time the first time)
RUN mvn dependency:go-offline -B

# 3. Copy the source code (This happens only after dependencies are safe)
COPY src ./src

# 4. Compile and package the JAR file
RUN mvn clean package -DskipTests

# --- STAGE 2: Run (The "Light" part) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 5. Copy the finished JAR from the build stage
# Note: Ensure your pom.xml generates a jar, usually in the /target folder
COPY --from=build /app/target/*.jar app.jar

# 6. Security: Don't run as root (Bank Standard)
RUN addgroup -S nexus && adduser -S nexus -G nexus
USER nexus

EXPOSE 8080

# 7. Performance: Optimize JVM for Docker memory limits
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]