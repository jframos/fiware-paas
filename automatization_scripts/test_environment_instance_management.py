'''
Created on 16/04/2013

@author: henar
'''
import sys
from xml.etree.ElementTree import tostring
from tools import utils

from tools.enviornmentrequest import EnvironmentRequest
from tools.environment_instance import EnvironmentInstance
from tools.enviornment_instance_request import EnvironmentInstanceRequest

if __name__ == "__main__":


# total = len(sys.argv)

    # Get the arguments list
    #cmd_args = sys.argv

    cmd_args = []
    cmd_args.append('hola')
    cmd_args.append('environment')
    cmd_args.append('blueprint')
    cmd_args.append('tier1')
    cmd_args.append('nodejs=0.6.15')

    config = {}
    execfile("sdc.conf", config)

    env_request=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                     config['vdc'],config['sdc_url'])


    instance_request = EnvironmentInstanceRequest (config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'], config['password'],
                                               config['vdc'],config['sdc_url'])


    environment_name = cmd_args[1]
    print environment_name
    blueprint_name =  cmd_args[2]
    tier_name = cmd_args[3]
    product_name = cmd_args[4]

    print('Create a blueprint Template : ' +environment_name )
    env_request.add_environment(environment_name,'description')
    print("  OK")

    print('Create tier: ' + tier_name + " with products " + product_name)
    env_request.add_tier_environment(environment_name,tier_name, product_name)
    print("  OK")

    print('Get Information about the template: ' + environment_name )
    env = env_request.get_environment(environment_name)
    env.to_string()
    print("  OK")

    print('Deploy an environment Instance' + blueprint_name )
    blueprint_instance = EnvironmentInstance (blueprint_name, 'description',  env, 'INIT')
    instance_request.add_blueprint_instance(blueprint_instance)
    print ('OK')

    print('Delete an environment Instance' + blueprint_name )
    instance_request.delete_blueprint_instance (blueprint_name)

    print('borrado del blueprint Template: ' + environment_name)
    env_request.delete_environment(environment_name)
    print("  OK")

