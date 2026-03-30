FROM eclipse-temurin:21-jdk as builder
LABEL authors="Koder95"
WORKDIR book-shop-online
COPY src/ ./src
COPY mvnw ./
COPY .mvn/ ./.mvn/
COPY pom.xml ./
COPY checkstyle.xml ./
RUN ./mvnw clean package -DskipTests
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bso.jar
RUN java -Djarmode=layertools -jar bso.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR book-shop-online
COPY --from=builder book-shop-online/dependencies/ ./
COPY --from=builder book-shop-online/spring-boot-loader/ ./
COPY --from=builder book-shop-online/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8888
EXPOSE 54342
