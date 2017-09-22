#!/bin/bash -xe

PROJECT_DIR=$(cd `dirname $0`/.. && pwd)

BASE_DIR=$(cd `dirname $0` && pwd)

BUILD_IMAGE_CACHE_FILE=$HOME/.cache/docker/agfsapi4j-integration-test-build.docker.tar
BUILD_IMAGE_FQNAME=agfsapi4j-integration-test-build:local

if [ -f "$BUILD_IMAGE_CACHE_FILE" ]; then
  docker load -i $BUILD_IMAGE_CACHE_FILE
fi

docker build --tag $BUILD_IMAGE_FQNAME -f $BASE_DIR/Dockerfile $BASE_DIR

mkdir -p $(dirname $BUILD_IMAGE_FQNAME) && \
docker save -o $BUILD_IMAGE_CACHE_FILE $BUILD_IMAGE_FQNAME

CMD='mvn -Dcom.github.agfsapi4j.it.hostname=\$HOSTNAME -Pintegration-tests,sonar-upload clean verify'
if [ $# -ne 0 ]; then
  CMD="$*"
fi

echo "Command: $CMD"

docker run -ti --rm -e DEBUG_ENTRYPOINT=1 \
	-e WORKER_UID=$(id -u) \
	-e WORKER_GID=$(id -g) \
	-v $PROJECT_DIR/:/work \
	-v $HOME/.m2/repository/:/home/worker/.m2/repository/ \
	--privileged \
	$BUILD_IMAGE_FQNAME \
	"$CMD" 
