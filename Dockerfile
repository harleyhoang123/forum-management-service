FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ARG JAR_FILE=target/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} forum-management-service.jar
ENTRYPOINT ["java","-jar","forum-management-service.jar"]
