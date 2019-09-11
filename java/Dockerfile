##
## stage build
##
FROM docker.io/library/centos:7 as build

LABEL maintainer="Marc Schweikert <schweikm@gmail.com>"

ARG MVN_VERSION=3.6.2

COPY apache-maven-${MVN_VERSION}-bin.tar.gz /

RUN yum upgrade && \
    yum -y install java-1.8.0-openjdk-devel && \
    tar xf /apache-maven-${MVN_VERSION}-bin.tar.gz -C /opt/ && \
    ln -nfs /opt/apache-maven-${MVN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm -fr /var/cache/yum/* /apache-maven-${MVN_VERSION}-bin.tar.gz

COPY my-app /usr/src/my-app

RUN export JAVA_HOME=/usr/lib/jvm/java && \
    cd /usr/src/my-app && \
    mvn package && \
    mkdir -p /apps && \
    cp target/*.jar /apps/ && \
    ls -la /apps/

CMD ["java -cp /apps/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App"]
