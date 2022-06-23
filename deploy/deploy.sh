##!/bin/bash
#echo "> 현재 구동중인 Set 확인"
#CURRENT_PROFILE=$(curl -s http://localhost/profile/server)
#echo "> $CURRENT_PROFILE"
#
## 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있음
#
#if [ $CURRENT_PROFILE == set1 ]
#then
#  IDLE_PROFILE=set2
#  IDLE_PORT=8083
#elif [ $CURRENT_PROFILE == set2 ]
#then
#  IDLE_PROFILE=set1
#  IDLE_PORT=8082
#else
#  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
#  echo "> set1을 할당합니다. IDLE_PROFILE: set1"
#  IDLE_PROFILE=set1
#  IDLE_PORT=8082
#fi
#
## 현재 구동중인 어플리케이션 찾기
#if [ $IDLE_PORT == 8082 ]
#then
#  CURRNET_PORT=8083
#else
#  CURRNET_PORT=8082
#fi
#
## Function stop()
#function stop(){
#    sudo echo " "
#    sudo echo "Stop process on port: $CURRNET_PORT"
#    sudo fuser -n tcp -k $CURRNET_PORT
#
#    if [ -f $TMP_PATH_NAME ]; then
#        sudo rm $TMP_PATH_NAME
#    fi
#
#    sudo echo " "
#}
## Function start()
#function start(){
#    sudo echo " "
#    sudo nohup java -jar -Dserver.port=$IDLE_PORT $WAR_FILE /tmp 2>> /dev/null >> /dev/null &
#    sudo echo "server start"
#}
#
## Function Call
#stop
#start
#
## switch proxy
#function switch_proxy() {
#    IDLE_PORT=$(find_idle_port)
#
#    echo "> 전환할Port: $IDLE_PORT"
#    echo "> Port 전환"
#    echo "set /$service_url http://127.0.0.1:${IDLE_PORT};" |
#    sudo tee /etc/niginx/conf.d/service-url.inc
#
#    echo "> 엔진엑스 Reload"
#    sudo service nginx reload
#}
#
## check
#echo "> $IDLE_PROFILE 10초 후 Health check 시작"
#echo "> curl -s http://localhost:$IDLE_PORT/profile/server"
#sleep 10
#
#for retry_count in {1..10}
#do
#  response=$(curl -s http://localhost:$IDLE_PORT/profile/server)
#  up_count=$(echo $response | grep 'real' | wc -l)
#
#  if [ $up_count -ge 1 ]
#  then # $up_count >= 1 ("real" 문자열이 있는지 검증)
#      echo "> Health check 성공"
#      switch_proxy
#      break
#  else
#      echo "> Health check의 응답을 알 수 없거나 혹은 status가 real이 아닙니다."
#      echo "> Health check: ${response}"
#  fi
#
#  if [ $retry_count -eq 10 ]
#  then
#    echo "> Health check 실패. "
#    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
#    exit 1
#  fi
#
#  echo "> Health check 연결 실패. 재시도..."
#  sleep 10
#done



function stop(){
    sudo echo " "
    sudo echo "Stop process on port: $WAS_SERVER_PORT"
    sudo fuser -n tcp -k $WAS_SERVER_PORT

    if [ -f $TMP_PATH_NAME ]; then
        sudo rm $TMP_PATH_NAME
    fi

    sudo echo " "
}

# Function start()
function start(){
    sudo echo " "
    sudo nohup java -jar -Dserver.port=$WAS_SERVER_PORT $WAR_FILE /tmp 2>> /dev/null >> /dev/null &
    sudo echo "server start"
}

## Function Call
stop
start
