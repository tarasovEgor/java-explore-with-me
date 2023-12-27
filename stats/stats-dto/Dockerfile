FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-dto.jar
ENTRYPOINT ["java","-jar","/stats-dto.jar"]