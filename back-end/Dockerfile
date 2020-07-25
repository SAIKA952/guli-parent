FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/demojenkins.jar demojenkins.jar
ENTRYPOINT ["java","-jar","/demojenkins.jar", "&"]