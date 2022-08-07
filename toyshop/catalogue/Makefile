NAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/catalogue
DBNAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/catalogue-db


TAG=0.0.1

INSTANCE = catalogue

buildDockerImages:
	 docker build -t $(NAME):$(TAG) -f docker/catalogue/Dockerfile .
	 docker build -t $(DBNAME):$(TAG) -f docker/catalogue-db/Dockerfile docker/catalogue-db/

dockertest: buildDockerImages
	docker run -d --name catalogue-db $(DBNAME):$(TAG)
	sleep 10
	docker run -d --name $(INSTANCE)-dev -p 8084:8080 --link catalogue-db $(NAME):$(TAG)
	scripts/testcontainer.sh
	$(MAKE) cleanup
cleanup :
	docker rm -f catalogue-db
	docker rm -f $(INSTANCE)-dev
 
