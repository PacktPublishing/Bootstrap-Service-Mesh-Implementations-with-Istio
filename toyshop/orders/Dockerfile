FROM openjdk:8-jdk-alpine AS build
COPY . /usr/src/orders
WORKDIR /usr/src/orders
RUN apk add maven
RUN mvn -DskipTests package

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/orders/target/orders.jar /root/
EXPOSE 7001
ENTRYPOINT [ "java" ]
CMD ["-jar","/root/orders.jar","--port=7001"] 
