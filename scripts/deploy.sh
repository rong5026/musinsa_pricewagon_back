#!/bin/bash

REPOSITORY="/home/hong/app/pricewagon-back"
LOG_PATH="$REPOSITORY/deploy.log"
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)
JAR_PATH="$REPOSITORY/$JAR_NAME"

CURRENT_PID=$(pgrep -f $JAR_NAME)

# 로그 파일 생성 (없을 시) 및 시작 로그 기록
if [ ! -f "$LOG_PATH" ]; then
  touch "$LOG_PATH"
fi

echo "======================" >> "$LOG_PATH"
echo "$(date '+%Y-%m-%d %H:%M:%S') - 배포 시작" >> "$LOG_PATH"
echo "JAR 파일 경로: $JAR_PATH" >> "$LOG_PATH"

# 현재 실행 중인 애플리케이션 종료
if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> "$LOG_PATH"
else
  echo "> 실행 중인 애플리케이션 종료 시도, PID: $CURRENT_PID" >> "$LOG_PATH"
  sudo kill -15 $CURRENT_PID
  sleep 5

  # 프로세스가 종료되었는지 확인
  CURRENT_PID=$(pgrep -f $JAR_NAME)
  if [ -z "$CURRENT_PID" ]; then
    echo "> 애플리케이션이 정상적으로 종료되었습니다." >> "$LOG_PATH"
  else
    echo "> 애플리케이션이 종료되지 않아 강제 종료를 시도합니다." >> "$LOG_PATH"
    sudo kill -9 $CURRENT_PID
  fi
fi

# 새로운 애플리케이션 실행
echo "> 새 애플리케이션 배포: $JAR_PATH" >> "$LOG_PATH"
nohup java -jar -Dspring.profiles.active=prod "$JAR_PATH" > "$REPOSITORY/nohup.out" 2>&1 &

NEW_PID=$(pgrep -f $JAR_NAME)
if [ -z "$NEW_PID" ]; then
  echo "> 애플리케이션 실행 실패" >> "$LOG_PATH"
else
  echo "> 애플리케이션 실행 완료, 새 PID: $NEW_PID" >> "$LOG_PATH"
fi

echo "$(date '+%Y-%m-%d %H:%M:%S') - 배포 완료" >> "$LOG_PATH"