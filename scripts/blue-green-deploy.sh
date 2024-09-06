#!/bin/bash

# 작업 디렉토리 설정
cd /home/hong/app/pricewagon-blue-green

# DOCKER_APP_NAME이 비어있으면 기본값을 설정
DOCKER_APP_NAME=pricewagon


DEPLOY_LOG="/home/hong/app/blue-green-deploy.log"  # 로그 파일 경로를 변수로 설정

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG

# 실행중인 blue가 있는지 확인
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps spring-blue | grep Up)

# 이미 실행 중인 blue가 있으면 종료
if [ -n "$EXIST_BLUE" ]; then
  echo "이미 실행 중인 blue 컨테이너가 있습니다. 종료 중..." >> $DEPLOY_LOG
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml down spring-blue
  echo "blue 컨테이너 종료 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
fi

# blue 배포 시작
echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml up -d --build spring-blue

# blue 컨테이너 실행 확인
for i in {1..6}; do
  sleep 7
  BLUE_HEALTH=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps spring-blue | grep Up)
  if [ -n "$BLUE_HEALTH" ]; then
    break
  fi
done

if [ -z "$BLUE_HEALTH" ]; then
  echo "blue 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
else
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml down spring-green
  docker image prune -af
  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
fi

# green 배포 시작
echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml up -d --build spring-green

for i in {1..6}; do
  sleep 7
  GREEN_HEALTH=$(docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml ps spring-green | grep Up)
  if [ -n "$GREEN_HEALTH" ]; then
    break
  fi
done

if [ -z "$GREEN_HEALTH" ]; then
  echo "green 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
else
  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml down spring-blue
  docker image prune -af
  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
echo "===================== 배포 완료 =====================" >>  $DEPLOY_LOG
echo >>  $DEPLOY_LOG