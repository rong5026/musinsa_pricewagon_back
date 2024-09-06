#!/bin/bash

# 작업 디렉토리 설정
cd /home/hong/app/pricewagon-blue-green

# DOCKER_APP_NAME이 비어있으면 기본값을 설정
DOCKER_APP_NAME=pricewagon

DEPLOY_LOG="/home/hong/app/blue-green-deploy.log"  # 로그 파일 경로를 변수로 설정
#NGINX_CONFIG="/home/hong/app/pricewagon-blue-green/nginx.conf"

# 실행중인 blue가 있는지 확인
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps | grep spring-blue-container | grep Up)

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG

  # blue 배포
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml up -d --build spring-blue

  # 컨테이너 실행 확인
  for i in {1..6}; do
    sleep 7
   BLUE_HEALTH=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps  | grep spring-blue-container | grep Up)
    if [ -n "$BLUE_HEALTH" ]; then
      break
    fi
  done

  if [ -z "$BLUE_HEALTH" ]; then
    echo "blue 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  else
#    # Nginx 설정에서 blue을 사용하도록 변경
#    sed -i 's/spring-green:8082/spring-blue:8081/g' $NGINX_CONFIG
#    docker-compose -p nginx-proxy -f docker-compose.yml restart nginx
#    echo "Nginx가 blue로 업데이트됨" >> $DEPLOY_LOG

    echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml stop spring-green
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml rm -f spring-green
    echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
  fi

# blue가 실행중이면 green up
else
  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>  $DEPLOY_LOG
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml up -d --build spring-green

  for i in {1..6}; do
    sleep 7
    GREEN_HEALTH=$(docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.yml ps | grep spring-green-container | grep Up)
    if [ -n "$GREEN_HEALTH" ]; then
      break
    fi
  done

  if [ -z "$GREEN_HEALTH" ]; then
    echo "green 배포 도중 실패 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  else
#    # Nginx 설정에서 green을 사용하도록 변경
#    sed -i 's/spring-blue:8081/spring-green:8082/g' $NGINX_CONFIG
#    docker-compose -p nginx-proxy -f docker-compose.yml restart nginx
#    echo "Nginx가 green로 업데이트됨" >> $DEPLOY_LOG

    echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml stop spring-blue
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml rm -f spring-blue
    echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  fi

fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
echo "===================== 배포 완료 =====================" >>  $DEPLOY_LOG
echo >>  $DEPLOY_LOG