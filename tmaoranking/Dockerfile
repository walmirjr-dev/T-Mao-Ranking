# ==========================================
# ESTÁGIO 1: Compilação (Build)
# ==========================================
# Baixa uma imagem oficial do Maven com Java 21 para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o arquivo de dependências primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia a pasta do código-fonte e compila gerando o arquivo .jar (pula os testes para ir mais rápido)
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# ESTÁGIO 2: Execução (Runtime)
# ==========================================
# Baixa uma imagem super leve apenas com o Java 21 rodando (sem o Maven pesado)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o arquivo .jar que foi gerado lá no Estágio 1 para dentro dessa imagem leve
COPY --from=build /app/target/*.jar app.jar

# Informa a porta que a aplicação vai escutar
EXPOSE 8080

# Comando definitivo que liga o Spring Boot dentro do Render
ENTRYPOINT ["java", "-jar", "app.jar"]