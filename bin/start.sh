#!/bin/sh

# JVM_ARGS for VM
##########################
JVM_ARGS="-DMARU_SUGI -server -DCP_CONF=../conf -Dlogback.configurationFile=../conf/logback.xml -Dfile.encoding=UTF-8 -Djava.io.tmpdir=../web/upload"
JVM_ARGS="$JVM_ARGS -Xss512k -Xms1024m -Xmx2048m"
JVM_ARGS="$JVM_ARGS -cp ../lib/*:../lib/apache/*:../lib/tomcat/*:../lib/spring/*:../web/WEB-INF/classes"


/usr/local/jdk1.8.0_271/bin/java $JVM_ARGS com.pgmate.lib.tomcat.Tomcat8 &
echo $!>pwd.pid
