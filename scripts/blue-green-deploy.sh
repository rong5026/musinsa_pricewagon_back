#!/bin/bash

## Blue/Green 배포 스크립트

# 작업 디렉토리 설정
cd /home/hong/app/pricewagon-blue-green

DOCKER_APP_NAME=pricewagon
DEPLOY_LOG="/home/hong/app/blue-green-deploy.log"  # 로그 파일 경로를 변수로 설정
NGINX_BLUE_CONFIG="/home/hong/app/pricewagon-blue-green/nginx.blue.conf"  # Blue용 Nginx 설정 파일
NGINX_GREEN_CONFIG="/home/hong/app/pricewagon-blue-green/nginx.green.conf"  # Green용 Nginx 설정 파일
NGINX_CONFIG="/etc/nginx/nginx.conf"  # 컨테이너 내부의 Nginx 설정 파일 경로

# 실행 중인 blue가 있는지 확인
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml ps | grep spring-blue-container | grep Up)

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
    # Blue 컨테이너가 실행 중이므로 Blue용 Nginx 설정 파일을 복사
    echo "Nginx 리로드 시작일자 :  $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    if ! ERROR_LOG=$(docker cp $NGINX_BLUE_CONFIG nginx-proxy:$NGINX_CONFIG 2>&1); then
        echo "Nginx 설정 복사 실패: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    fi
    if ! ERROR_LOG=$(docker exec nginx-proxy nginx -s reload 2>&1); then
        echo "Nginx 리로드 실패:  $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    fi

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
    # Blue 컨테이너가 실행 중이므로 Blue용 Nginx 설정 파일을 복사
    echo "Nginx 리로드 시작일자 :  $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    if ! ERROR_LOG=$(docker cp $NGINX_GREEN_CONFIG nginx-proxy:$NGINX_CONFIG 2>&1); then
        echo "Nginx 설정 복사 실패:  $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    fi
    if ! ERROR_LOG=$(docker exec nginx-proxy nginx -s reload 2>&1); then
        echo "Nginx 리로드 실패:  $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    fi

    echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml stop spring-blue
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.yml rm -f spring-blue
    echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  fi

fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
echo "===================== 배포 완료 =====================" >>  $DEPLOY_LOG
echo >>  $DEPLOY_LOG