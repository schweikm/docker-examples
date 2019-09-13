##
## Multi-stage build for OpenJDK
##
## This assumes you are using the current version of docker-ce which is 19.03
## You should **NOT** be using the 'docker' package from centos extras
##
## multi-stage builds:  https://docs.docker.com/develop/develop-images/multistage-build
##
## This is not a production-ready Docker image but illistrates some core principals


## build image with:  docker build -t `hostname`/`whoami`/java:latest .
## run   image with:  docker run --rm -it `hostname`/`whoami`/java:latest


##
## stage build: install OpenJDK, maven and package the jar
##


# every 'FROM' statement initiates a new stage
# the centos base image is 202MB
FROM docker.io/library/centos:7 as build

# static label for helpful information
LABEL maintainer="Marc Schweikert <schweikm@gmail.com>"

# build args can be overridden on the command line
# https://docs.docker.com/engine/reference/builder/#arg
ARG MVN_VERSION=3.6.2

# in this example I'll just copy in the tar
# production-level code would install via RPM
COPY apache-maven-${MVN_VERSION}-bin.tar.gz /

# install and configure the build dependencies
# make sure you cleanup artifacts you don't want to embed in the final layer!
RUN yum -y upgrade && \
    yum -y install java-1.8.0-openjdk-devel && \
    tar xf /apache-maven-${MVN_VERSION}-bin.tar.gz -C /opt/ && \
    ln -nfs /opt/apache-maven-${MVN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm -fr /var/cache/yum/* /apache-maven-${MVN_VERSION}-bin.tar.gz

# copy in the source code
COPY my-app /usr/src/my-app

# now compile the source code into a single jar
RUN export JAVA_HOME=/usr/lib/jvm/java && \
    cd /usr/src/my-app && \
    mvn package


# at this point the final build stage is 486MB


##
## stage final: copy artifacts from build stage and make runnable container
##


# another FROM statement starts a new image
FROM docker.io/library/centos:7 as final

# static label for helpful information
LABEL maintainer="Marc Schweikert <schweikm@gmail.com>"

# remember that this is a new image so we need to install the required packages
RUN yum -y upgrade && \
    yum -y install java-1.8.0-openjdk-headless && \
    mkdir -p /apps && \
    groupadd --gid 1000 javauser && \
    useradd --uid 1000 --gid 1000 --shell /sbin/nologin --home-dir /apps --no-log-init --no-create-home javauser && \
    rm -fr /var/cache/yum/*

# the magic step - copy the build artifact from the previous stage
# the 'FROM <> as build' gives the stage a name we can refer to here
COPY --from=build /usr/src/my-app/target/*.jar /apps/

# ensure permissions are set
RUN chown -R javauser: /apps

# instead of defining the container command here, delegate to a shell script
# this is a personal preference but keeps the Dockerfile simple
COPY docker-entrypoint.sh /

# the 'ENTRYPOINT' and 'CMD' directives are different
# https://docs.docker.com/engine/reference/builder/#entrypoint
# https://docs.docker.com/engine/reference/builder/#cmd
ENTRYPOINT ["/docker-entrypoint.sh"]

# don't run containers as root
USER javauser
WORKDIR /apps

# the final image is 362MB
