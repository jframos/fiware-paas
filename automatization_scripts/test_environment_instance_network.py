'''
Created on 16/04/2013

@author: henar
'''

from tools.enviornmentrequest import EnvironmentRequest
from tools.environment_instance import EnvironmentInstance
from tools.enviornment_instance_request import EnvironmentInstanceRequest


config = {}
execfile("sdc.conf", config)

g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['image'],config['sdc_url'])


instance_request = EnvironmentInstanceRequest (config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                                               config['vdc'],config['sdc_url'])


environment_name = 'diasss3'
blueprintname ="diass3"
network ="diass3"

print('Delete an environment Instance' + blueprintname )
#instance_request.delete_blueprint_instance (blueprintname)

print('Delete an environment ' + environment_name )
#g.delete_environment(environment_name);

print('Create a template for network: ')
g.add_environment(environment_name,'description')
print("  OK")

print('Create template: node')
tier_name = 'tiername45'
g.add_tier_environment_network(environment_name,tier_name, "nodejs=0.6.15",network)
print("  OK")

print('Get Information about the Blueprint: ' + environment_name )
env = g.get_environment(environment_name)
env.to_string()
print("  OK")

print('Deploy an environment Instance' + blueprintname )
blueprint_instance = EnvironmentInstance (blueprintname, 'description',  env, 'INIT')
instance_request.add_blueprint_instance(blueprint_instance)
print ('OK')

print('Delete an environment Instance' + blueprintname )
instance_request.delete_blueprint_instance (blueprintname)

print('Delete an environment ' + environment_name )
g.delete_environment(environment_name);

