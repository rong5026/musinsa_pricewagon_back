#!/bin/bash

DEPLOY_LOG="/home/hong/app/ssl.log"  # 로그 파일 경로를 변수로 설정

# 도커 컴포즈 설치 여부 확인
if ! [ -x "$(command -v docker-compose)" ]; then
  echo 'Error: docker-compose is not installed.' >&2 >> $DEPLOY_LOG
  exit 1
fi

domains=("hong-nuri.shop" "www.hong-nuri.shop")
rsa_key_size=4096
data_path="./data/certbot"
email="rong5026@naver.com" # Adding a valid address is strongly recommended
staging=1 # Set to 1 if you're testing your setup to avoid hitting request limits

# 기존 뎅리터 확인
if [ -d "$data_path" ]; then
  read -p "기존 데이터가 있습니다. 인증서를 다시 발급받으시겠습니까? (y/N) " decision
  if [ "$decision" != "Y" ] && [ "$decision" != "y" ]; then
    exit
  fi
fi


if [ ! -e "$data_path/conf/options-ssl-nginx.conf" ] || [ ! -e "$data_path/conf/ssl-dhparams.pem" ]; then
  echo "### 권장 TLS 설정 다운로드 중 ..." >> $DEPLOY_LOG
  mkdir -p "$data_path/conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf > "$data_path/conf/options-ssl-nginx.conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem > "$data_path/conf/ssl-dhparams.pem"
  echo
fi

echo "### 더미 인증서 생성 중 ..." >> $DEPLOY_LOG
path="/etc/letsencrypt/live/$domains"
mkdir -p "$data_path/conf/live/$domains"
docker-compose run --rm --entrypoint "\
  openssl req -x509 -nodes -newkey rsa:$rsa_key_size -days 1\
    -keyout '$path/privkey.pem' \
    -out '$path/fullchain.pem' \
    -subj '/CN=localhost'" certbot
echo

echo "### Nginx 시작 중 ..." >> $DEPLOY_LOG
docker-compose up --force-recreate -d nginx
echo

echo "### 더미 인증서 삭제 중 ..." >> $DEPLOY_LOG
docker-compose run --rm --entrypoint "\
  rm -Rf /etc/letsencrypt/live/$domains && \
  rm -Rf /etc/letsencrypt/archive/$domains && \
  rm -Rf /etc/letsencrypt/renewal/$domains.conf" certbot
echo


echo "### Let's Encrypt 인증서 요청 중 ..." >> $DEPLOY_LOG
#Join $domains to -d args
domain_args=""
for domain in "${domains[@]}"; do
  domain_args="$domain_args -d $domain"
done

# Select appropriate email arg
case "$email" in
  "") email_arg="--register-unsafely-without-email" ;;
  *) email_arg="--email $email" ;;
esac

# Enable staging mode if needed
if [ $staging != "0" ]; then staging_arg="--staging"; fi

docker-compose run --rm --entrypoint "\
  certbot certonly --webroot -w /var/www/certbot \
    $staging_arg \
    $email_arg \
    $domain_args \
    --rsa-key-size $rsa_key_size \
    --agree-tos \
    --force-renewal" certbot
echo

echo "### Nginx 재시작 중 ...">> $DEPLOY_LOG

docker-compose exec nginx nginx -s reload