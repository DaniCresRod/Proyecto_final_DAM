FROM maven:3.8.7-eclipse-temurin-19

ENV DATABASE_HOST=mysql_container
ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=secret
ENV DATABASE_NAME=db_myschedule
ENV DATABASE_PORT=3306

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /


# Copia el archivo JAR de la aplicación
COPY "target/mySchedule-0.0.1-SNAPSHOT.jar" mySchedule.jar

# Expone el puerto en el que la aplicación Spring Boot se ejecutará dentro del contenedor
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "mySchedule.jar"]
