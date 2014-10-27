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


class NovaRequest:

    def __init__(self, nova_url, tenant, user, password, vdc, auth_token):
        """
        Constructor. Initializes class attributes.
        :param nova_url: Nova URL
        :param tenant: Fiware tenant name
        :param user: Fiware User name
        :param password: Fiware Password
        :param vdc: TenantId
        :param auth_token: Valid Auth Token (Keystone)
        """
        self.nova_url = nova_url

        self.vdc = vdc

        self.user = user
        self.password = password
        self.tenant = tenant

        self.default_headers = {'Accept': 'application/json',
                                'X-Auth-Token': auth_token,
                                'Tenant-Id': self.tenant}

    def __get__(self, url):
        """
        Executes a get request to Nova service
        :param url: Full URL to GET
        :return: HTTPlib request
        """
        return http.get(url, self.default_headers)

    def get_flavour_list(self):
        """
        Gets the list of available flavors
        :return: HTTPlib request
        """
        url = "{}/{}".format(self.nova_url, 'flavors')
        return self.__get__(url)


def get_number_of_flavors(body_response):
    """
    Returns the number of images in the list.
    :param body_response: Parsed response (Python dic). List of flavors
    :return: Length of the list
    """
    return len(body_response['flavors'])


def get_first_flavor_in_list(body_response, name_filter=None):
    flavor = None
    if name_filter is not None:
        for flavor in body_response['flavors']:
            if name_filter in flavor['name']:
                flavor = flavor['id']
                break
    else:
        if len(body_response['flavors']) != 0:
            flavor = body_response['flavors'][0]['id']

    return flavor