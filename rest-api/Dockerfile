FROM adoptopenjdk/openjdk11

ARG JAR_FILE=build/libs/rest-api-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar
COPY ./data.csv data.csv

ENV PROFILE=""
ENV DATABASE_URL=""
ENV DATABASE_USERNAME=""
ENV DATABASE_PASSWORD=""
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${PROFILE}","-Dspring.datasource.url=${DATABASE_URL}","-Dspring.datasource.username=${DATABASE_USERNAME}","-Dspring.datasource.password=${DATABASE_PASSWORD}","app.jar"]