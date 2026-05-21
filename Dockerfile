FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

ADD target/SpringDataRest1-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]