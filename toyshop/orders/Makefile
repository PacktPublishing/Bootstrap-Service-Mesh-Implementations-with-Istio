SVCNAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/orders-svc
TAG = 0.0.1

compile:
	mvn -DskipTests package
	mvn install -DskipTests

buildDockerImages:
	docker build -t $(SVCNAME):$(TAG) .

dockerTest : 
	docker run -d --rm --name orders-db mongo:latest

	docker run -d --rm --link orders-db --name orders-svc -p 7001:7001 $(SVCNAME):$(TAG) 
	
cleanup :
	docker rm -f orders-svc 
	docker rm -f orders-db 	