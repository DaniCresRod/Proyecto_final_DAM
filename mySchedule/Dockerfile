# Usa la imagen de OpenJDK como base
# FROM openjdk:19-jdk-slim
# FROM eclipse-temurin:19-jdk-alpine
FROM maven:3.8.7-eclipse-temurin-19
# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR de la aplicación al contenedor
COPY BackEnd/target/mySchedule.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]
