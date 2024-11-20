#!/bin/bash

export MYSQL_URL=$MYSQL_URL
export DB_USERNAME=$DB_USERNAME
export DB_PASSWORD=$DB_PASSWORD
export REDIS_HOST=$REDIS_HOST
export REDIS_PORT=$REDIS_PORT

DEPLOY_PATH=/home/ubuntu/todo/
mkdir -p $DEPLOY_PATH

BUILD_JAR=$(ls /home/ubuntu/todo/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

echo ">>> build 파일 복사" >> /home/ubuntu/deploy.log
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료" >> /home/ubuntu/deploy.log
sudo ps -ef | grep java | awk '{print $2}' | xargs kill -15

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
echo ">>> $DEPLOY_JAR의 $JAR_NAME를 실행합니다" >> /home/ubuntu/deploy.log
nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> /home/ubuntu/deploy.log 2> /home/ubuntu/deploy_err.log &
