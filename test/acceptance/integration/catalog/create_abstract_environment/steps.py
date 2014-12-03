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
from tools import environment_request
from tools.constants import NAME, DESCRIPTION
from common_steps import sdc_product_provisioning_steps, paas_environment_provisioning

dataset_utils = DatasetUtils()


@step(u'the paas manager is up and properly configured')
def the_paas_manager_is_up_and_properly_configured(step):
    pass  # Nothing to do here, the set up should be done by external means


@step(u'the following instance attributes for product "([^"]*)":')
def the_following_instance_attributes(step, product_name):
    paas_environment_provisioning.process_following_instance_attributes(step, product_name)


@step(u'a list of tiers has been defined with data:')
def a_list_of_tiers_has_been_defined_with_data(step):
    world.tiers = paas_environment_provisioning.process_the_list_of_tiers(step)


@step(u'an abstract environment has already been created with data:')
def an_abstract_environment_has_already_been_created_with_data(step):
    i_request_the_creation_of_an_abstract_environment_with_data(step)


@step(u'I request the creation of an abstract environment with data:')
def i_request_the_creation_of_an_abstract_environment_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_abstract_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'I request the creation of an abstract environment with the previous tiers and data:')
def i_request_the_creation_of_an_abstract_environment_with_tiers_and_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_abstract_environment(data.get(NAME), data.get(DESCRIPTION), world.tiers)


@step(u'I receive an? "([^"]*)" response')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    environment_request.check_add_environment_response(world.response, status_code)


@step(u'the product "([^"]*)" with version "([^"]*)" is created in SDC with attributes:')
def the_product_group1_is_created_in_sdc_with_attributes(step, product_name, product_version):
    sdc_product_provisioning_steps.product_is_created_in_sdc_with_attributes(step, product_name, product_version)
