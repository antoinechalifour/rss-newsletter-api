FROM openjdk:11-jdk
RUN mkdir /app
WORKDIR /app
COPY build/libs/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]