# Wersja uruchomieniowa (lightweight, bez Mavena)
FROM eclipse-temurin:17-jdk-alpine

# Tworzymy tymczasowy katalog
VOLUME /tmp

EXPOSE 8080

COPY target/health-reservation-system-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

# Komenda startowa kontenera
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar



