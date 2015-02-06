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
from tools.constants import NAME, DESCRIPTION
from common_steps import sdc_product_provisioning_steps, paas_environment_provisioning

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
    i_request_the_addition_of_a_tier_to_abstract_environment_with_data(step, env_name)


@step(u'there is no abstract environment with name "([^"]*)" already created')
def there_is_no_abstract_environment_with_name_already_created(step, name):
    world.env_requests.delete_abstract_environment(name)  # Just in case it exists


@step(u'I request the addition of a tier to the abstract environment "([^"]*)" with data:')
def i_request_the_addition_of_a_tier_to_abstract_environment_with_data(step, env_name):
    tier_list = paas_environment_provisioning.process_the_list_of_tiers(step)

    for tier in tier_list:
        world.env_requests.add_tier_abstract_environment(env_name, tier)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    tier.check_add_tier_response(world.response, status_code)


@step(u'the following instance attributes for product "([^"]*)":')
def the_following_instance_attributes(step, product_name):
    paas_environment_provisioning.process_following_instance_attributes(step, product_name)


@step(u'the product "([^"]*)" with version "([^"]*)" is created in SDC with attributes:')
def the_product_group1_is_created_in_sdc_with_attributes(step, product_name, product_version):
    sdc_product_provisioning_steps.product_is_created_in_sdc_with_attributes(step, product_name, product_version)
