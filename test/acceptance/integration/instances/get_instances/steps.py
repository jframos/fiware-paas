# -*- coding: utf-8 -*-
from lettuce import step, world
from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http, environment_request, environment_instance_request
from tools.tier import Tier
from tools.environment_instance import EnvironmentInstance
import json

dataset_utils = DatasetUtils()

# Auxiliary constants used in .feature files or interfaces
NAME = "name"
DESCRIPTION = "description"
PRODUCTS = "products"
NETWORKS = "networks"


@step(u'the paas manager is up and properly configured')
def the_paas_manager_is_up_and_properly_configured(step):
    pass  # Nothing to do here, the set up should be done by external means


@step(u'a list of tiers has been defined with data:')
def a_list_of_tiers_has_been_defined_with_data(step):
    world.tiers = []
    for row in step.hashes:
        data = dataset_utils.prepare_data(row)
        tier = Tier(data.get(NAME), world.config['paas']['tier_image'])
        tier.parse_and_add_products(data.get(PRODUCTS))
        tier.parse_and_add_networks(data.get(NETWORKS))
        world.tiers.append(tier)


@step(u'an environment has already been created with data:')
def an_environment_has_already_been_created_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'an environment has already been created with the previous tiers and data:')
def an_environment_has_already_been_created_with_the_previous_tiers_and_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_environment(data.get(NAME), data.get(DESCRIPTION), world.tiers)
    assert world.response.status == 204, \
    "Wrong status code received creating environment: %d. Expected: %d. Body content: %s" \
    % (world.response.status, 204, world.response.read())


@step(u'an instance of the environment "([^"]*)" has already been created using data:')
def an_instance_of_the_environment_has_already_been_created_using_data(step, env_name):
    # First, send the request to get the environment on which the instance will be based
    env_name = dataset_utils.generate_fixed_length_param(env_name)
    world.env_requests.get_environment(env_name)
    assert world.response.status == 200, \
    "Wrong status code received getting environment: %d. Expected: %d. Body content: %s" \
    % (world.response.status, 200, world.response.read())
    environment = environment_request.process_environment(json.loads(world.response.read()))
    # Then, create the instance
    data = dataset_utils.prepare_data(step.hashes[0])
    instance = EnvironmentInstance(data.get(NAME), data.get(DESCRIPTION), environment)
    world.inst_requests.add_instance(instance)


@step(u'I request the list of existing instances')
def i_request_the_list_of_existing_instances(step):
    world.inst_requests.get_instances()


@step(u'I receive an? "([^"]*)" response with "(\d)" items? in the list')
def i_receive_a_response_of_type_with_items(step, response_type, items_number):
    status_code = http.status_codes[response_type]
    environment_instance_request.check_get_instances_response(world.response, status_code, int(items_number))


@step(u'I receive an? "([^"]*)" response with no content')
def i_receive_a_response_of_type_with_no_content(step, response_type):
    status_code = http.status_codes[response_type]
    environment_instance_request.check_get_instances_response(world.response, status_code, 0)


@step(u'there is an instance in the list with "(\d)" tiers? and data:')
def there_is_an_instance_in_the_list_based_on_environment_with_tiers_and_data(step, tiers_number):
    data = dataset_utils.prepare_data(step.hashes[0])
    environment_instance_request.check_instance_in_list(world.response.instances, data.get(NAME),
                                                        data.get(DESCRIPTION), int(tiers_number))
