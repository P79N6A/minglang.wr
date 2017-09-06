#!/bin/bash
##
## Usage  :    sh bin/jbossctl pubstart
## Author :    baoqing.guobq@alibaba-inc.com, juven.xuxb@alibaba-inc.com
## Docs   :    http://gitlab.alibaba-inc.com/spring-boot/docs/wikis/home
## =========================================================================================

# by default, we don't know the environment to run
# but when we know it, e.g. test environment, we can add special logic, like adding debug port
# value candidates: test, production
if [ -z ${APP_ENVIRONMENT+x} ]; then
    APP_ENVIRONMENT_FROM_JMENV=`curl -s --connect-timeout 3 --max-time 5 --retry 3 --retry-delay 1 jmenv.tbsite.net:8080/env`
    if [ "$APP_ENVIRONMENT_FROM_JMENV" == "daily" ]; then
        APP_ENVIRONMENT="test"
    else
        APP_ENVIRONMENT=""
    fi
fi
HOME="$(getent passwd "$UID" | awk -F":" '{print $6}')" # fix "$HOME" by "$UID"
MIDDLEWARE_LOGS="${HOME}/logs"


# os env
# NOTE: must edit LANG and JAVA_FILE_ENCODING together
## TODO: review and fix
export LANG=zh_CN.GB18030
export JAVA_FILE_ENCODING=GB18030
export NLS_LANG=AMERICAN_AMERICA.ZHS16GBK
export LD_LIBRARY_PATH=/opt/taobao/oracle/lib:/opt/taobao/lib:$LD_LIBRARY_PATH

export JAVA_HOME=/opt/taobao/java
export CPU_COUNT="$(grep -c 'cpu[0-9][0-9]*' /proc/stat)"
ulimit -c unlimited

export JAVA_OUT=$APP_HOME/logs/java.log

# env check and calculate
#
if [ -z "$APP_NAME" ]; then
	APP_NAME=$(basename "${APP_HOME}")
fi

JAVA_OPTS="-server"

# use memory based on the available resources in the machine
let memTotal=`cat /proc/meminfo | grep MemTotal | awk '{printf "%d", $2/1024*0.75 }'`
if [ $memTotal -gt 6000 ]; then
    JAVA_OPTS="${JAVA_OPTS} -Xms4g -Xmx4g"
    JAVA_OPTS="${JAVA_OPTS} -Xmn2g"
else
    JAVA_OPTS="${JAVA_OPTS} -Xms2g -Xmx2g"
    JAVA_OPTS="${JAVA_OPTS} -Xmn1g"
fi
JAVA_OPTS="${JAVA_OPTS} -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m"
JAVA_OPTS="${JAVA_OPTS} -XX:MaxDirectMemorySize=1g"
JAVA_OPTS="${JAVA_OPTS} -XX:SurvivorRatio=10"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:CMSMaxAbortablePrecleanTime=5000"
JAVA_OPTS="${JAVA_OPTS} -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="${JAVA_OPTS} -XX:+ExplicitGCInvokesConcurrent -Dsun.rmi.dgc.server.gcInterval=2592000000 -Dsun.rmi.dgc.client.gcInterval=2592000000"
JAVA_OPTS="${JAVA_OPTS} -XX:ParallelGCThreads=${CPU_COUNT}"
JAVA_OPTS="${JAVA_OPTS} -Xloggc:${MIDDLEWARE_LOGS}/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${MIDDLEWARE_LOGS}/java.hprof"
JAVA_OPTS="${JAVA_OPTS} -Djava.awt.headless=true"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.client.defaultConnectTimeout=10000"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.client.defaultReadTimeout=30000"
JAVA_OPTS="${JAVA_OPTS} -Dfile.encoding=UTF-8"
JAVA_OPTS="${JAVA_OPTS} -Dproject.name=${APP_NAME}"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.port=1090"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="${JAVA_OPTS} -Djava.rmi.server.hostname=$HOSTNAME"
JAVA_OPTS="${JAVA_OPTS} -XX:+UsePerfData"

SPRINGBOOT_OPTS="--server.port=7001 --management.port=7002"
SPRINGBOOT_OPTS="${SPRINGBOOT_OPTS} --management.info.build.mode=full"
SPRINGBOOT_OPTS="${SPRINGBOOT_OPTS} --spring.profiles.active=production"
SPRINGBOOT_OPTS="${SPRINGBOOT_OPTS} --logging.path=${APP_HOME}/logs --logging.file=${APP_HOME}/logs/application.log"

export JAVA_OPTS
export SPRINGBOOT_OPTS


# nginx env
#
NGINX_HOME=/home/admin/cai
# if set to "1", skip start nginx.
test -z "$NGINX_SKIP" && NGINX_SKIP=0
NGINXCTL=$NGINX_HOME/bin/nginxctl
STATUSROOT_HOME="${APP_HOME}/target/htdocs"

# update JAVA_HOME and OPTS based on APP.RELEASE file
source "$APP_HOME/bin/update_setenv.sh"