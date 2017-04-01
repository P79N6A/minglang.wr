#!/bin/bash

##
## Usage  :    sh entrypoint.sh
## Author :    fanzhong@alibaba-inc.com
## func   :    init container and init start script for app
## =========================================================================================

#######  error code specification  #########
# Please update this documentation if new error code is added.Error code should more than 10000
# 10003   => init start script error for app

APP_NAME=`[ -f /home/admin/.appname ] && cat /home/admin/.appname`

if [[ -n "$APP_NAME" && -d "/home/admin/app-script-templates" ]]; then
    echo "[INFO]=>init scripts for ${APP_NAME}"
    cp -vu /home/admin/app-script-templates/* /home/admin/${APP_NAME}/bin/ || exit 10003
    mv /home/admin/app-script-templates /tmp/
fi

/home/admin/${APP_NAME}/bin/jbossctl pubstart