'''
Created on 16/04/2013

@author: henar
'''
import httplib
import sys
import os
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import getDOMImplementation
from xml.etree.ElementTree import Element, SubElement, tostring
import httplib, urllib
import utils


vm_fqn = 'fqn6'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
vdc = '60b4125450fc4a109f50357894ba2e28'
#vdc = '6571e3422ad84f7d828ce2f30373b3d4'
#tenant = '60b4125450fc4a109f50357894ba2e28'
#vdc = 'ebe6d9ec7b024361b7a3882c65a57dda'
user = 'henar'
password='vallelado'
project ='henarproject'

org = 'FIWARE'

#domine = "130.206.80.112"
domine = "130.206.80.112"
environment_name = 'con58'
bluename = environment_name + 'bluepcontext28'

token = utils.obtainToken (user, password, project)
print(token)
#port ="8082"
port ="8082"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  ,  'Tenant-ID': vdc}
print(headers)


print('Create a blueprint Template No tiers for contextbroker: ')
environmentDto =  utils.createEnvironmentDtoNoTiers (environment_name)
print (tostring(environmentDto))
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")

print('Create createTierNoProduct: tiermongoconfig')
tier_name = environment_name  + 'moncfig55'
tiertDto =  utils.createTierNoProduct (tier_name)
print (tostring(tiertDto))
print("  OK")

tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto mongoconfig: ')
productReelaseDto =  utils.createProductRelease ('mongodbconfig','2.2.3')
#productReelaseDto =  utils.createProductRelease ('tomcat','7')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")


print('Create createTierNoProduct: tiermongoshard')
tier_name2 = environment_name  +'monsha58'
tiertDto =  utils.createTierNoProduct (tier_name2)
print (tostring(tiertDto))
tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto mongoshard: ')
productReelaseDto =  utils.createProductRelease ('mongodbshard','2.2.3')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name2+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Create createTierNoProduct: tiercontextbroker')
tier_name = environment_name  + 'contbrok58'
tiertDto =  utils.createTierNoProduct (tier_name)
print (tostring(tiertDto))

tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto mongos: ')
productReelaseDto =  utils.createProductRelease ('mongos','2.2.3')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Create productReelaseDto contextbroker: ')

productReelaseDto =  utils.createProductRelease ('contextbroker','1.0.0')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Get Information about the Blueprint: ' + environment_name )
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name
environmentDto = utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)


environmentInstanceDto = utils.createEnvironmentInstanceDto (environmentDto, bluename)
print (tostring(environmentInstanceDto))
print("  OK")


print('Deploy an environment Instance' + bluename )  
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstanceDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	
  
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
