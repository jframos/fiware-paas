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

'''
Created on 16/04/2013

@author: henar
'''

from tools.enviornmentrequest import EnvironmentRequest


config = {}
execfile("sdc.conf", config)

g = EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'],
    config['password'],
    config['vdc'], config['image'], config['sdc_url'])









# And an abstract environment has already been created with data:
#  | name   | description |
#   | nameqa | descqa      |
environment_name = 'henarprueddsba44w'
tier_name = 'nameqa'

print('Create a bluepint Template: ')
g.add_abstract_environment(environment_name, 'description')

print("  OK")

tier_name2 = 'tiernameqa5'

print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name, tier_name2, "tomcat=6", "netqa3s;netqa4s")

print('Create createTier : ')
g.add_abstract_tier_environment(environment_name, tier_name, "tomcat=6")
print("  OK")

#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | networks |
#    | tiernameqa4 | netqa1   |
tier_name1 = 'tiernameqa4'

print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name, tier_name1, "tomcat=6", "netqa3s")

#And a tier has already been added to the abstract environment "nameqa" with data:
#   | name        | networks      |
#   | tiernameqa5 | netqa1,netqa2 |


#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | products | networks |
#    | tiernameqa6 | git=1.7  | netqa1   |
tier_name3 = 'tiernameqa6'

print('Create createTier : ')
#g.add_abstract_tier_environment_network(environment_name,tier_name3, "tomcat=6", "netqa3")



#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | products                 | networks      |
#    | tiernameqa7 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
tier_name4 = 'tiernameqa7'

print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name, tier_name4, "tomcat=6", "netqa3s;netqa4s")

print("  OK")

print('Deleting environment: ')
g.delete_abstract_environments(environment_name)
print("  OK")







