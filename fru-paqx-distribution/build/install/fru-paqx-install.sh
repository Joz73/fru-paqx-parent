#!/bin/bash
#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#

RETVAL=0

SERVICE_BASE=/opt/dell/cpsd/fru-paqx

echo "Installing Dell Inc. FRU PAQX components"


if [ ! -d "$SERVICE_BASE" ]; then
    echo "Could not find directory - [$SERVICE_BASE] does not exist."
    exit 1
fi

# add the credentials service
echo "Adding the Dell Inc. FRU PAQX Service"

docker load -i ${SERVICE_BASE}/image/cpsd-fru-paqx-service.tar
usermod -aG docker fru
systemctl enable fru-paqx
systemctl start fru-paqx

echo "Dell Inc. FRU PAQX install has completed successfully."

exit $RETVAL
