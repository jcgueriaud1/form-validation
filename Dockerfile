# Allows you to run this app easily as a docker container.
# See README.md for more details.
#
# 1. Build the image with: docker build --no-cache -t test/my-hilla-app:latest .
# 2. Run the image with: docker run --rm -ti -p8080:8080 test/my-hilla-app
#
# Uses Docker Multi-stage builds: https://docs.docker.com/build/building/multi-stage/

# The "Build" stage. Copies the entire project into the container, into the /app/ folder, and builds it.
FROM eclipse-temurin:21 AS BUILD
COPY . /app/
WORKDIR /app/
RUN ./mvnw verify --fail-never
RUN ./mvnw test package -Pproduction
# At this point, we have the app (executable jar file):  /app/target/my-hilla-app-1.0-SNAPSHOT.jar

# The "Run" stage. Start with a clean image, and copy over just the app itself, omitting gradle, npm and any intermediate build files.
FROM eclipse-temurin:21
COPY --from=BUILD /app/target/form-validation-example-1.0-SNAPSHOT.jar /app/
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT java -jar form-validation-example-1.0-SNAPSHOT.jar 8080