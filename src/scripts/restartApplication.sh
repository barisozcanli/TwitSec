#!/bin/bash
_now=$(date +"%s_%m_%d_%Y")
_file="../logs/twitsec_$_now.log"
echo "cd and mvn start"
cd /root/workspace/TwitSec-API
mvn install

nohup mvn spring-boot:run > "$_file" 2>&1 </dev/null &
echo "mvn ended"