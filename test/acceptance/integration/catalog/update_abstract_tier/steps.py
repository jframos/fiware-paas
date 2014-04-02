# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es

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


@step(u'I request the update of the tier "([^"]*)" of the abstract environment "([^"]*)" with data:')
def i_request_the_update_of_a_tier_of_a_abstract_environment_with_data(step, tier_name, env_name):
    data = dataset_utils.prepare_data(step.hashes[0])
    tier = Tier(data.get(NAME), world.config[PAAS][TIER_IMAGE])
    tier.parse_and_add_products(data.get(PRODUCTS))
    tier.parse_and_add_networks(data.get(NETWORKS))
    world.env_requests.update_tier_abstract_environment(env_name, tier_name, tier)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_update_tier_response(world.response, status_code)


@step(u'the data of the tier "([^"]*)" of the abstract environment "([^"]*)" becomes:')
def the_data_of_a_tier_of_a_abstract_environment_becomes(step, tier_name, env_name):
    tier_name = dataset_utils.generate_fixed_length_param(tier_name)
    world.env_requests.get_tier_abstract_environment(env_name, tier_name)
    data = dataset_utils.prepare_data(step.hashes[0])
    products = tier.parse_products(data.get(PRODUCTS))
    networks = tier.parse_networks(data.get(NETWORKS))
    tier.check_get_tier_response(world.response, 200,
        data.get(NAME), products, networks)
