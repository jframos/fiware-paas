'''
Created on 16/04/2013

@author: henar
'''

from environment_request import EnvironmentRequest
from environment_instance import EnvironmentInstance
from environment_instance_request import EnvironmentInstanceRequest

config = {}
execfile("tools/sdc.conf", config)

g = EnvironmentRequest(config['keystone_url'],
                       config['paasmanager_url'],
                       config['tenant'],
                       config['user'],
                       config['password'],
                       config['vdc'],
                       config['image'],
                       config['sdc_url'])

instance_request = EnvironmentInstanceRequest(config['keystone_url'],
                                              config['paasmanager_url'],
                                              config['tenant'],
                                              config['user'],
                                              config['password'],
                                              config['vdc'],
                                              config['sdc_url'])

env_name = 'env-name'
env_desc = 'env-desc'
inst_name = 'inst-name'
inst_desc = 'inst-desc'
tier_name = 'git-tier'

print('Create a blueprint template: ' + env_name)
g.add_environment(env_name, env_desc)
print("  OK")

print('Create a tier with name: ' + tier_name)
g.add_tier_environment(env_name, tier_name, "git=1.7")
print("  OK")

print('Get Information about the blueprint template: ' + env_name)
env = g.get_environment(env_name)
print("  OK")

print('Deploy a blueprint instance: ' + inst_name)
blueprint_instance = EnvironmentInstance(inst_name, inst_desc, env, 'INIT')
instance_request.add_blueprint_instance(blueprint_instance)
print("  OK")

print('Delete the blueprint instance: ' + inst_name)
instance_request.delete_blueprint_instance(inst_name)
print("  OK")

print('Delete the blueprint template: ' + env_name)
g.delete_environment(env_name)
print("  OK")
