SERVER_DIR = ./server
CLIENT_DIR = ./client

GRADLE = ./gradlew
NPM = npm

all: up

build-server:
	@cd ${SERVER_DIR} && ${GRADLE} clean build && cd ..

build-client:
	@cd ${CLIENT_DIR} && ${NPM} run build --prod && cd ..

build-all: build-server build-client

up: build-server
	docker-compose up --build

down:
	@docker-compose down

.PHONY: build-server build-client build-all up down