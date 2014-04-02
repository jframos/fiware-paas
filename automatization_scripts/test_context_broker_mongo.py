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
from tools.environment_instance import EnvironmentInstance
from tools.enviornment_instance_request import EnvironmentInstanceRequest


config = {}
execfile("sdc.conf", config)

g = EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'],
    config['password'],
    config['vdc'], config['image'], config['sdc_url'])

instance_request = EnvironmentInstanceRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'],
    config['user'], config['password'],
    config['vdc'], config['sdc_url'])

environment_name = "orion3"
bluename = "blueorion3"

print('Create a blueprint Template No tiers for contextbroker: ')
g.add_environment(environment_name, 'description')

print("  OK")

print('Create createTier: tiermongoconfig')
g.add_tier_environment(environment_name, "tierconfig", "mongodbconfig=2.2.3")
print("  OK")

print('Create createTier: tiermongoshard')
g.add_tier_environment(environment_name, "tiersha58", "mongodbshard=2.2.3")
print("  OK")

print('Create createTier: tiercontextbroker')
g.add_tier_environment(environment_name, "tierzcontbrok", "mongos=2.2.3;contextbroker=1.0.0")
print("  OK")

print('Get Information about the Blueprint: ' + environment_name )
env = g.get_environment(environment_name)
env.to_string()
print("  OK")

print('Deploy an environment Instance' + bluename )
blueprint_instance = EnvironmentInstance(bluename, 'description', env, 'INIT')
instance_request.add_blueprint_instance(blueprint_instance)
print ('OK')

print ('Get tier shard')
#newEnvInst = instance_request.get_blueprint_instance(bluename)

print("  no done")

print('scale shard '  )

print("NO DONE  OK")

print('Delete an environment Instance' + bluename )
instance_request.delete_blueprint_instance(bluename)

print('borrado del blueprint Template No tiers for contextbroker: ')
g.delete_environment(environment_name)

print("  OK")
