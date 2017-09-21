#!/bin/bash 

if [ ! -z "$DEBUG_ENTRYPOINT" ]; then
  set +x
fi

if [ "x$UID" == "x0" ]; then
  DEFAULT_WORKER_UID=1000
  DEFAULT_WORKER_GID=1000

  if [ -z "${WORKER_UID}" ]; then
    WORKER_UID=${DEFAULT_WORKER_UID}
  fi
  if [ -z "${WORKER_GID}" ]; then
    WORKER_GID=${DEFAULT_WORKER_GID}
  fi

  groupadd -g ${WORKER_GID} worker
  useradd -u ${WORKER_UID} -g ${WORKER_GID} -G sudo -d /home/worker -m worker
  chown -R worker.worker /home/worker/

  WORK_DIR=$PWD

  chown $WORKER_UID.$WORKER_GID /work
else
  echo "Should be run as root. Container will drop privileges on its own."
fi

for f in /entrypoint.d/*; do
  echo "Running $f..."
  $f
done

CMD="$@"

exec su - worker -c "cd $WORK_DIR && exec $CMD"
