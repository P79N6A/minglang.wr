#!/bin/bash
##
## Usage  :    sh bin/jbossctl pubstart
## Author :    baoqing.guobq@alibaba-inc.com, juven.xuxb@alibaba-inc.com
## Docs   :    http://gitlab.alibaba-inc.com/spring-boot/docs/wikis/home
## =========================================================================================

#######  error code specification  #########
# Please update this documentation if new error code is added.
# 1   => reserved for script error
# 2   => bad usage
# 3   => bad user
# 4   => tomcat start failed
# 5   => preload.sh check failed
# 6   => hsf online failed
# 7   => nginx start failed
# 8   => status.taobao check failed
# 9   => hsf offline failed
# 128 => exit with error message

PROG_NAME=$0
ACTION=$1
JAVA_PORT="7001"
NGINX_SKIP=1
HEALTH_URL="http://localhost:7002/health"
usage() {
    echo "Usage: $PROG_NAME {start|stop|online|offline|pubstart|restart|deploy}"
    exit 2 # bad usage
}

if [ "$UID" -eq 0 ]; then
    echo "can't run as root, please use: sudo -u admin $0 $@"
    exit 3 # bad user
fi

if [ $# -lt 1 ]; then
    usage
    exit 2 # bad usage
fi

APP_HOME=$(cd $(dirname $0)/..; pwd)
source "$APP_HOME/bin/setenv.sh"

PACKAGING="unknown"
RUNNABLE_PACKAGE="unknown"
EXPLODED_TARGET="${APP_HOME}/target/exploded"

extract_tgz() {
    echo "[ 5/10] -- extract tgz package"

    if tar tvf ${APP_TGZ} | grep -q "${APP_NAME}.jar" ; then
        echo "        -- package to launch: ${APP_NAME}.jar"
        PACKAGING="jar"
        RUNNABLE_PACKAGE=${APP_NAME}.jar
    elif tar tvf ${APP_TGZ} | grep -q "${APP_NAME}.war" ; then
        echo "        -- package to launch: ${APP_NAME}.war"
        PACKAGING="war"
        RUNNABLE_PACKAGE=${APP_NAME}.war
    else
        echo "        -- no package found" && exit 2
    fi

    cd "${APP_HOME}/target" || exit 1
    rm -rf ${APP_HOME}/target/${RUNNABLE_PACKAGE}
    tar xzf ${APP_TGZ} -C "${APP_HOME}/target/"
    [[ ${APP_HOME}/target/${RUNNABLE_PACKAGE} ]] || ( echo "unzip failed" && exit 1 )

    ## special logic for war package, its exploded dir must be same as war file name
    if [[ "${PACKAGING}" == "war" ]]; then
        mv ${APP_HOME}/target/${RUNNABLE_PACKAGE} ${APP_HOME}/target/${RUNNABLE_PACKAGE}.original
        EXPLODED_TARGET="${APP_HOME}/target/${RUNNABLE_PACKAGE}"
        RUNNABLE_PACKAGE="${RUNNABLE_PACKAGE}.original"
    fi

    cd "${APP_HOME}/target" || exit 1
    if [ -d ${EXPLODED_TARGET} ] ; then
        rm -fr ${EXPLODED_TARGET}
    fi
    mkdir -p ${EXPLODED_TARGET}
    unzip -q -d ${EXPLODED_TARGET} ${RUNNABLE_PACKAGE}
    echo "        -- package exploded to: ${EXPLODED_TARGET}"
}

startjava() {
    extract_tgz
    echo "[ 6/10] -- start java process"

    cd ${EXPLODED_TARGET} || exit 1
    echo "        -- java stdout log: ${JAVA_OUT}"
    SPRINGBOOT_OPTS="${SPRINGBOOT_OPTS} --startup.at=$(($(date +%s%N)/1000000))"

    if [[ "${PACKAGING}" == "jar" ]]; then
        nohup $JAVA_HOME/bin/java $JAVA_OPTS org.springframework.boot.loader.JarLauncher $SPRINGBOOT_OPTS &>$JAVA_OUT &
    elif [[ "${PACKAGING}" == "war" ]]; then
        nohup $JAVA_HOME/bin/java $JAVA_OPTS org.springframework.boot.loader.WarLauncher $SPRINGBOOT_OPTS &>$JAVA_OUT &
    else
        echo "        -- unable to launch java in ${EXPLODED_TARGET} " && exit 1
    fi

    echo "[ 7/10] -- check health for java application"
    . "$APP_HOME/bin/preload.sh"
    [[ $? -ne 0 ]] && ( echo "check heath failed, exit" && exit 1 )
}

online_hsf() {
    echo "[10/10] -- online hsf from hsf registry server"
    ONLINE_HSF_URL="http://localhost:7002/hsf/online"
    if curl -s --connect-timeout 3 --max-time 5 ${HEALTH_URL} | grep -q hsf ; then
        hsf_onlined=false
        times=10
        for e in $(seq 10); do
            curl -s --connect-timeout 3 --max-time 5 ${ONLINE_HSF_URL} -o /dev/null
            sleep 1            
            HEALTH_CHECK_CODE=`curl -s --connect-timeout 3 --max-time 5 ${HEALTH_URL} -o /dev/null -w %{http_code}`
            if [ "$HEALTH_CHECK_CODE" == "200" ]; then
                hsf_onlined=true
                break
            else
                COSTTIME=$(($times - $e ))
                echo -n -e  "\online hsf `expr $COSTTIME` seconds."
            fi            
        done
        if [ "$hsf_onlined" = false ] ; then
            echo "hsf online failed. HSF is NOT online!"
            exit 6
        fi
    fi
}

offline_hsf() {
    echo "[ 2/10] -- offline hsf from hsf registry server"
    OFFLINE_HSF_URL="http://localhost:7002/hsf/offline"
    if curl -s --connect-timeout 3 --max-time 5 ${HEALTH_URL} | grep -q hsf ; then
        hsf_offlined=false
        times=10
        for e in $(seq 10); do
            curl -s --connect-timeout 3 --max-time 5 ${OFFLINE_HSF_URL} -o /dev/null
            sleep 1            
            HEALTH_CHECK_CODE=`curl -s --connect-timeout 3 --max-time 5 ${HEALTH_URL} -o /dev/null -w %{http_code}`
            if [ "$HEALTH_CHECK_CODE" == "503" ] && curl -s ${HEALTH_URL} | grep -q OUT_OF_SERVICE ; then
                hsf_offlined=true
                sleep 5 ## sleep some time to make sure all visits are finished
                break
            else
                COSTTIME=$(($times - $e ))
                echo -n -e  "\offline hsf `expr $COSTTIME` seconds."
            fi            
        done
        if [ "$hsf_offlined" = false ] ; then
            echo "hsf offline failed. HSF is still online!"            
        fi
    fi        
}

online_http() {
    echo "[ 9/10] -- online http from load balance server"
    if [ "${NGINX_SKIP}" -ne "1" ]; then
        mkdir -p $STATUSROOT_HOME
        touch -m $STATUSROOT_HOME/status.taobao        
        touch -m $STATUSROOT_HOME/status.html
    fi
}

offline_http() {
    echo "[ 1/10] -- offline http from load balance server"
    if [ "${NGINX_SKIP}" -ne "1" ]; then
        rm -f $STATUSROOT_HOME/status.taobao
        rm -f $STATUSROOT_HOME/status.html
        sleep 5 ## sleep some time to make sure all visits are finished
    fi    
}

online() {
    online_http
    online_hsf
}


offline() {
    offline_http
    offline_hsf
}

start_http() {
    echo "[ 8/10] -- start http server"
    if [ "${NGINX_SKIP}" -ne "1" ]; then
        "$NGINXCTL" start | sed 's/^/        -- /g'
        if [ "$?" == "0" ]; then
            :
        else
            echo "HTTP Start Failed."
            exit 7 # nginx start failed
        fi
    fi    
}

stop_http() {
    echo "[ 3/10] -- stop http server"
    if [ "${NGINX_SKIP}" -ne "1" ]; then
        "$NGINXCTL" stop | sed 's/^/        -- /g'
    fi    
}

stopjava() {
    echo "[ 4/10] -- stop java process"
    times=60
    for e in $(seq 60)
    do
        sleep 1
        COSTTIME=$(($times - $e ))
        checkjavapida=`ps -ef|grep java|grep $APP_NAME|grep -v appctl.sh|grep -v jbossctl| grep -v restart.sh |grep -v grep`
        if [[ $checkjavapida ]];then
                checkjavapid=`ps -ef|grep java|grep $APP_NAME|grep -v appctl.sh|grep -v jbossctl | grep -v restart.sh |grep -v grep|awk '{print $2}'`
                kill -9 $checkjavapid
                echo -n -e  "\r        -- stopping java lasts `expr $COSTTIME` seconds."
        else
                break;
        fi
    done
    echo ""
}

backup() {
    echo "backup..."
}


case "$ACTION" in
    start)
        startjava
        start_http
    ;;
    stop)
        offline
        stop_http
        stopjava
    ;;
    pubstart)
        offline
        stop_http
        stopjava
        startjava
        start_http
        online
    ;;
    online)
        online
    ;;
    offline)
        offline
    ;;
    restart)
        offline
        stop_http
        stopjava
        startjava
        start_http
        online
    ;;
    deploy)
        stop_http
        stopjava
        startjava
        start_http
        backup
    ;;
    *)
        usage
    ;;
esac
