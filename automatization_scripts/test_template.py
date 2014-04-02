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
    config['vdc'], config['sdc_url'])

environment_name = 'enviornmentname3'
tier_name = 'tiername2'

print('Create a blueprint Template No tiers: ')
g.add_environment(environment_name, 'description')

print("  OK")

print('Create createTier No product: ')
g.add_tier_environment(environment_name, tier_name, None)

print("  OK")
print('Create productReelaseDto test: ')
g.add_product_to_tier(environment_name, tier_name, "test=0.1")
print("  OK")

print('Create productReelaseDto tomcat: ')
g.add_product_to_tier(environment_name, tier_name, "tomcat=6")
print("  OK")

print('Deleting tier: ')
g.delete_tier(environment_name, tier_name)
print("  OK")

print('Deleting environment: ')
g.delete_environment(environment_name)
print("  OK")







