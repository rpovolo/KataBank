# Etapa de construcción
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copia los archivos del proyecto al contenedor
COPY . .

# Ejecuta Maven para compilar y empaquetar la aplicación, omitiendo las pruebas
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia el archivo .jar desde la etapa de construcción
COPY --from=build /app/target/kata-bank-1.0.0.jar app.jar

# Establece el perfil de Spring a "web"
ENV SPRING_PROFILES_ACTIVE=default

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación con el perfil de Spring configurado
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]
