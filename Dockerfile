FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY target/bankapps.jar /app/bankapps.jar
COPY wait-for.sh /app/wait-for.sh

RUN chmod +x /app/wait-for.sh \
    && apk add --no-cache curl netcat-openbsd

EXPOSE 8081

ENTRYPOINT ["/app/wait-for.sh", "keycloak", "8080", "--", "java", "-jar", "/app/bankapps.jar"]

