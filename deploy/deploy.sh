#!/bin/bash


# Function
function stop(){
    sudo echo " "
    sudo echo "Stop process on port: $WAS_SERVER_PORT"
    sudo fuser -n tcp -k $SERVER_PORT

    if [ -f $TMP_PATH_NAME ]; then
        sudo rm $TMP_PATH_NAME
    fi

    sudo echo " "
}

function start(){
    sudo echo " "
    sudo nohup java -jar -Dserver.port=$WAS_SERVER_PORT $WAR_FILE /tmp 2>> /dev/null >> /dev/null &
    sudo echo "server start"
}

# Function Call
stop

start