ARG MVN_VERSION=3.6.3
ARG JDK_VERSION=17

FROM maven:${MVN_VERSION}-jdk-${JDK_VERSION}-slim as build

WORKDIR /build
COPY pom.xml .
# creates layer with maven dependencies
# first build will be significantly slower than subsequent
RUN mvn dependency:go-offline

COPY ./pom.xml /tmp/
COPY ./src /tmp/src/
WORKDIR /tmp/
# build the project
RUN mvn clean package

# extract JAR Layers
WORKDIR /tmp/target
RUN java -Djarmode=layertools -jar *.jar extract

# runtime image
FROM gcr.io/distroless/java:${JDK_VERSION} as runtime

USER nonroot:nonroot
WORKDIR /application

# copy layers from build image to runtime image as nonroot user
COPY --from=build --chown=nonroot:nonroot /tmp/target/dependencies/ ./
COPY --from=build --chown=nonroot:nonroot /tmp/target/snapshot-dependencies/ ./
COPY --from=build --chown=nonroot:nonroot /tmp/target/spring-boot-loader/ ./
COPY --from=build --chown=nonroot:nonroot /tmp/target/application/ ./

EXPOSE 8080

ENV _JAVA_OPTIONS "-XX:MinRAMPercentage=60.0 -XX:MaxRAMPercentage=90.0 \
-Djava.security.egd=file:/dev/./urandom \
-Djava.awt.headless=true -Dfile.encoding=UTF-8 \
-Dspring.output.ansi.enabled=ALWAYS \
-Dspring.profiles.active=default"

# set entrypoint to layered Spring Boot application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]