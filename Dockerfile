# Indica la imagen base que se utilizará
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos necesarios al directorio de trabajo del contenedor
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

# Expone el puerto en el que se ejecuta tu aplicación
EXPOSE 8086

# Define el comando para ejecutar tu aplicación
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
