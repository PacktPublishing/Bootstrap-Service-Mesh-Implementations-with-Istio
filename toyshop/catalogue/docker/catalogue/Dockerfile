FROM golang:alpine3.15 AS build
RUN echo $PWD
COPY . /go/src/catalogue

WORKDIR /go/src/catalogue

#RUN go get -u github.com/FiloSottile/gvt
#RUN gvt restore && \
#    CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o /app github.com/microservices-demo/catalogue/cmd/cataloguesvc
RUN go mod download && go mod verify && go mod tidy
RUN CGO_ENABLED=0 go build -o /bin/catalogue ./cmd/cataloguesvc/.

FROM scratch
COPY --from=build /bin/catalogue /bin/catalogue
COPY images/ /images/
EXPOSE 8080
ENTRYPOINT ["catalogue"]
CMD ["--port=8080"]