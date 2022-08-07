SVCNAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/shipping
TAG = 0.0.1

compile:
	mvn -DskipTests package
	mvn install -DskipTests

buildDockerImages:
	docker build -t $(SVCNAME):$(TAG) .

