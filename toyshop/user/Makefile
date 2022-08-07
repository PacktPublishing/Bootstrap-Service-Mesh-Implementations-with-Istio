NAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/users
DBNAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/user-db
INSTANCE = user
TESTDB = toyshoptestuserdb
TAG = 0.0.1

default: docker

test:
	@docker build -t $(INSTANCE)-test -f ./Dockerfile-test .
	@docker run --rm -it $(INSTANCE)-test /bin/sh -c 'glide novendor| xargs go test -v'

dockerdev:
	docker build -t $(INSTANCE)-dev .

dockertestdb:
	docker build -t $(TESTDB) -f docker/user-db/Dockerfile docker/user-db/
#dockerruntest: dockertestdb dockerdev
dockerruntest: dockerdev
#	docker run -d --name my$(TESTDB) -h my$(TESTDB) $(TESTDB)

	docker run -it --rm --name $(INSTANCE)-dev -p 8084:8080 --link my$(TESTDB)  -e MONGO_HOST="my$(TESTDB):27017" -e port=8080 $(INSTANCE)-dev
	

docker:
	docker build -t $(NAME) -f docker/user/Dockerfile-release .

dockerlocal:
	docker build -t $(INSTANCE)-local -f docker/user/Dockerfile-release .

dockertravisbuild: 
#TBD Replace with GitLab
	docker build -t $(NAME):$(TAG) -f docker/user/Dockerfile-release .
	docker build -t $(DBNAME):$(TAG) -f docker/user-db/Dockerfile docker/user-db/
	if [ -z "$(DOCKER_PASS)" ]; then \
		echo "This is a build triggered by an external PR. Skipping docker push."; \
	else \
		docker login -u $(DOCKER_USER) -p $(DOCKER_PASS); \
		scripts/push.sh; \
	fi

buildDockerImages: 
#TBD Replace with GitLab
	docker build -t $(NAME):$(TAG) .
	docker build -t $(DBNAME):$(TAG) -f docker/user-db/Dockerfile docker/user-db/
	

dockertest: dockerruntest
	scripts/testcontainer.sh
	$(MAKE) cleandocker

cleandocker:
	-docker rm -f my$(TESTDB)
	-docker rm -f $(INSTANCE)-dev

clean: cleandocker 
	rm -rf bin
	rm -rf docker/user/bin
	rm -rf vendor
