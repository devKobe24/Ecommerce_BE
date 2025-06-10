# 🛠️ Build stage
FROM gradle:8.5-jdk21 AS build
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2"
WORKDIR /app
COPY . .
WORKDIR /app/product-service
RUN gradle clean bootJar -x test --no-daemon

# 🚀 Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# ✅ fix: build stage에서 jar 복사
COPY --from=build /app/product-service/build/libs/app.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]