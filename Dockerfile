FROM amazoncorretto:21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build

FROM amazoncorretto:21
WORKDIR /appl
COPY --from=build /app/build/libs/gen-ai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/appl/app.jar"]
