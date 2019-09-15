#!/bin/bash

# export 	QUARKUS_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/blogdb
docker-compose up -d blogdb 
cd ./backend
docker build -f src/main/docker/Dockerfile.multistage -t hantsy/quarkus-post-service .
docker image ls|grep quarkus-post-service
echo "done."