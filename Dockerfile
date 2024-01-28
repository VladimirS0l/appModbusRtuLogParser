FROM openjdk:17
WORKDIR /opt
RUN mkdir -p ./uploads
COPY target/*.jar parser.jar
CMD ["java", "-jar", "parser.jar"]