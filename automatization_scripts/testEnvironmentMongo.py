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
org = 'FIWARE'

domine = "130.206.80.112"
environment_name = 'mongo12'
bluerprintname = "hola"

user = 'henar'
password='vallelado'
project ='henarproject'
org = 'FIWARE'

token = utils.obtainToken (user, password, project)
print(token)
port ="8082"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID':  vdc}
print(headers)
    
print('Creating environment mongo: ')

environmentDto =  utils.createEnvironment2TiersDto (environment_name,'mongodbshard','2.2.3','mongodbconfig', '2.2.3')


resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"

utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")

print('Get Information about the blueprint Template: ')
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name

#utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)
#print("  OK")




resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"

environmentInstaneDto =utils.createEnvironmentInstanceDto (tostring(environmentDto), bluerprintname)

print('Deploy an environment ' + environment_name )  
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstaneDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	
  
resource_info_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluerprintname
print('Get Environment Instance Info. Environment ' + bluerprintname )  
data = utils.doRequestHttpOperation(domine,port,resource_info_environment_instance, 'GET',None,headers)
print("  OK")
#status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
 # print("Status not correct" + status)

resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluerprintname
print('Delete Environment Instance ' + bluerprintname)  
task = utils.doRequestHttpOperation(domine,port,resource_delete_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
  

print('Delete  blueprint Template: ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name

utils.doRequestHttpOperation(domine, port,resource_blueprint, 'DELETE',None,headers)
print("  OK")





