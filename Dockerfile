# Estágio 1: Build da aplicação com Maven
FROM maven:3.8.6-openjdk-17 AS builder

WORKDIR /app

# 1. Copia os arquivos do projeto (exceto o que está no .dockerignore)
COPY pom.xml .
COPY src ./src

# 2. Baixa dependências e faz o build (cacheando dependências)
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# ---

# Estágio 2: Imagem final
FROM openjdk:17-jdk-slim

WORKDIR /app

# 3. Copia o JAR gerado
COPY --from=builder /app/target/*.jar ./app.jar

# 4. Configurações do Firebase (serão injetadas em runtime)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/watertrack
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=senha_segura

# 5. Porta exposta (a mesma do seu Tomcat)
EXPOSE 8080

# 6. Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]