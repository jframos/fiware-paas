'''
Created on 16/04/2013

@author: henar
'''
from xml.etree.ElementTree import tostring
from tools import utils


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

#resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
#environmentDto =  utils.createEnvironmentDto (environment_name,product_name, product_version)
#print('Deploy an environment ' + environment_name )  
#task = utils.doRequestHttpOperation(domine,resource_environment_instance, 'POST',tostring(environmentDto))
#status = utils.processTask (domine,task)
#print ("  " + status)

resource_application_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name+"/applicationInstance"

applicationDto =  utils.createApplicationDto(application_name, application_version, product_name, product_version, artifact_name)
print (tostring(applicationDto))
print('Deploy an application ' + application_name )  
task = utils.doRequestHttpOperation(domine,resource_application_instance, 'POST',tostring(applicationDto))
status = utils.processTask (domine,task)
print ("  " + status)


resource_info_application_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name+"/applicationInstance/"+application_name+"-"+vdc+"-"+environment_name
print('Get Application Instance Info. Environment ' + application_name  + ' ' + resource_info_application_instance)  
data = utils.doRequestHttpOperation(domine,resource_info_application_instance, 'GET',None)
print("  OK")
#status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
 # print("Status not correct" + status)

resource_delete_application_instance ="/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance/"+vdc+"-"+environment_name+"/applicationInstance/"+application_name+"-"+vdc+"-"+environment_name
print('Delete Application Instance ' + application_name+ ' ' + resource_delete_application_instance)  
task = utils.doRequestHttpOperation(domine,resource_delete_application_instance, 'DELETE',None)
status = utils.processTask (domine,task)
print ("  " + status)
data = utils.doRequestHttpOperation(domine,resource_info_application_instance, 'GET',None)

#if  status != 'UNINSTALLED':
 # print("Status not correct" + statusProduct)






    

    
    

    

    
    
 


