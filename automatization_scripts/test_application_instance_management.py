'''
Created on 16/04/2013

@author: henar
'''
from xml.etree.ElementTree import tostring
from tools import utils
from tools.enviornmentrequest import EnvironmentRequest
from tools.environment_instance import EnvironmentInstance
from tools.application_instance import ApplicationInstance
from tools.application_instance import Artifact
from tools.enviornment_instance_request import EnvironmentInstanceRequest
from xml.etree.ElementTree import tostring


domine = "130.206.80.112"

vm_fqn = 'fqn9'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
vdc = 'test3'
org = 'FIWARE'
environment_name = 'environment_name3'
application_name = 'application_name6'
application_version ='1.0'
artifact_name='dd'

config = {}
execfile("sdc.conf", config)

g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['image'],config['sdc_url'])


instance_request = EnvironmentInstanceRequest (config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                                               config['vdc'],config['sdc_url'])

environment_name = 'git32'
blueprintname = 's62'
application = 'app36'

print('Create a blueprint Template tomcat/mysql: ')
#g.add_environment(environment_name,'description')

tier_name = 'tomcat'
#g.add_tier_environment(environment_name,tier_name, "tomcat=6")
tier_name = 'mysql'
#g.add_tier_environment(environment_name,tier_name, "mysql=1.2.4")
#print("  OK")


env = g.get_environment(environment_name)

print('Deploy an environment Instance' + blueprintname )
blueprint_instance = EnvironmentInstance (blueprintname, 'description',  env, 'INIT')
#instance_request.add_blueprint_instance(blueprint_instance)
print ('OK')

print('Deploy an application Instance' + application )

application_instance = ApplicationInstance (application, "2.0")
artifact = Artifact ("mywar", "path", "tomcat=6")
application_instance.add_artifact(artifact)

print tostring(application_instance.to_xml())
instance_request.add_application_instance(blueprintname,application_instance )
print ('OK')

#resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
#environmentDto =  utils.createEnvironmentDto (environment_name,product_name, product_version)
#print('Deploy an environment ' + environment_name )  
#task = utils.doRequestHttpOperation(domine,resource_environment_instance, 'POST',tostring(environmentDto))
#status = utils.processTask (domine,task)
#print ("  " + status)
