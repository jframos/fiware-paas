'''
Created on 16/04/2013

@author: henar
'''

from tools.enviornmentrequest import EnvironmentRequest


config = {}
execfile("sdc.conf", config)

g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['sdc_url'])


environment_name = 'enviornmentname3'
tier_name ='tiername2'


print('Create a blueprint Template No tiers: ')
g.add_environment(environment_name,'description')

print("  OK")

print('Create createTier No product: ')
g.add_tier_environment(environment_name,tier_name, None)

print("  OK")
print('Create productReelaseDto test: ')
g.add_product_to_tier(environment_name,tier_name, "test=0.1")
print("  OK")

print('Create productReelaseDto tomcat: ')
g.add_product_to_tier(environment_name,tier_name, "tomcat=6")
print("  OK")

print('Deleting tier: ')
g.delete_tier(environment_name,tier_name)
print("  OK")

print('Deleting environment: ')
g.delete_environment(environment_name)
print("  OK")







