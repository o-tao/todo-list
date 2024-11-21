#!/bin/bash

{
echo "MYSQL_URL: $MYSQL_URL"
echo "DB_USERNAME: $DB_USERNAME"
echo "DB_PASSWORD: $DB_PASSWORD"
echo "REDIS_HOST: $REDIS_HOST"
echo "REDIS_PORT: $REDIS_PORT"
} >> /home/ubuntu/deploy.log


DEPLOY_PATH=/home/ubuntu/todo/
echo "배포 경로: $DEPLOY_PATH" >> /home/ubuntu/deploy.log

mkdir -p $DEPLOY_PATH
echo "디렉토리 생성 완료" >> /home/ubuntu/deploy.log

BUILD_JAR=$(ls /home/ubuntu/todo/build/libs/*.jar)
JAR_NAME=$(basename "$BUILD_JAR")
echo ">>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

echo ">>> build 파일 복사" >> /home/ubuntu/deploy.log
cp "$BUILD_JAR" "$DEPLOY_PATH"

echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료" >> /home/ubuntu/deploy.log
sudo ps -ef | grep java | awk '{print $2}' | xargs kill -15

DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
echo ">>> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
echo ">>> $DEPLOY_JAR의 $JAR_NAME를 실행합니다" >> /home/ubuntu/deploy.log
nohup java -jar -Dspring.profiles.active=prod "$DEPLOY_JAR" >> /home/ubuntu/deploy.log 2> /home/ubuntu/deploy_err.log &
