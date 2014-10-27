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


from lettuce import step, world
from lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools.keystone_request import get_images_regions, get_public_endpoint_url_by_type, get_public_endpoint_url_by_name
from tools.glance_request import get_number_of_images, get_first_image_in_list
from tools.tier import Tier
from tools.constants import NAME, DESCRIPTION, PRODUCTS, NETWORKS
from tools.utils import raw_httplib_request_to_python_dic
from tools.nova_request import get_number_of_flavors, get_first_flavor_in_list
from tools.environment_instance_request import EnvironmentInstance, check_task_status, check_add_instance_response
from tools.environment_request import process_environment
from nose.tools import assert_not_equal, assert_equal, assert_true
from tools import http

dataset_utils = DatasetUtils()


@step(u'the region "([^"]*)"')
def the_region_group1(step, region_name):
    """ Set region name """
    world.region_name = region_name


@step(u'I retrieve the service catalog')
def i_retrieve_the_service_catalog(step):
    """ Retrieve service catalog from keystone """
    world.response = world.keystone_request.get_token()


@step(u'endpoints from service catalog')
def endpoints_from_service_catalog(step):
    """ Configure endpoints to be used from service catalog urls instead of config file """
    response = world.keystone_request.get_token()
    body_keystone_response = raw_httplib_request_to_python_dic(response)

    world.glance_request.glance_url = get_public_endpoint_url_by_type(body_keystone_response,
                                                                      'image', world.region_name)
    world.nova_request.nova_url = get_public_endpoint_url_by_type(body_keystone_response,
                                                                  'compute', world.region_name)
    world.env_requests.paasmanager_url = get_public_endpoint_url_by_type(body_keystone_response,
                                                                         'paas', world.region_name)
    world.inst_requests.paasmanager_url = world.env_requests.paasmanager_url


@step(u'the region is in image endpoints list')
def the_region_is_in_image_endpoints_list(step):
    """ Check if region is in keystone (image service) """
    assert_equal(world.response.status, 200, "Error to obtain the token. HTTP status code is not the expected")
    world.body_keystone_response = raw_httplib_request_to_python_dic(world.response)

    # Get img list from response (service catalog)
    if world.region_list is None:
        world.region_list = get_images_regions(world.body_keystone_response)

    found = False
    for region in world.region_list:
        if world.region_name == region['region']:
            found = True
            break
    assert_true(found, "Region {} not found in services catalog (images)".format(world.region_name))


@step(u'I retrieve the list of images')
def i_retrieve_the_list_of_images(step):
    """ Retrieve the list of images """
    world.response = world.glance_request.get_image_list()


@step(u'I retrieve the list of SDC-aware images')
def i_retrieve_the_list_of_sdc_aware_images(step):
    """ Retrieve the list of SDC-Aware images """
    world.response = world.glance_request.get_image_list_by_property('sdc_aware', 'true')


@step(u'I retrieve the list of flavors')
def i_retrieve_the_list_of_flavours(step):
    """ Retrieve the list of flavors """
    world.response = world.nova_request.get_flavour_list()


@step(u'the region has at least one SDC-aware image')
def the_region_has_sdc_aware_images(step):
    """ Check if the list retrieved has a SDC-aware image at least and save in World var the first one """
    assert_equal(world.response.status, 200, "Error to obtain the images list. HTTP status code is not the expected")
    world.body_response = raw_httplib_request_to_python_dic(world.response)

    # Check if there are images (list > 0)
    number_of_images = get_number_of_images(world.body_response)
    assert_not_equal(number_of_images, 0, "There are not images in the region " + world.region_name)

    # Get a image from the list. First, try to get the first Centos img
    world.image_sdc_aware_id = get_first_image_in_list(world.body_response, name_filter='Cent')
    if world.image_sdc_aware_id is None:
        # If no centOS images found, get the first in list whatever its type is
        print "WARNING: No 'Cent' images found. It will get the first image found whatever its type is"
        world.image_sdc_aware_id = get_first_image_in_list(world.body_response)

    assert_not_equal(world.image_sdc_aware_id, None, "Error to obtain a image from the list")


@step(u'the region has images')
def the_region_has_images(step):
    """ Check if the image list retrieved is not empty and save in World var the first one """
    the_region_has_sdc_aware_images(step)


