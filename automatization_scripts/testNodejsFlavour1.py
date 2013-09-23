'''
Created on 06/06/2013

@author: jmms392
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
vdc = 'ebe6d9ec7b024361b7a3882c65a57dda'
user = 'jesus'
password='susje'
project ='jesusproject'
org = 'FIWARE'


domine = "localhost"
environment_name = '07junionodejs27'
bluename = environment_name + 'bluepcontext'

token = utils.obtainToken (user, password, project)
print(token)
#port ="8082"
port ="8080"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  ,  'Tenant-ID': vdc}
print(headers)


print('Delete an environment Instance' + bluename )  
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluename
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)
  
print('Create a blueprint Template No tiers for contextbroker: ')
environmentDto =  utils.createEnvironmentDtoNoTiers (environment_name)
print (tostring(environmentDto))
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")

print('Create createTierNoProduct: nodejs1')
tier_name = environment_name  + 'nodejs1'
tiertDto =  utils.createTierNoProductFlavor (tier_name,'1')
print (tostring(tiertDto))
tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto nodejs1: ')
productReelaseDto =  utils.createProductRelease ('nodejs','0.6.15')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Create createTierNoProduct: nodejs2')
tier_name = environment_name  + 'nodejs2'
tiertDto =  utils.createTierNoProductFlavor (tier_name, '2')
print (tostring(tiertDto))
tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"
utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto nodejs2: ')
productReelaseDto =  utils.createProductRelease ('nodejs','0.6.15')
print (tostring(productReelaseDto))
product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"
utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Get Information about the Blueprint: ' + environment_name )
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name
environmentDto = utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)
print(environmentDto)
print("  OK")

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