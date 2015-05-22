# Vertx 3 Web Example

This project is similar to some vertx examples. It can be started in your IDE or with `./gradlew run`.
It uses the main class `io.vertx.core.Starter` and passes in the arguments `run vertx3.restexample.RestExampleVerticle`.

## Fat Jar

The `build.gradle` uses the Gradle `shadowJar` plugin to assemble the application and all it's dependencies into a single "fat" jar.

To build the "fat jar"

    ./gradlew shadowJar

To run the fat jar:

    java -jar build/libs/gradle-verticle-3.0.0-SNAPSHOT-fat.jar

## API methods

Point your browser at `http://localhost:8080`

1. `GET /users`
2. `GET /users/:userid`
3. `PUT /users`
