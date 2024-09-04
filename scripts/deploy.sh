#!/bin/bash

APP_NAME=PRICEWAGON
REPOSITORY=/home/hong/app/pricewagon-back
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

# 현재 실행 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar \
  -Dspring.profiles.active=prod \
  $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &