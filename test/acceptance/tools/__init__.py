# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es


from lettuce import world
import json
import os
import sys
from tools.constants import ENVIRONMENT, LOGS_PATH

"""
Parse the JSON configuration file located in the src folder and
store the resulting dictionary in the lettuce world global variable.
"""
with open("properties.json") as config_file:
    try:
        world.config = json.load(config_file)
    except Exception, e:
        print 'Error parsing config file: %s' % (e)
        sys.exit(1)

"""
Make sure the logs path exists and create it otherwise.
"""
if not os.path.exists(world.config[ENVIRONMENT][LOGS_PATH]):
    os.makedirs(world.config[ENVIRONMENT][LOGS_PATH])
