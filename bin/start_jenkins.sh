#!/bin/sh

export JAVA_PATH=/usr/local/jdk1.8.0_271/bin
export MARU_PATH=/home/bkwinners/MARU_SUGI

# JVM_ARGS for VM
##########################
JVM_ARGS="-DMARU_SUGI -server -DCP_CONF=$MARU_PATH/conf -Dlogback.configurationFile=$MARU_PATH/conf/logback.xml -Dfile.encoding=UTF-8 -Djava.io.tmpdir=$MARU_PATH/web/upload"
JVM_ARGS="$JVM_ARGS -Xss512k -Xms1024m -Xmx2048m"
JVM_ARGS="$JVM_ARGS -cp $MARU_PATH/lib/*:$MARU_PATH/lib/apache/*:$MARU_PATH/lib/tomcat/*:$MARU_PATH/lib/spring/*:$MARU_PATH/web/WEB-INF/classes"

nohup $JAVA_PATH/java $JVM_ARGS com.pgmate.lib.tomcat.Tomcat8 > /dev/null 2>&1 &
echo $!>pwd.pid
