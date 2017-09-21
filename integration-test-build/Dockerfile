FROM debian:stretch

RUN apt-get update && \
	apt-get install -y sudo curl glusterfs-server openjdk-8-jdk-headless && \
	apt-get clean && rm -rf /var/lib/apt/lists/*

RUN curl -s -o /tmp/apache-maven.tgz http://babyname.tips/mirrors/apache/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.tar.gz && \
	tar xvfz /tmp/apache-maven.tgz -C /opt/ && \
	ln -s /opt/apache-maven-3.5.0/bin/mvn /usr/local/bin/mvn && \
	rm /tmp/apache-maven.tgz

COPY assets/entrypoint.sh /entrypoint.sh
RUN mkdir -p /entrypoint.d/ && \
	chmod 755 /entrypoint.sh

COPY assets/entrypoint.d/setup-glusterfs.sh /entrypoint.d
RUN chmod 755 /entrypoint.d/setup-glusterfs.sh

RUN echo "worker ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

ENV HOME=/home/worker

VOLUME /work
WORKDIR /work

ENTRYPOINT [ "/entrypoint.sh" ]
