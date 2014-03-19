# -*- coding: utf-8 -*-
from lettuce import step, world
from lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http
from tools import tier
from tools.tier import Tier
from tools.constants import NAME, DESCRIPTION, PRODUCTS, NETWORKS, PAAS,\
    TIER_IMAGE

dataset_utils = DatasetUtils()


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
    tier = Tier(data.get(NAME), world.config[PAAS][TIER_IMAGE])
    tier.parse_and_add_products(data.get(PRODUCTS))
    tier.parse_and_add_networks(data.get(NETWORKS))
    world.env_requests.add_tier_abstract_environment(env_name, tier)


@step(u'I request the deletion of the tier "([^"]*)" from the abstract environment "([^"]*)"')
def i_request_the_deletion_of_a_tier_from_a_abstract_environment_with_data(step, tier_name, env_name):
    tier_name = dataset_utils.generate_fixed_length_param(tier_name)
    world.env_requests.delete_tier_abstract_environment(env_name, tier_name)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_delete_tier_response(world.response, status_code)


@step(u'the tier "([^"]*)" of the abstract environment "([^"]*)" is no longer available')
def the_tier_of_a_abstract_environment_is_no_longer_available(step, tier_name, env_name):
    tier_name = dataset_utils.generate_fixed_length_param(tier_name)
    world.env_requests.get_tier_abstract_environment(env_name, tier_name)
    tier.check_get_tier_response(world.response, 404)
