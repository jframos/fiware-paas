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

import http


class GlanceRequest:

    def __init__(self, glance_url, tenant, user, password, vdc, auth_token):
        """
        Class constructor. Initializes class variables
        :param glance_url: Glance URL
        :param tenant: Fiware Tenant name
        :param user: Fiware User name
        :param password: Paasword
        :param vdc: Tenant Id
        :param auth_token: Valid Auth Token (Keystone)
        """
        self.glance_url = glance_url

        self.vdc = vdc

        self.user = user
        self.password = password
        self.tenant = tenant

        self.default_headers = {'Accept': 'application/json',
                                'X-Auth-Token': auth_token,
                                'Tenant-Id': self.tenant}

    def __get__(self, url):
        """
        Executes a get request to Glance service
        :param url: Full URL to GET
        :return: HTTPlib request
        """
        return http.get(url, self.default_headers)

    def get_image_list(self):
        """
        Retrieves the list of images from Glance
        :return: HTTPlib request
        """
        url = "{}/{}".format(self.glance_url, 'images')
        return self.__get__(url)

    def get_image_list_by_property(self, property_name, property_value):
        """
        Retrieves the detailed list of images from Glance filtered by property
        :return: HTTPlib request
        """
        url = "{}/{}/{}?property-{}={}".format(self.glance_url, 'images', 'detail', property_name, property_value)
        return self.__get__(url)


def get_number_of_images(body_response):
    """
    Returns the number of images in the list.
    :param body_response: Parsed response (Python dic). List of images
    :return: Length of list
    """
    return len(body_response['images'])


def get_first_image_in_list(body_response, name_filter=None):
    """
    Gets the first image in the list
    :param body_response: Parsed response (Python dic)
    :param name_filter: If this arg is set, this method will filtered by name content
    :return: First image in list (that contains name_filter in its name); None if not found or list is empty
    """
    image_id = None
    if name_filter is not None:
        for image in body_response['images']:
            if name_filter in image['name']:
                image_id = image['id']
                break
    else:
        if len(body_response['images']) != 0:
            image_id = body_response['images'][0]['id']

    return image_id
