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
vdc = '6571e3422ad84f7d828ce2f30373b3d4'

domine = "130.206.80.112"
environment_name = 'test15'
tier_name = 'tier'

token = utils.obtainToken ()
print(token)
port ="8080"
headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  , 'Tenant-ID': '6571e3422ad84f7d828ce2f30373b3d4'}
print(headers)
    

print('Create a blueprint Template No tiers: ')
environmentDto =  utils.createEnvironmentDtoNoTiers (environment_name)
print (tostring(environmentDto))

resource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment"

utils.doRequestHttpOperation(domine, port,resource_blueprint, 'POST',tostring(environmentDto),headers)
print("  OK")

print('Create createTierNoProduct: ')
tiertDto =  utils.createTierNoProduct (tier_name)
print (tostring(tiertDto))

tierresource_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier"


utils.doRequestHttpOperation(domine, port,tierresource_blueprint, 'POST',tostring(tiertDto),headers)
print("  OK")

print('Create productReelaseDto test: ')
productReelaseDto =  utils.createProductRelease ('test','0.1')
print (tostring(productReelaseDto))

product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"

utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Create productReelaseDto tomcat: ')
productReelaseDto =  utils.createProductRelease ('tomcat','7')
print (tostring(productReelaseDto))

product_reelease_source_blueprint = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease"

utils.doRequestHttpOperation(domine, port,product_reelease_source_blueprint, 'POST',tostring(productReelaseDto),headers)
print("  OK")

print('Deleting tomcat: ')

product_reelease_removed = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease/tomcat-7"

utils.doRequestHttpOperation(domine, port,product_reelease_removed, 'DELETE',None,headers)
print("  OK")

print('Deleting test: ')

product_reelease_removed = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name+"/productRelease/test-0.1"

utils.doRequestHttpOperation(domine, port,product_reelease_removed, 'DELETE',None,headers)
print("  OK")

print('Deleting tier: ')

tier_removed = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name+"/tier/"+tier_name

utils.doRequestHttpOperation(domine, port,tier_removed, 'DELETE',None,headers)
print("  OK")

print('Deleting environment: ')

environment_removed = "/paasmanager/rest/catalog/org/FIWARE/vdc/"+vdc+"/environment/"+environment_name

utils.doRequestHttpOperation(domine, port,environment_removed, 'DELETE',None,headers)
print("  OK")






