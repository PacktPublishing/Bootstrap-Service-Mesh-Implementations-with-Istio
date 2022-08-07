NAME = github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/payment
INSTANCE = payment
TAG=0.0.1

.PHONY: default copy test

default: test

copy:
	docker create --name $(INSTANCE) $(NAME)-dev
	docker cp $(INSTANCE):/app $(shell pwd)/app
	docker rm $(INSTANCE)

buildDockerImages:
	docker build -t $(NAME):$(TAG) -f ./docker/payment/Dockerfile .


