FROM gradle:8.3-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:17

EXPOSE 8081

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENV POSTGRES_IP="localhost"
ENV POSTGRES_PORT="5432"

ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]