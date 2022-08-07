SVCNAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/carts-svc
TAG = 0.0.1

compile:
	mvn -DskipTests package
	mvn install -DskipTests

buildDockerImages: compile
	mvn package -Pdocker -DskipTests -f .

dockerTest : buildDockerImages
	docker run -d --rm --name carts-db mongo:latest
	sleep 10
	docker run -d --rm --link carts-db --name carts-svc -p 7001:7001 $(SVCNAME):$(TAG)
	
cleanup :
	docker rm -f carts-svc 
	docker rm -f carts-db 	



