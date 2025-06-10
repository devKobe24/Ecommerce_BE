# 🛠️ Build stage
FROM gradle:8.5-jdk21 AS build
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2"
WORKDIR /app/discovery-service
COPY . .
RUN gradle clean bootJar -x test --no-daemon

# 🚀 Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/discovery-service/build/libs/app.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]

