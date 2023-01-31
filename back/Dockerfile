FROM openjdk:17-alpine

COPY ./build/libs/*.jar ./app.jar

EXPOSE 8845

ENTRYPOINT [ "java", "-jar", "app.jar" ]