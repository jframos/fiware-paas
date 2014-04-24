#!/bin/bash
#
# Start/Stop FI-WARE Paasmanager


HOME_APP=`dirname $0`

if [[ "$HOME_APP" != /* ]]; then
  HOME_APP=`pwd`/$HOME_APP
fi

echo $HOME_APP

start() {
        echo -n "Starting PaasManager server:"
        java -Dspring.profiles.active=fiware -Djetty.home=$HOME_APP/.. -jar $HOME_APP/../lib/jetty-runner-8.1.9.v20130131.jar \
            --lib $HOME_APP/../lib \
            --config $HOME_APP/jetty.xml \
            --stop-port 28005 --stop-key secret \
            --path /paasmanager \
            --log $HOME_APP/../logs/requests_yyyy_mm_dd.log \
            --out $HOME_APP/../logs/output_yyyy_mm_dd.log \
            --port 8082 \
            $HOME_APP/../webapp/paas-manager-rest-api.war &
        ### Create the lock file ###
        echo -n "PaasManager server startup"
        echo
}

stop() {
        echo -n "Stopping PaasManager server"
        java -DSTOP.PORT=28005 -DSTOP.KEY=secret -jar $HOME_APP/../lib/start.jar --stop
        ### Now, delete the lock file ###
        echo
}

### main logic ###
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  status)
        # status paasmanager
        ;;
  restart|reload)
        stop
        start
        ;;
  *)
        echo $"Usage: $0 {start|stop|restart|reload|status}"
        exit 1
esac
exit 0

