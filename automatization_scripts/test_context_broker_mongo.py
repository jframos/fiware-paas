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

environment_name = "context37"
bluename = "blue33"




print('Create a blueprint Template No tiers for contextbroker: ')
g.add_environment(environment_name,'description')

print("  OK")

print('Create createTier: tiermongoconfig')
g.add_tier_environment(environment_name,"tiermongoconfig", "mongodbconfig=2.2.3")
print("  OK")

print('Create createTier: tiermongoshard')
g.add_tier_environment(environment_name,"monsha58", "mongodbshard=2.2.3")
print("  OK")

print('Create createTier: tiercontextbroker')
g.add_tier_environment(environment_name,"contbrok58", "mongos=2.2.3;contextbroker=1.0.0")
print("  OK")

print('Get Information about the Blueprint: ' + environment_name )
env = g.get_environment(environment_name)
env.to_string()
print("  OK")

print('Deploy an environment Instance' + bluename )
blueprint_instance = EnvironmentInstance (bluename, 'description',  env, 'INIT')
instance_request.add_blueprint_instance(blueprint_instance)
print ('OK')
  
print ('Get tier shard')
#newEnvInst = instance_request.get_blueprint_instance(bluename)

print("  no done")

print('scale shard '  )


print("NO DONE  OK")

  
print('Delete an environment Instance' + bluename )
instance_request.delete_blueprint_instance (bluename)
 
print('borrado del blueprint Template No tiers for contextbroker: ')
g.delete_environment(environment_name)

print("  OK")
