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
                     config['vdc'],config['sdc_url'])


instance_request = EnvironmentInstanceRequest (config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                                               config['vdc'],config['sdc_url'])

environment_name = 'test1d19'
blueprintname ="test1d19"
network ="test1d19"

print('Delete an environment Instance' + blueprintname )
instance_request.delete_blueprint_instance (blueprintname)

print('Create a template for network: ')
g.add_environment(environment_name,'description')
print("  OK")

print('Create template: node')
tier_name = 'tiername'
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

# Network already created

print('Create a template for network: ')
g.add_environment(environment_name+"already",'description')
print("  OK")

print('Create template: node')
tier_name = 'tiername'
g.add_tier_environment_network(environment_name+"already",tier_name, "nodejs=0.6.15",network)
print("  OK")

#several networks
network2=network+ "several;anotherone"
print('Create a template for network: ')
g.add_environment(environment_name+"several",'description')
print("  OK")

print('Create template: node')
tier_name = 'tiername'
g.add_tier_environment_network(environment_name+"several",tier_name, "nodejs=0.6.15",network2)
print("  OK")


#network and subnetwork configuration
network3=network+ "other"
print('Create a template for network: ')
g.add_environment(environment_name+"other",'description')
print("  OK")

print('Create template: node')
tier_name = 'tiername'
g.add_tier_environment_network(environment_name+"other",tier_name, "nodejs=0.6.15",network3)
print("  OK")




print('borrado del blueprint Template No tiers for contextbroker: ')
g.delete_environment(environment_name)

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


