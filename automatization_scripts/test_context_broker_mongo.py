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


environment_name = "context_broker_temp1"
bluename = "blue_context"


g.delete_environment(environment_name)
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

instance_request = EnvironmentInstanceRequest (config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                                               config['vdc'],config['sdc_url'])
print('Deploy an environment Instance' + bluename )
blueprint_instance = EnvironmentInstance (bluename, 'description', env)
instance_request.add_blueprint_instance(blueprint_instance)
print ('OK')
  
print ('Get tier shard')
get_tier_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name2
tierDto = utils.doRequestHttpOperation(domine, port,get_tier_resource_blueprint, 'GET',None,headers)
print (tierDto)
print("  OK")

print('scale shard '  )  
resource_tier_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluename+"/tierInstance"
print (resource_tier_instance)
task = utils.doRequestHttpOperation(domine,port,resource_tier_instance, 'POST',tierDto,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
 sys.exit(1)

  
print('Delete an environment Instance' + bluename )  
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluename
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)
 
print('borrado del blueprint Template No tiers for contextbroker: ')
environmentDto =  utils.createEnvironmentDtoNoTiers (environment_name)
print (tostring(environmentDto))
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'DELETE',None,headers)
print("  OK")
