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


domine = "130.206.80.112"

vm_fqn = 'fqn25'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
vdc = '6571e3422ad84f7d828ce2f30373b3d4'
org = 'FIWARE'
environment_name = 'name40d157'
domine = "130.206.80.112"

user = 'henar'
password='vallelado'
project ='henarproject'
org = 'FIWARE'

token = utils.obtainToken (user, password, project)

port ="8080"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID': '6571e3422ad84f7d828ce2f30373b3d4'}


resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
environmentDto =  utils.createEnvironmentDto (environment_name,'tierhenar', product_name, product_version)

environmentInstanceDto =  utils.createEnvironmentInstanceDto (tostring(environmentDto), 'testenviroment')

print('Deploy an environment ' + environment_name )  
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstanceDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	



resource_info_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name
print('Get Environment Instance Info. Environment ' + environment_name )  
data = utils.doRequestHttpOperation(domine,port,resource_info_environment_instance, 'GET',None,headers)
print("  OK")
#status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
 # print("Status not correct" + status)

resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name
print('Delete Environment Instance ' + environment_name )  
task = utils.doRequestHttpOperation(domine,port,resource_delete_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
#data = utils.doRequestHttpOperation(domine,resource_info_environment_instance, 'GET',None)

#if  status != 'UNINSTALLED':
 # print("Status not correct" + statusProduct)






    

    
    

    

    
    
 


