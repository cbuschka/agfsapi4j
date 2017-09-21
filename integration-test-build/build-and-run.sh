#!/bin/bash 

PROJECT_DIR=$(cd `dirname $0`/.. && pwd)

BASE_DIR=$(cd `dirname $0` && pwd)

docker build --tag agfsapi4j-integration-test-build:local -f $BASE_DIR/Dockerfile $BASE_DIR

CMD='mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent verify -Dcom.github.agfsapi4j.it.hostname=\$HOSTNAME -Pintegration-tests -Dmaven.repo.local=/work/.m2/repository/'
if [ $# -ne 0 ]; then
  CMD="$*"
fi

docker run -ti --rm -e DEBUG_ENTRYPOINT=1 \
	-e WORKER_UID=$(id -u) \
	-e WORKER_GID=$(id -g) \
	-v $PROJECT_DIR/:/work \
	--privileged \
	agfsapi4j-integration-test-build:local \
	"$CMD" 
