#!/bin/bash

IP=$(ip add | grep global | sed -r 's|^\s*inet\s+([^/]+)/.*$|\1|g')

/etc/init.d/glusterfs-server start

mkdir -p /srv/glusterfs/brick0

gluster volume create vol0 $IP:/srv/glusterfs/brick0 force

gluster volume start vol0

mount.glusterfs $IP:vol0 /mnt

mkdir -p /mnt/.test/ && chmod 777 /mnt/.test/
