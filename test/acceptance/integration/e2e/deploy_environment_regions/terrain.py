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

__author__ = 'jfernandez'


from lettuce import before, world, after
from tools.http import get_token
from tools.keystone_request import KeystoneRequest
from tools.glance_request import GlanceRequest
from tools.environment_request import EnvironmentRequest
from tools.environment_instance_request import EnvironmentInstanceRequest
from tools.nova_request import NovaRequest
from tools import environment_request, environment_instance_request
from tools.utils import raw_httplib_request_to_python_dic
import os
from tools.constants import PAAS, KEYSTONE_URL, PAASMANAGER_URL, TENANT, USER,\
    PASSWORD, VDC, SDC_URL, GLANCE_URL, NOVA_URL, ENVIRONMENT, ENVIRONMENT_TESTFILES


@before.all
def before_all():
    """ Hook: Before all features. It will config common requisites for TCs execution """
    # Get Auth Token
    world.auth_token = get_token(world.config[PAAS][KEYSTONE_URL] + '/tokens', world.config[PAAS][TENANT],
                                 world.config[PAAS][USER],
                                 world.config[PAAS][PASSWORD])


@before.each_feature
def after_each_feature(feature):
    """ Hook: Before each feature. It will instance all request managers used in the feature and will initialize vars"""
    world.keystone_request = KeystoneRequest(world.config[PAAS][KEYSTONE_URL],
                                             world.config[PAAS][TENANT],
                                             world.config[PAAS][USER],
                                             world.config[PAAS][PASSWORD],
                                             world.config[PAAS][VDC])

    world.glance_request = GlanceRequest(world.config[PAAS][GLANCE_URL],
                                         world.config[PAAS][TENANT],
                                         world.config[PAAS][USER],
                                         world.config[PAAS][PASSWORD],
                                         world.config[PAAS][VDC],
                                         world.auth_token)

    world.nova_request = NovaRequest(world.config[PAAS][NOVA_URL],
                                         world.config[PAAS][TENANT],
                                         world.config[PAAS][USER],
                                         world.config[PAAS][PASSWORD],
                                         world.config[PAAS][VDC],
                                         world.auth_token)

    world.env_requests = EnvironmentRequest(world.config[PAAS][KEYSTONE_URL],
                                            world.config[PAAS][PAASMANAGER_URL],
                                            world.config[PAAS][TENANT],
                                            world.config[PAAS][USER],
                                            world.config[PAAS][PASSWORD],
                                            world.config[PAAS][VDC],
                                            world.config[PAAS][SDC_URL])

    world.inst_requests = EnvironmentInstanceRequest(world.config[PAAS][KEYSTONE_URL],
        world.config[PAAS][PAASMANAGER_URL],
        world.config[PAAS][TENANT],
        world.config[PAAS][USER],
        world.config[PAAS][PASSWORD],
        world.config[PAAS][VDC],
        world.config[PAAS][SDC_URL])

    world.region_list = None


@after.outline
def before_outline(param1, param2, param3, param4):
    """ Hook: Will be executed before each Scenario Outline. Same behaviour as 'before_each_scenario'"""
    try:
        test_files_dir = world.config[ENVIRONMENT][ENVIRONMENT_TESTFILES]
        print "Writing instance {} details to dir {}".format(world.instance_name, test_files_dir)

        world.inst_requests.get_instance(world.instance_name)
        body_env_response = raw_httplib_request_to_python_dic(world.response)

        if not os.path.exists(test_files_dir):
            os.makedirs(test_files_dir)

        file = open(os.path.join(test_files_dir, world.instance_name+"_instance"), 'w')
        file.write(str(body_env_response))
        file.close()
    except Exception as e:
        print "WARNING: Instance data cannot be written to test file. {}".format(e.message)


@after.each_scenario
def after_each_scenario(scenario):
    """ Hook: After each scenario. It will clean environments and instances created in the scenario. """
    environment_instance_request.delete_created_instances()
    environment_request.delete_created_environments()
