#!/bin/bash

TIME=$(date "+%Y-%m-%d %H:%M:%S")

DEPLOY_PATH=/home/ubuntu/todo/
echo "[$TIME] 배포 경로: $DEPLOY_PATH" >> /home/ubuntu/deploy.log

mkdir -p $DEPLOY_PATH
echo "[$TIME] 디렉토리 생성 완료" >> /home/ubuntu/deploy.log

BUILD_JAR=$(ls /home/ubuntu/todo/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "[$TIME] >>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

echo "[$TIME] >>> build 파일 복사" >> /home/ubuntu/deploy.log
cp $BUILD_JAR $DEPLOY_PATH 2>> /home/ubuntu/deploy_err.log

echo "[$TIME] >>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료" >> /home/ubuntu/deploy.log
sudo ps -ef | grep java | awk '{print $2}' | xargs kill -15 2>> /home/ubuntu/deploy_err.log

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "[$TIME] >>> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
echo "[$TIME] >>> $DEPLOY_JAR의 $JAR_NAME를 실행합니다" >> /home/ubuntu/deploy.log
nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>> /home/ubuntu/deploy_err.log &
