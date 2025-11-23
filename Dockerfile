# Multi-stage Dockerfile: build with Maven, run on a slim JRE image
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy only Maven files first to leverage Docker layer caching
COPY pom.xml mvnw* ./
COPY .mvn .mvn
COPY src ./src

# Build the application (skip tests to speed up CI builds; change if needed)
RUN mvn -B -DskipTests package

# Find the built artifact (jar or war) and copy it to a known location
RUN mkdir -p /workspace/out && \
    ARTIFACT=$(ls -1 target/* | egrep -i '\\.jar$|\\.war$' | head -n1) && \
    echo "Found artifact: $ARTIFACT" && \
    cp "$ARTIFACT" /workspace/out/app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the application artifact from the builder stage
COPY --from=builder /workspace/out/app.jar /app/app.jar

# Port exposed by Spring Boot default; Render provides PORT env var at runtime
EXPOSE 8080

# Allow runtime JVM options with the JAVA_OPTS environment variable
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
