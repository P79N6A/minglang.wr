#!/bin/bash
##
## Usage  :    sh bin/jbossctl pubstart
## Author :    baoqing.guobq@alibaba-inc.com, juven.xuxb@alibaba-inc.com
## Docs   :    http://gitlab.alibaba-inc.com/spring-boot/docs/wikis/home
## =========================================================================================

## in case there's more than one app tgz, we should only use the newest one

SPRINGBOOT_OPTS="${SPRINGBOOT_OPTS} --logging.config=classpath:logback-spring.xml"
export SPRINGBOOT_OPTS


APP_TGZ_NUM=`ls ${APP_HOME}/target/*${APP_NAME}*.tgz | grep -v "appconf.tgz" | wc -l`
if [[ $APP_TGZ_NUM -gt 1 ]];then
    echo "[ WARN] -- more than one tgz package found, using the newest one."
    APP_TGZ_OLD=`ls -t ${APP_HOME}/target/*${APP_NAME}*.tgz | tail -n 1`
    APP_TGZ_BACKUP_TIME=`date +"%Y-%m-%d_%H-%M-%S"`
    mv $APP_TGZ_OLD ${APP_TGZ_OLD}.${APP_TGZ_BACKUP_TIME}
    echo "[ WARN] -- old package moved to ${APP_TGZ_OLD}.${APP_TGZ_BACKUP_TIME}."
fi

APP_TGZ=`ls ${APP_HOME}/target/*${APP_NAME}*.tgz | grep -v "appconf.tgz"`
APP_RELEASE="${APP_HOME}/target/station-bundle.release"
if [ -f "$APP_RELEASE" ] ; then
    rm -rf "$APP_RELEASE" || exit
fi

tar tvf ${APP_TGZ} | grep -q "station-bundle.release"
if [ $? == 0 ] ; then
    tar xzf ${APP_TGZ} -C "${APP_HOME}/target/" station-bundle.release || exit
fi

if [ -f "$APP_RELEASE" ] && grep -q 'baseline.jdk' "$APP_RELEASE" ; then
    dos2unix -q "$APP_RELEASE"
    BASELINE_JDK=`grep "baseline.jdk" "$APP_RELEASE" | awk -F'=' '{print $2}' | tr -d ' '`
    if [[ $BASELINE_JDK == ajdk* ]] ; then
        if [ ! -d "/opt/taobao/install/$BASELINE_JDK" ]; then
            echo "Error: baseline.jdk $BASELINE_JDK specified in ${APP_NAME}.release is not installed."
            exit 1;
        fi
        export JAVA_HOME=/opt/taobao/install/"$BASELINE_JDK"
    fi

    ## extra java opts for testing environment
    if [ "$APP_ENVIRONMENT" = 'test' ]; then
        if grep -q 'env.test.java-opts' "$APP_RELEASE" ; then
            ENV_TEST_JAVA_OPTS=`grep "env.test.java-opts" "$APP_RELEASE" | awk -F'=' '{ st = index($0,"=");print substr($0,st+1)}'`
            JAVA_OPTS="${JAVA_OPTS} ${ENV_TEST_JAVA_OPTS}"
            export JAVA_OPTS
        fi
    fi
fi

