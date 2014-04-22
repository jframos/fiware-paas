#!/bin/bash
#
# Start/Stop FI-WARE Paasmanager



start() {
        echo -n "Starting PaasManager server:"
        java -Dspring.profiles.active=fiware -Djetty.home=. -jar lib/jetty-runner-8.1.9.v20130131.jar --lib lib --config bin/jetty.xml --stop-port 28005 --stop-key secret --path /paasmanager webapp/paas-manager-rest-api.war &
        ### Create the lock file ###
        echo -n "PaasManager server startup"
        echo
}

stop() {
        echo -n "Stopping PaasManager server"
        java -DSTOP.PORT=28005 -DSTOP.KEY=secret -jar lib/start.jar --stop
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

