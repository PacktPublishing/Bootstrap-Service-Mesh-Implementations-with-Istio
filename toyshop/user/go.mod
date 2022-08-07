module github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/user

go 1.18

replace github.com/openzipkin/zipkin-go-opentracing => github.com/openzipkin-contrib/zipkin-go-opentracing v0.4.5

require (
	github.com/go-kit/kit v0.12.0
	github.com/gorilla/mux v1.7.3
	github.com/microservices-demo/user v0.0.0-20210126124737-ea7bc23723af
	github.com/opentracing/opentracing-go v1.2.0
	github.com/prometheus/client_golang v1.11.0
	gopkg.in/mgo.v2 v2.0.0-20190816093944-a6b53ec6cb22
)

require gopkg.in/tomb.v2 v2.0.0-20161208151619-d5d1b5820637 // indirect
