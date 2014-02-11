'''
Created on 16/04/2013

@author: henar
'''

from tools.enviornmentrequest import EnvironmentRequest


config = {}
execfile("sdc.conf", config)

g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['image'],config['sdc_url'])


environment_name = 'env3'
tier_name ='tiername2'

print('Create a blueprint Template: ')
g.add_abstract_environment(environment_name,'description')

print("  OK")

print('Create createTier : ')
g.add_abstract_tier_environment(environment_name,tier_name, "tomcat=6")

print("  OK")

print('Deleting tier: ')
g.delete_abstract_tier(environment_name,tier_name)
print("  OK")

print('Deleting environment: ')
g.delete_abstract_environments(environment_name)
print("  OK")







