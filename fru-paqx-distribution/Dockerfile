#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#
#  USAGE: (from .. dir)
#    docker build -f fru-paqx-distribution/Dockerfile --tag=fru-paqx fru-paqx
#

# Start from base image with Java 8
FROM java:8-jre-alpine

WORKDIR /opt/dell/cpsd/fru-paqx/

RUN mkdir -p ./bin \
&& mkdir -p ./logs \
&& mkdir -p ./lib \
&& mkdir -p ./install \
&& mkdir -p ./conf

COPY target/dependency/lib/* ./lib/
COPY build/conf/* ./conf/
COPY build/service/run.sh ./bin/

RUN chmod 755 ./bin/run.sh

EXPOSE 18443
EXPOSE 8085

ARG DELL_JAVA_VERSION=unknown
ARG BUILD_NUMBER=unknown
ARG GIT_COMMIT=unknown
ARG BUILD_TYPE=dev
ARG CPSD_SOFTWARE_VERSION=unknown
LABEL   com.dell.cpsd.version=CPSD_SOFTWARE_VERSION\
        com.dell.cpsd.git_revision=$GIT_COMMIT\
        com.dell.cpsd.build_number=$BUILD_NUMBER\
        com.dell.cpsd.build_type=$BUILD_TYPE\
        com.dell.cpsd.java=$DELL_JAVA_VERSION

CMD ["/opt/dell/cpsd/fru-paqx/bin/run.sh"]
