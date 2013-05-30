play stage

REMOTE_BIN=/home/kender/
REMOTE_DEP=/home/kender/nostra/

SERVER=innovation-ml.janrain.com

zip -r target/staged.zip target/staged target/start

scp target/staged.zip $SERVER:$REMOTE_DEP

START="start-stop-daemon --pidfile $REMOTE_DEP/RUNNING_PID --chdir $REMOTE_DEP/target --exec $REMOTE_DEP/target/start  --verbose --background --oknodo --start -- -Dhttp.port=80"
STOP="start-stop-daemon --pidfile $REMOTE_DEP/RUNNING_PID --verbose --stop --oknodo --retry 5 --"
CMD1="echo '$START' > start; chmod +x start"
CMD2="echo '$STOP' > stop; chmod +x stop"
CMD3="cd $REMOTE_DEP ; sudo rm -rf target/staged ; sudo unzip -o staged.zip; sudo $REMOTE_BIN/stop ; sudo $REMOTE_BIN/start"

ssh -t $SERVER "$CMD1;$CMD2;$CMD3"
