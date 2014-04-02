'''
Created on 16/04/2013

@author: henar
'''

from tools.enviornmentrequest import EnvironmentRequest


config = {}
execfile("sdc.conf", config)

g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['image'],config['sdc_url'])









   # And an abstract environment has already been created with data:
      #  | name   | description |
    #   | nameqa | descqa      |
environment_name = 'henarprueddsba44w'
tier_name ='nameqa'

print('Create a bluepint Template: ')
g.add_abstract_environment(environment_name,'description')

print("  OK")

tier_name2 ='tiernameqa5'


print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name,tier_name2, "tomcat=6", "netqa3s;netqa4s")

print('Create createTier : ')
g.add_abstract_tier_environment(environment_name,tier_name, "tomcat=6")
print("  OK")

#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | networks |
#    | tiernameqa4 | netqa1   |
tier_name1 ='tiernameqa4'


print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name,tier_name1, "tomcat=6", "netqa3s")

#And a tier has already been added to the abstract environment "nameqa" with data:
 #   | name        | networks      |
 #   | tiernameqa5 | netqa1,netqa2 |


#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | products | networks |
#    | tiernameqa6 | git=1.7  | netqa1   |
tier_name3 ='tiernameqa6'


print('Create createTier : ')
#g.add_abstract_tier_environment_network(environment_name,tier_name3, "tomcat=6", "netqa3")



#And a tier has already been added to the abstract environment "nameqa" with data:
#    | name        | products                 | networks      |
#    | tiernameqa7 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
tier_name4 ='tiernameqa7'


print('Create createTier : ')
g.add_abstract_tier_environment_network(environment_name,tier_name4, "tomcat=6", "netqa3s;netqa4s")

print("  OK")




print('Deleting environment: ')
g.delete_abstract_environments(environment_name)
print("  OK")







