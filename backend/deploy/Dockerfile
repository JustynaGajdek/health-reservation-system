# Wersja uruchomieniowa (lightweight, bez Mavena)
FROM eclipse-temurin:17-jdk-alpine

# Tworzymy tymczasowy katalog
VOLUME /tmp

# Definiujemy zmienną z nazwą pliku .jar
ARG JAR_FILE=target/health-reservation-system-0.0.1-SNAPSHOT.jar

# Kopiujemy gotowy plik .jar z lokalnego systemu
COPY ${JAR_FILE} app.jar

# Komenda startowa kontenera
ENTRYPOINT ["java", "-jar", "/app.jar"]
