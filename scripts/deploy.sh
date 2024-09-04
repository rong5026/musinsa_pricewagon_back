#!/bin/bash

APP_NAME="PRICEWAGON"
REPOSITORY="/home/hong/app/pricewagon-back"
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)
JAR_PATH="$REPOSITORY/$JAR_NAME"

CURRENT_PID=$(pgrep -f $APP_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> 실행 중인 애플리케이션 종료 시도, PID: $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5

  # 프로세스가 종료되었는지 확인
  CURRENT_PID=$(pgrep -f $APP_NAME)
  if [ -z "$CURRENT_PID" ]; then
    echo "> 애플리케이션이 정상적으로 종료되었습니다."
  else
    echo "> 애플리케이션이 종료되지 않아 강제 종료를 시도합니다."
    sudo kill -9 $CURRENT_PID
  fi
fi

# 새로운 애플리케이션 실행
echo "> 새 애플리케이션 배포: $JAR_PATH"
nohup java -jar \
  -Dspring.profiles.active=prod \
  $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &

NEW_PID=$(pgrep -f $APP_NAME)
echo "> 애플리케이션 실행 완료, 새 PID: $NEW_PID"