all: build

build: build-jar build-docker

build-jar:
	mvn package

build-docker:
	docker build -t csms/coffee-society-member:latest .

clean:
	mvn clean

run-jar:
	java -jar target/coffee-society-member*.jar

run-docker:
	docker run -d --name coffee-society-member csms/coffee-society-member

tag:
	docker tag csms/coffee-society-member csms/coffee-society-member:${TAG}

push-latest:
	docker push csms/coffee-society-member:latest

push-tag:
	docker push csms/coffee-society-member:${TAG}

docker-login:
	@docker login -u "${DOCKER_ID}" -p "${DOCKER_PASS}"
	
docker-run: run-docker

docker-remove:
	docker rm -f coffee-society-member

docker-logs:
	docker logs coffee-society-member
