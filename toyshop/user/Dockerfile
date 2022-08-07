FROM golang:alpine3.15 
COPY . /go/src/user
WORKDIR /go/src/user
ENV GO111MODULE=on 
RUN go mod tidy 
RUN CGO_ENABLED=0 go build -o /bin/user .
ENV HATEAOS user
ENV USER_DATABASE mongodb
ENV MONGO_HOST user-db
EXPOSE 8080
ENTRYPOINT ["/bin/user"]
CMD ["--port=8080"]
