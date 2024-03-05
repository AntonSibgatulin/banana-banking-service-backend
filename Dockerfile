FROM openjdk:17
ARG JAR_FILE=./build/libs/*.jar
EXPOSE 8080
COPY ./build/libs/banking-service-0.0.1-SNAPSHOT.jar /home/run/app.jar
ENTRYPOINT ["java","-jar","/home/run/app.jar"]