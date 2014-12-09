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
from tools import http, environment_request, environment_instance_request
from tools.environment_instance import EnvironmentInstance
import json
from tools.constants import NAME, DESCRIPTION
from common_steps import sdc_product_provisioning_steps, paas_environment_provisioning

dataset_utils = DatasetUtils()


@step(u'the paas manager is up and properly configured')
def the_paas_manager_is_up_and_properly_configured(step):
    pass  # Nothing to do here, the set up should be done by external means


@step(u'a list of tiers has been defined with data:')
def a_list_of_tiers_has_been_defined_with_data(step):
    world.tiers = paas_environment_provisioning.process_the_list_of_tiers(step)


@step(u'an environment has already been created with data:')
def an_environment_has_already_been_created_with_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_environment(data.get(NAME), data.get(DESCRIPTION))


@step(u'an environment has already been created with the previous tiers and data:')
def an_environment_has_already_been_created_with_the_previous_tiers_and_data(step):
    data = dataset_utils.prepare_data(step.hashes[0])
    world.env_requests.add_environment(data.get(NAME), data.get(DESCRIPTION), world.tiers)
    assert world.response.status == 204,\
    "Wrong status code received creating environment: %d. Expected: %d. Body content: %s"\
    % (world.response.status, 204, world.response.read())


@step(u'an instance of the environment "([^"]*)" has already been created using data:')
def an_instance_of_the_environment_has_already_been_created_using_data(step, env_name):
    i_request_the_creation_of_an_instance_of_an_environment_using_data(step, env_name)
    the_task_ends_with_status(step, "SUCCESS")

@step(u'I request the creation of an instance of the environment "([^"]*)" using data:')
def i_request_the_creation_of_an_instance_of_an_environment_using_data(step, env_name):
    # First, send the request to get the environment on which the instance will be based
    env_name = dataset_utils.generate_fixed_length_param(env_name)
    world.env_requests.get_environment(env_name)
    assert world.response.status == 200,\
    "Wrong status code received getting environment: %d. Expected: %d. Body content: %s"\
    % (world.response.status, 200, world.response.read())
    environment = environment_request.process_environment(json.loads(world.response.read()))
    # Then, create the instance
    data = dataset_utils.prepare_data(step.hashes[0])
    instance = EnvironmentInstance(data.get(NAME), data.get(DESCRIPTION), environment)
    world.inst_requests.add_instance(instance)


@step(u'I receive an? "([^"]*)" response(?: with a task)?')
def i_receive_a_response_of_type(step, response_type):
    status_code = http.status_codes[response_type]
    environment_instance_request.check_add_instance_response(world.response, status_code)


@step(u'the task ends with "([^"]*)" status')
def the_task_ends_with_status(step, status):
    environment_instance_request.check_task_status(world.task_data, status)


@step(u'the product installator to be used is "([^"]*)"')
def the_installator_to_be_used_is_group1(step, installator):
    world.product_installator = installator


@step(u'the product "([^"]*)" with version "([^"]*)" is created in SDC with attributes:')
def the_product_group1_is_created_in_sdc_with_attributes(step, product_name, product_version):
    sdc_product_provisioning_steps.product_is_created_in_sdc_with_attributes(step, product_name, product_version)
