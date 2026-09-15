FROM ubuntu/jdk:21-24.04_stable as builder
LABEL authors="Koder95"
WORKDIR book-shop-online
COPY src/ ./src
COPY mvnw ./
COPY .mvn/ ./.mvn/
COPY pom.xml ./
COPY checkstyle.xml ./
RUN ./mvnw clean package -DskipTests
ARG JAR_FILE=target/*.jar
RUN cp ${JAR_FILE} bso.jar
RUN java -Djarmode=tools -jar bso.jar extract --layers --launcher

FROM ubuntu/jre:21-24.04_stable
WORKDIR book-shop-online
COPY --from=builder book-shop-online/bso/dependencies/ ./
COPY --from=builder book-shop-online/bso/spring-boot-loader/ ./
COPY --from=builder book-shop-online/bso/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8888
EXPOSE 54342
