#!/bin/bash -e

PROJECT_DIR=$(cd `dirname $0`/.. && pwd)

BASE_DIR=$(cd `dirname $0` && pwd)

BUILD_IMAGE_FQNAME=agfsapi4j-integration-test-build:local

cd $BASE_DIR
BUILD_IMAGE_CHECKSUM=$(tar c assets/ Dockerfile | md5sum | cut -d' ' -f 1)
BUILD_IMAGE_CACHE_FILE=$HOME/.cache/docker/agfsapi4j-integration-test-build.$BUILD_IMAGE_CHECKSUM.docker.tar

ls -la $(dirname $BUILD_IMAGE_CACHE_FILE)/

if [ -f "$BUILD_IMAGE_CACHE_FILE" ]; then
  docker load -i $BUILD_IMAGE_CACHE_FILE
else
  rm $(dirname $BUILD_IMAGE_CACHE_FILE)/*.docker.t?? || true
  docker build --tag $BUILD_IMAGE_FQNAME -f $BASE_DIR/Dockerfile $BASE_DIR
  mkdir -p $(dirname $BUILD_IMAGE_FQNAME) && \
  docker save -o $BUILD_IMAGE_CACHE_FILE $BUILD_IMAGE_FQNAME
fi

CMD="mvn -Dsonar.login=$SONAR_TOKEN -Dcom.github.agfsapi4j.it.hostname=\$HOSTNAME -Pcode-analysis,integration-tests,sonar-upload clean verify"
if [ $# -ne 0 ]; then
  CMD="$*"
fi

docker run -ti --rm -e DEBUG_ENTRYPOINT=1 \
	-e WORKER_UID=$(id -u) \
	-e WORKER_GID=$(id -g) \
	-v $PROJECT_DIR/:/work \
	-v $HOME/.m2/repository/:/home/worker/.m2/repository/ \
	--privileged \
	$BUILD_IMAGE_FQNAME \
	"$CMD" 
