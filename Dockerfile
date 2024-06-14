# Etapa de construção
FROM maven:3.8.5-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa de execução
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/netflix-0.0.1-SNAPSHOT.jar netflix-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "netflix-0.0.1-SNAPSHOT.jar"]
