# Use uma imagem base do Maven com OpenJDK 17 para construir a aplicação
FROM maven:3.8.5-openjdk-17 as builder

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie todos os arquivos do projeto para o diretório de trabalho no contêiner
COPY . .

# Resolva as dependências do projeto
RUN mvn dependency:resolve

# Compile e empacote a aplicação, pulando os testes
RUN mvn clean package -DskipTests

# Use uma imagem base do Amazon Corretto 17 para rodar a aplicação
FROM amazoncorretto:17

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR gerado pelo estágio de construção para o diretório de trabalho
COPY --from=builder /app/target/*.jar ./application.jar

# Exponha a porta que a aplicação irá rodar
EXPOSE 8080

# Defina a variável de ambiente POSTGRES_HOST
ENV POSTGRES_HOST=localhost

# Comando para executar a aplicação com o perfil de produção ativo
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "application.jar"]