FROM openjdk:11-jre
ARG JAR_FILE=build/libs/rock-paper-scissors-*.jar
COPY ${JAR_FILE} rock-paper-scissors.jar
ENTRYPOINT ["java", "-jar", "rock-paper-scissors.jar"]
