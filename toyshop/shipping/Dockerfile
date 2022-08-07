FROM openjdk:8-jdk-alpine AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN apk add maven
RUN mvn -DskipTests package

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/app/target/shipping.jar /root/
EXPOSE 7001
ENTRYPOINT [ "java" ]
CMD ["-jar","/root/shipping.jar","--port=7001"] 