@step(u'the region has at least one flavor')
def the_region_has_at_least_one_flavor(step):
    """ Check if the flavors list retrieved is not empty and save in World var the first one """
    assert_equal(world.response.status, 200, "Error to obtain the images list. HTTP status code is not the expected")
    world.body_nova_response = raw_httplib_request_to_python_dic(world.response)

    # Check if there are flavors (list > 0)
    number_of_flavors = get_number_of_flavors(world.body_nova_response)
    assert_not_equal(number_of_flavors, 0, "There are not flavors in the region " + world.region_name)

    # Get a image from the list. First, try to get the first 'small' flavor
    world.flavor_id = get_first_flavor_in_list(world.body_nova_response, 'small')
    if world.flavor_id is None:
        # If no 'small' flavor found, get the first in list whatever its type is
        print "WARNING: No 'small' flavor found. It will get the first one found whatever its type is"
        world.flavor_id = get_first_flavor_in_list(world.body_nova_response, 'small')

    assert_not_equal(world.flavor_id, None, "Error to obtain a flavor from the list")


@step(u'the region exists and it has valid images and flavors')
def the_region_exists_and_it_has_valid_images_and_flavors(step):
    """ Complex step: Retrieve and check if region exists in keystone and retrieve and check images and flavors """
    # Region is in service catalog list (image)
    i_retrieve_the_service_catalog(step)
    the_region_is_in_image_endpoints_list(step)

    # Region has images
    i_retrieve_the_list_of_sdc_aware_images(step)
    try:
        the_region_has_images(step)
    except:
        i_retrieve_the_list_of_images(step)
        the_region_has_images(step)

    # Region has flavors
    i_retrieve_the_list_of_flavours(step)
    the_region_has_at_least_one_flavor(step)


@step(u'a created environment with data:')
def a_created_environment_with_data(step):
    """ Create a environment """
    data = dataset_utils.prepare_data(step.hashes[0])
    world.environment_name = data.get(NAME)+world.region_name
    world.env_requests.add_environment(world.environment_name, data.get(DESCRIPTION))


@step(u'a created tiers with data:')
def a_list_of_tiers_has_been_defined_with_data(step):
    """ Create and add tiers to the environment """
    world.tiers = []
    for row in step.hashes:
        data = dataset_utils.prepare_data(row)

        tier = Tier(data.get(NAME), world.image_sdc_aware_id, tier_flavour=world.flavor_id,
                    tier_region=world.region_name)

        tier.parse_and_add_products(data.get(PRODUCTS))

        if NETWORKS in data:
            # Is Neutron available?
            i_retrieve_the_service_catalog(step)
            body_response = raw_httplib_request_to_python_dic(world.response)
            nova_public_url = get_public_endpoint_url_by_name(body_response, 'nova', world.region_name)
            if nova_public_url is not None:
                tier.parse_and_add_networks(data.get(NETWORKS))
            else:
                print "WARNING: Neutron is not available. Tier wil be created without networks."

        world.env_requests.add_tier_environment(world.environment_name, tier)


@step(u'I request the creation of an instance for that environment using data:')
def i_request_the_creation_of_an_instance_using_data(step):
    """ Create a product instance for the created environment and tiers """
    # First, send the request to get the environment on which the instance will be based
    world.env_requests.get_environment(world.environment_name)
    body_env_response = raw_httplib_request_to_python_dic(world.response)
    assert_equal(world.response.status, 200,
                 "Wrong status code received getting environment: %d. Expected: %d. Body content: %s"
                 % (world.response.status, 200, body_env_response))

    target_environment = process_environment(body_env_response)

    # Then, create the instance
    env_data = dataset_utils.prepare_data(step.hashes[0])
    environment_instance = EnvironmentInstance(env_data.get(NAME)+world.region_name, env_data.get(DESCRIPTION),
                                               target_environment)

    world.inst_requests.add_instance(environment_instance)


@step(u'I receive an? "([^"]*)" response(?: with a task)?')
def i_receive_a_response_of_type(step, response_type):
    """ Check task response """
    status_code = http.status_codes[response_type]
    check_add_instance_response(world.response, status_code)


@step(u'the task ends with "([^"]*)" status')
def the_task_ends_with_status(step, status):
    """ Wait for task execution and check task status """
    print "TEST DATA - Region: {}; Image: {}; Flavor: {}".format(world.region_name, world.image_sdc_aware_id, world.flavor_id)
    check_task_status(world.task_data, status)
