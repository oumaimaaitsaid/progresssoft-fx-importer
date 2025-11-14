
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY mvnw .

COPY .mvn/ .mvn/

COPY pom.xml .

RUN chmod +x mvnw


RUN apk add --no-cache bash


RUN ./mvnw dependency:go-offline


COPY src/ src/


RUN ./mvnw package -DskipTests -Djacoco.skip=true


EXPOSE 8080


CMD ["sh", "-c", "java -jar target/*.jar"]
