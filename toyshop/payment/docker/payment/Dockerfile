FROM golang:alpine3.15 AS BUILD
COPY . /go/src/github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/payment
WORKDIR /go/src/github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/payment
RUN go mod download && go mod verify && go mod tidy && go mod vendor
RUN CGO_ENABLED=0 go build -o /bin/app ./cmd/paymentsvc/.

FROM scratch
COPY --from=build /bin/app /bin/app
ENV HATEAOS user
EXPOSE 8080
ENTRYPOINT ["app"]
CMD ["--port=8080"]