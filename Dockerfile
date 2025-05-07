# --------------------
# 🏗️ Etapa 1: Build
# --------------------
# Usa una imagen base con el JDK de Java 21 para compilar el proyecto
FROM eclipse-temurin:21-jdk AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todo el contenido del proyecto (incluyendo mvnw y pom.xml)
COPY . .

# Ejecuta el empaquetado del proyecto, omitiendo los tests
RUN ./mvnw clean package -DskipTests

# -------------------------------
# 🚀 Etapa 2: Imagen final (runtime)
# -------------------------------
# Usa una imagen más liviana con solo el JRE (no JDK)
FROM eclipse-temurin:21-jre

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR construido desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto 8080 (el que Spring Boot usa por defecto)
EXPOSE 8080

# Comando de inicio de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

