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


user = 'henar'
password='vallelado'
project ='henarproject'
org = 'FIWARE'



vm_fqn = 'fqn6'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
vdc = '60b4125450fc4a109f50357894ba2e28'
vdc2= '6571e3422ad84f7d828ce2f30373b3d4'

domine = "130.206.80.112"
environment_name = 'test'

cloneenvironment = 'nametest14'

token = utils.obtainToken (user, password, project)
print(token)
port ="8082"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID': vdc}
print(headers)
    
print('Get Abstract Blueprint in the Blueprint Repository: ')
resource_abstract_blueprint = "/paasmanager/rest/catalog/org/FIWARE/environment"
data1 = utils.doRequestHttpOperation(domine, port, resource_abstract_blueprint, 'GET',None,headers)
print("  OK")
print(data1)
dom = parseString(data1)
try:
	environment = (dom.getElementsByTagName('environmentDto'))[0]
	environment_name = environment.firstChild
	print('First environment_name in the environment catalogue')
	print(environment_name)

except:
	print ("Error in the processing enviroment")
	sys.exit(1)

print('Get Blueprints in the Blueprint Repository: ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc2+"/environment"
data1 = utils.doRequestHttpOperation(domine, port,resource_blueprint, 'GET',None,headers)
print(data1)
print("  OK")

print('Cloning a blueprint template')
print('Get abstract Blueprints : ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/environment/2testing"
data1 = utils.doRequestHttpOperation(domine, port,resource_blueprint, 'GET',None,headers)
print(data1)
clonedEnvironment = utils.createCloneEnviornmentDto (data1, cloneenvironment, 'description')


print('Create a blueprint Template: ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"
print clonedEnvironment
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',clonedEnvironment,headers)
print("  OK")

print('Update a tier from the Template: ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+cloneenvironment+"/tier/tomcat7postgres8Tier"
print resource_blueprint
tierDto = utils.doRequestHttpOperation(domine, port,resource_blueprint, 'GET',None,headers)
print(tierDto)
print ('new tier ')
newtier= utils.changeTier (tierDto, 'henar', 'floatingip')
print(newtier)
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+cloneenvironment+"/tier/tomcat7postgres8Tier"
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'PUT',newtier,headers)
print("  OK")

print('Get Information about the blueprint Template: ')
get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+cloneenvironment
data=utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)
print(data)
print("  OK")


print('Delete  blueprint Template: ')
resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+cloneenvironment
utils.doRequestHttpOperation(domine, port,resource_blueprint, 'DELETE',None,headers)
print("  OK")

#print('Create a blueprint Template No tiers: ')
#environmentDto =  utils.createEnvironmentDtoNoTiers ('testenvi19')
#print (tostring(environmentDto))

#resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"

#utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
#print("  OK")

#tiertDto =  utils.createTier ('dd','99')
#print (tostring(tiertDto))

#tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"tier"

#utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
#print("  OK")


#print('Create a blueprint Template No FIWARE: ')
#environmentDto =  utils.createNoFIWAREEnvironmentDto ('testenvi18',product_name, product_version)
#print (tostring(environmentDto))

#resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"

#utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
#print("  OK")

#print('Get Information about the blueprint Template No FIWARE: ')
#get_resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/testenvi18"

#utils.doRequestHttpOperation(domine, port,get_resource_blueprint, 'GET',None,headers)
#print("  OK")

#print('Delete  blueprint Template No FIWARE: ')
#resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/testenvi18"

#utils.doRequestHttpOperation(domine, port,resource_blueprint, 'DELETE',None,headers)
#print("  OK")





