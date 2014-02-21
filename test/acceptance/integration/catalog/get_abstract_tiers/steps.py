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


@step(u'an abstract environment has already been created with data:')
def an_abstract_environment_has_already_been_created_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_abstract_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'a tier has already been added to the abstract environment "([^"]*)" with data:')
def a_tier_has_already_been_added_to_abstract_environment_with_data(step, env_name):
    data = dataset_utils.prepare_data(step.hashes[0])
    tier = Tier(data.get(NAME), world.config['paas']['image'])
    tier.parse_and_add_products(data.get(PRODUCTS))
    tier.parse_and_add_networks(data.get(NETWORKS))
    world.env_requests.add_tier_abstract_environment(env_name, tier)


@step(u'I request the list of tiers of the abstract environment "([^"]*)"')
def i_request_the_list_of_tiers_of_a_abstract_environment(step, env_name):
    world.env_requests.get_tiers_abstract_environment(env_name)


@step(u'I receive an? "([^"]*)" response with "(\d)" items? in the list')
def i_receive_a_response_of_type_with_items(step, response_type, items_number):
    status_code = http.status_codes[response_type]
    tier.check_get_tiers_response(world.response, status_code, int(items_number))


@step(u'I receive an? "([^"]*)" response with no content')
def i_receive_a_response_of_type_with_no_content(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_get_tiers_response(world.response, status_code, 0)


@step(u'there is a tier in the list with data:')
def there_is_a_tier_in_the_list_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    products = tier.parse_products(data.get(PRODUCTS))
    networks = tier.parse_networks(data.get(NETWORKS))
    tier.check_tier_in_list(world.response.tiers, data.get(NAME), products, networks)
