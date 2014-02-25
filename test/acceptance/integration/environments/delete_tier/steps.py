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
    data = dataset_utils.prepare_data(step.hashes[0])
    tier = Tier(data.get(NAME), world.config['paas']['tier_image'])
    tier.parse_and_add_products(data.get(PRODUCTS))
    tier.parse_and_add_networks(data.get(NETWORKS))
    world.env_requests.add_tier_environment(env_name, tier)


@step(u'I request the deletion of the tier "([^"]*)" from the environment "([^"]*)"')
def i_request_the_deletion_of_a_tier_from_a_environment_with_data(step, tier_name, env_name):
    tier_name = dataset_utils.generate_fixed_length_param(tier_name)
    world.env_requests.delete_tier_environment(env_name, tier_name)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_add_tier_response(world.response, status_code)


@step(u'the tier "([^"]*)" of the environment "([^"]*)" is no longer available')
def the_tier_of_a_environment_is_no_longer_available(step, tier_name, env_name):
    tier_name = dataset_utils.generate_fixed_length_param(tier_name)
    world.env_requests.get_tier_environment(env_name, tier_name)
    tier.check_get_tier_response(world.response, 404)
