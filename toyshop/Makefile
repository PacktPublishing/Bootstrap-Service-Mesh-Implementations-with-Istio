buildToyShop: 
	$(MAKE) buildDockerImages -C ./carts
	$(MAKE) buildDockerImages -C ./catalogue
	$(MAKE) buildDockerImages -C ./front-end
	$(MAKE) buildDockerImages -C ./orders
	$(MAKE) buildDockerImages -C ./payment
	$(MAKE) buildDockerImages -C ./user
	$(MAKE) buildDockerImages -C ./shipping

deployToMinikube:

	$(MAKE) buildToyShop
	kubectl create -f ./microservices-demo/deploy/kubernetes/manifests/00-sock-shop-ns.yaml
	kubectl create -f ./microservices-demo/deploy/kubernetes/manifests

cleanUpMinikube:
	kubectl delete -f /Users/anand.rai@konghq.com/go/src/github.com/packtpublishing/bootstrap-service-mesh-implementations-with-istio/toyshop/microservices-demo/deploy/kubernetes/manifests/00-sock-shop-ns.yaml

