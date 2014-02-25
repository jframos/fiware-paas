# -*- coding: utf-8 -*-
from lettuce import step, world
from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http
from tools import environment_request
from tools.tier import Tier

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


@step(u'an abstract environment has already been created with data:')
def an_abstract_environment_has_already_been_created_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_abstract_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'an abstract environment has already been created with the previous tiers and data:')
def an_abstract_environment_has_already_been_created_with_the_previous_tiers_and_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_abstract_environment(data.get(NAME), data.get(DESCRIPTION), world.tiers)


@step(u'there is no abstract environment with name "([^"]*)" already created')
def there_is_no_abstract_environment_with_name_already_created(step, name):
    world.env_requests.delete_abstract_environment(name)  # Just in case it exists


@step(u'I request the details of the abstract environment with name "([^"]*)"')
def i_request_the_list_of_existing_abstract_environments(step, name):
    name = dataset_utils.generate_fixed_length_param(name)
    world.env_requests.get_abstract_environment(name)


@step(u'I receive an? "([^"]*)" response with data:')
def i_receive_a_response_of_type_with_data(step, response_type):
    status_code = http.status_codes[response_type]
    data = dataset_utils.prepare_data(step.hashes[0])
    environment_request.check_get_environment_response(world.response, status_code,
                                                       data.get(NAME), data.get(DESCRIPTION))


@step(u'I receive an? "([^"]*)" response with the previous tiers and data:')
def i_receive_a_response_of_type_with_the_previous_tiers_and_data(step, response_type):
    status_code = http.status_codes[response_type]
    data = dataset_utils.prepare_data(step.hashes[0])
    environment_request.check_get_environment_response(world.response, status_code,
                                                       data.get(NAME), data.get(DESCRIPTION),
                                                       world.tiers)


@step(u'I receive an? "([^"]*)" response$')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    environment_request.check_get_environment_response(world.response, status_code)
