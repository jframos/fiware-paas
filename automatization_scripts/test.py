__author__ = 'henar'
import json

test = "{\"@href\":\"http://130.206.80.112:8082/paasmanager/rest/vdc/60b4125450fc4a109f50357894ba2e28/task/1290\"," \
       "\"@startTime\":\"2013-09-27T13:38:10.753+02:00\",\"@status\":\"RUNNING\",\"description\":\"Create environment blue14\"," \
       "\"vdc\":\"60b4125450fc4a109f50357894ba2e28\",\"environment\":\"blue14\"}"
objs = json.loads(test)
print objs["@href"]

