#!/bin/sh 
kill -9 `cat < pwd.pid`
rm pwd.pid