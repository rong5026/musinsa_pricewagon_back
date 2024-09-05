#!/bin/bash

# 작업 디렉토리 설정
cd /home/hong/app/pricewagon-back

# DOCKER_APP_NAME이 비어있으면 기본값을 설정
DOCKER_APP_NAME=pricewagon

DEPLOY_LOG="/home/hong/app/blue-green-deploy.log"  # 로그 파일 경로를 변수로 설정

# 실행중인 blue가 있는지 확인
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG

  # blue 배포
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  # 컨테이너 실행 확인
  for i in {1..6}; do
    sleep 7
    BLUE_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
    if [ -n "$BLUE_HEALTH" ]; then
      break
    fi
  done

  if [ -z "$BLUE_HEALTH" ]; then
    echo "blue 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  else
    echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
    sudo docker image prune -af
    echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
  fi

# blue가 실행중이면 green up
else
  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  for i in {1..6}; do
    sleep 7
    GREEN_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)
    if [ -n "$GREEN_HEALTH" ]; then
      break
    fi
  done

  if [ -z "$GREEN_HEALTH" ]; then
    echo "green 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  else
    echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
    sudo docker image prune -af
    echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  fi

fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
echo "===================== 배포 완료 =====================" >>  $DEPLOY_LOG
echo >>  $DEPLOY_LOG