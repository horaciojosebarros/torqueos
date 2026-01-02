# ---- build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# cache de dependências
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# build
COPY src ./src
COPY db ./db
COPY docker-compose.yml ./docker-compose.yml
COPY README.md ./README.md
RUN mvn -q -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/torqueos.jar /app/torqueos.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["java","-jar","/app/torqueos.jar"]
