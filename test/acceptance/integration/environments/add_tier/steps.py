# -*- coding: utf-8 -*-
from lettuce import step, world
from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http
from tools import tier
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


@step(u'an environment has already been created with data:')
def an_environment_has_already_been_created_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'a tier has already been added to the environment "([^"]*)" with data:')
def a_tier_has_already_been_added_to_environment_with_data(step, env_name):
    i_request_the_addition_of_a_tier_to_environment_with_data(step, env_name)


@step(u'there is no environment with name "([^"]*)" already created')
def there_is_no_environment_with_name_already_created(step, name):
    world.env_requests.delete_environment(name)  # Just in case it exists


@step(u'I request the addition of a tier to the environment "([^"]*)" with data:')
def i_request_the_addition_of_a_tier_to_environment_with_data(step, env_name):
    data = dataset_utils.prepare_data(step.hashes[0])
    tier = Tier(data.get(NAME), world.config['paas']['image'])
    tier.parse_and_add_products(data.get(PRODUCTS))
    tier.parse_and_add_networks(data.get(NETWORKS))
    world.env_requests.add_tier_environment(env_name, tier)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_add_tier_response(world.response, status_code)
