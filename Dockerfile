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

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación sin especificar el perfil de Spring
ENTRYPOINT ["java", "-jar", "app.jar"]
