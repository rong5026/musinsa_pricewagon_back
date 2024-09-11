#!/bin/bash

## Blue/Green 배포 스크립트

# 작업 디렉토리 설정
cd /home/hong/app/pricewagon-blue-green

DOCKER_APP_NAME=pricewagon
DEPLOY_LOG="/home/hong/app/blue-green-deploy.log"  # 로그 파일 경로를 변수로 설정
NGINX_CONFIG="/etc/nginx/nginx.conf"

# 실행 중인 blue가 있는지 확인
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps | grep spring-blue-container | grep Up)

# Nginx 컨테이너가 이미 실행 중인지 확인
EXIST_NGINX=$(docker ps --filter "name=nginx-proxy" --filter "status=running" -q)


# 현재 실행 중인 컨테이너가 Blue인지 Green인지 확인하여 Nginx 설정 변경
if [ -z "$EXIST_BLUE" ]; then
    # Blue가 실행 중이 아닌 경우 Nginx 설정을 Blue로 변경
    docker cp /etc/nginx/nginx.blue.conf nginx-proxy:/etc/nginx/nginx.conf
else
    # Green이 실행 중인 경우 Nginx 설정을 Green으로 변경
    docker cp /etc/nginx/nginx.green.conf nginx-proxy:/etc/nginx/nginx.conf
fi
docker exec nginx-proxy nginx -s reload

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

    echo "Nginx 리로드 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
#    docker exec nginx-proxy sed -i 's/spring-green-container:8080/spring-blue-container:8080/g' $NGINX_CONFIG
    docker cp /etc/nginx/nginx.blue.conf nginx-proxy:/etc/nginx/nginx.conf
    docker exec nginx-proxy nginx -s reload


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

    # Nginx 재시작 또는 설정 리로드
    echo "Nginx 리로드 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
#    docker exec nginx-proxy sed -i 's/spring-blue-container:8080/spring-green-container:8080/g' $NGINX_CONFIG
    docker cp /etc/nginx/nginx.green.conf nginx-proxy:/etc/nginx/nginx.conf
    docker exec nginx-proxy nginx -s reload


    echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml stop spring-blue
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml rm -f spring-blue
    echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  fi

fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
echo "===================== 배포 완료 =====================" >>  $DEPLOY_LOG
echo >>  $DEPLOY_LOG