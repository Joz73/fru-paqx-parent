#!/usr/bin/env bash
#call from pom
#step 1: make temporary directory, and target/deployment directory
mkdir -p target/temp_deployment
mkdir -p target/deployment

#save a tar of the docker image previously built to the temp directory with the other deployment files
docker save ${IMAGE_NAME}:${IMAGE_TAG} > target/temp_deployment/${IMAGE_NAME}.tar
cp build/install/* target/temp_deployment/
cp build/install/.env target/temp_deployment/.env
sed -i -- 's/\${IMAGE_TAG}/'${IMAGE_TAG}'/g' target/temp_deployment/docker-compose.yml
sed -i -- 's/\${IMAGE_TAG}/'${IMAGE_TAG}'/g' target/temp_deployment/fru-paqx-remove.sh