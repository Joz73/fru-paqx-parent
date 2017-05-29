#!/bin/bash
#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#

SERVICE_BASE=/opt/dell/cpsd/fru-paqx

echo "Removing Dell Inc. FRU PAQX Service"

echo "Stopping Dell Inc. FRU PAQX Service"
systemctl stop fru-paqx

echo "Disabling Dell Inc. FRU PAQX Service"
systemctl disable fru-paqx

echo "Cleaning up Dell Inc. FRU PAQX Service Components"
rm -rf ${SERVICE_BASE}
docker rmi cpsd-fru-paqx-service:${IMAGE_TAG}

echo "Dell Inc. FRU PAQX components removal has completed successfully."

exit 0
