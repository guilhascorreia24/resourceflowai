# Use uma imagem base do Java (OpenJDK)
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR construído do Spring Boot para dentro do container
COPY target/*.jar app.jar

# Exponha a porta em que a aplicação Spring Boot roda (por padrão, 8080)
EXPOSE 8080

# Comando para executar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]