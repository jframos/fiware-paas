'''
Created on 16/04/2013

@author: henar
'''
import sys
from xml.etree.ElementTree import tostring
from tools import utils


vm_fqn = 'fqn6'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
vdc = '60b4125450fc4a109f50357894ba2e28'
#vdc = '980ae4606f464bb8bc214999c596b158'
#vdc = '6571e3422ad84f7d828ce2f30373b3d4'
#tenant = '60b4125450fc4a109f50357894ba2e28'
#user = 'aalonso'
#password='aAl0ns0'
#project ='FIWARE'
user = 'henar'
password='vallelado'
project ='henarproject'
org = 'FIWARE'

domine = "130.206.80.112"
environment_name = 'justnode86'
blueprintname = 'nombreblue136'

token = utils.obtainToken (user, password, project)
print(token)
port ="8082"
#headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID':  '6571e3422ad84f7d828ce2f30373b3d4'}
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID': vdc}
print(headers)
    
print('Create a blueprint Template No tiers for nodemysql: ')
environmentDto =  utils.createEnvironmentDtoNoTiers (environment_name)
print (tostring(environmentDto))
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")

print('Create createTierNoProduct: node')
tier_name = 'nodejsjust66'
tiertDto =  utils.createTierProduct (tier_name,'nodejs','0.6.15')
print (tostring(tiertDto))


tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")


print('Get Information about the Blueprint: ' + environment_name )
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name
environmentDto = utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)

print("  OK")

environmentInstanceDto = utils.createEnvironmentInstanceDto (environmentDto, blueprintname)
print (environmentInstanceDto)
print('Deploy an environment ' + environment_name )  
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstanceDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	
  
resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+blueprintname
print('Delete Environment Instance ' + vdc+"-"+environment_name )  
task = utils.doRequestHttpOperation(domine,port,resource_delete_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)

print('Deploy an environment ' + environment_name )  
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstanceDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	
  
  resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+blueprintname
print('Delete Environment Instance ' + vdc+"-"+environment_name )  
task = utils.doRequestHttpOperation(domine,port,resource_delete_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)


