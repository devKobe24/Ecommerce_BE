# 🛠️ 1단계: 빌드 단계 (TLS 오류 방지용 ENV 추가)
FROM gradle:8.5-jdk21 AS build

# ✅ TLS 프로토콜 호환 설정 추가
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2"

WORKDIR /app

# ✅ 전체 프로젝트 복사
COPY . .

# ✅ Gradle 빌드 실행 (테스트 제외)
RUN gradle :gateway-service:clean :gateway-service:bootJar -x test --no-daemon

# 🚀 2단계: 런타임 단계
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# ✅ 빌드 결과물 복사
COPY --from=build /app/gateway-service/build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]