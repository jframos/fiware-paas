'''
Created on 04/06/2013

@author: jmms392
'''
import sys
from xml.etree.ElementTree import tostring
from tools import utils


domine = "localhost"

#vm_fqn = 'fqn25'
#vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
product_name2 = 'test'
product_version2 = '0.1'
product_name3 = 'test'
product_version3 = '0.1'
vdc = 'ebe6d9ec7b024361b7a3882c65a57dda'
org = 'FIWARE'
environment_name = 'envname06junio14'
tier_name='tiername-' + environment_name
bluename = environment_name + 'bluepcontext7'

project='jesusproject'
user='jesus'
password='susje'

token = utils.obtainToken (user, password, project)
print ("Token:  " + token)

port ="8080"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID': 'ebe6d9ec7b024361b7a3882c65a57dda'}

#Creando Environment
print('Inserting an environment in the catalog')
environmentDto =  utils.createEnvironmentDto (environment_name,tier_name, product_name, product_version)
print (tostring(environmentDto))
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")
print ("Token:  " + token)

#Getting Environment
print('Getting the environment: ' + environment_name )
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name
environmentDto = utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)
print (environmentDto)
print("  OK")
print ("Token:  " + token)

#Creating an EnvironmentInstance
print('Creating an environmentInstance');
resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
environmentInstanceDto = utils.createEnvironmentInstanceDto (environmentDto, bluename)
print (tostring(environmentInstanceDto))
print('Deploy an environment ' + bluename )  
task = utils.doRequestHttpOperation(domine,port,resource_environment_instance, 'POST',tostring(environmentInstanceDto),headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
if status == 'ERROR':
  sys.exit(1)	
print("  OK")
print ("Token:  " + token)


resource_info_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluename
print('Get Environment Instance Info. EnvironmentInstance: ' + bluename )  
data = utils.doRequestHttpOperation(domine,port,resource_info_environment_instance, 'GET',None,headers)
print(data)
print("  OK")
print ("Token:  " + token)
#status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
 # print("Status not correct" + status)

#resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name
resource_delete_environment_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+bluename
print('Delete Environment Instance: ' + bluename )  
task = utils.doRequestHttpOperation(domine,port,resource_delete_environment_instance, 'DELETE',None,headers)
status = utils.processTask (domine,port,headers,task)
print ("  " + status)
print ("Token:  " + token)
#data = utils.doRequestHttpOperation(domine,resource_info_environment_instance, 'GET',None)
#if  status != 'UNINSTALLED':
 # print("Status not correct" + statusProduct)
