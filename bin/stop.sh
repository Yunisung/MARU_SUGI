#!/bin/sh 
#kill -9 `cat < pwd.pid`
PID=`cat < pwd.pid`

if [ -z $PID ]; then
    echo 'PID가 존재하지 않습니다.'
else 
	echo 'PID ========> ${PID}'
fi

#rm pwd.pid