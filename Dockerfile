FROM eclipse-temurin:19-jre-alpine
COPY /target/lotto.jar /lotto.jar
ENTRYPOINT ["java", "-jar", "/lotto.jar"]