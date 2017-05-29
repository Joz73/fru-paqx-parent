#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#
Summary: Dell Inc. FRU PAQX
Name: fru-paqx
Version: %_version
Release: %_revision
License: Commercial
Vendor: Dell Inc.
Group: System Environment/Dell Inc. Applications
URL: http://www.dell.com
Requires: jre = 1.8.0

%define _use_internal_dependency_generator 0
%define __find_requires %{nil}

%description
Dell Inc. FRU PAQX


##############################################################################
# build
##############################################################################
%build

# Creates directory if it doesn't exist
# $1: Directory path
init_dir ()
{
    [ -d $1 ] || mkdir -p $1
}

##############################################################################
# check and create the  directories
##############################################################################
init_dir ${RPM_BUILD_ROOT}/usr/lib/systemd/system

# check and create the directories for the service
SERVICE_BUILD_ROOT=${RPM_BUILD_ROOT}/opt/dell/cpsd/fru-paqx
init_dir ${SERVICE_BUILD_ROOT}
init_dir ${SERVICE_BUILD_ROOT}/install
init_dir ${SERVICE_BUILD_ROOT}/conf
init_dir ${SERVICE_BUILD_ROOT}/image
init_dir ${SERVICE_BUILD_ROOT}/logs


##############################################################################
# copy the unit file
cp ${RPM_SOURCE_DIR}/build/service/fru-paqx.service ${RPM_BUILD_ROOT}/usr/lib/systemd/system

# copy the install scripts
cp -riv ${RPM_SOURCE_DIR}/build/conf/*.properties ${SERVICE_BUILD_ROOT}/conf
#cp -riv ${RPM_SOURCE_DIR}/build/conf/*.xml ${SERVICE_BUILD_ROOT}/conf
cp -riv ${RPM_SOURCE_DIR}/target/temp_deployment/*.sh ${SERVICE_BUILD_ROOT}/install
cp -riv ${RPM_SOURCE_DIR}/target/temp_deployment/*.yml ${SERVICE_BUILD_ROOT}/install
cp -riv ${RPM_SOURCE_DIR}/target/temp_deployment/.env ${SERVICE_BUILD_ROOT}/install/.env

# copy the image to the required directories
cp ${RPM_SOURCE_DIR}/target/temp_deployment/*.tar ${SERVICE_BUILD_ROOT}/image

##############################################################################
# pre
##############################################################################
%pre
getent group dell >/dev/null || /usr/sbin/groupadd -f -r dell
getent passwd fru >/dev/null || /usr/sbin/useradd -r -g dell -s /sbin/nologin -M fru
exit 0


##############################################################################
# post
##############################################################################
%post
if [ $1 -eq 1 ];then
    /bin/sh /opt/dell/cpsd/fru-paqx/install/fru-paqx-install.sh
elif [ $1 -eq 2 ];then
    /bin/sh /opt/dell/cpsd/fru-paqx/install/fru-paqx-upgrade.sh
else
    echo "Unexpected argument passed to RPM %post script: [$1]"
    exit 1
fi
exit 0


##############################################################################
# preun
##############################################################################
%preun
if [ $1 -eq 0 ];then
    /bin/sh /opt/dell/cpsd/fru-paqx/install/fru-paqx-remove.sh
fi
exit 0

##############################################################################
# configure directory and file permissions
##############################################################################
%files

%attr(644,root,root) /usr/lib/systemd/system/fru-paqx.service

%attr(0755,fru,dell) /opt/dell/cpsd/fru-paqx/

%attr(0755,fru,dell) /opt/dell/cpsd/fru-paqx/install/

%attr(0755,fru,dell) /opt/dell/cpsd/fru-paqx/logs/

%attr(0755,fru,dell) /opt/dell/cpsd/fru-paqx/image/

%attr(0755,fru,dell) /opt/dell/cpsd/fru-paqx/conf/