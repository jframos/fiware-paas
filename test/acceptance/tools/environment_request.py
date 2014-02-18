from tools import tier
__author__ = 'henar'

import http
import sys
import json
from xml.etree.ElementTree import tostring

from environment import Environment
from tier import Tier, Network
from lettuce import world


class EnvironmentRequest:
    def __init__(self, keystone_url, paas_manager_url, tenant, user, password, vdc, image, sdc_url=''):
        self.paasmanager_url = paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.keystone_url = keystone_url

        self.user = user
        self.password = password
        self.tenant = tenant

        self.token = self.__get__token()

    def __get__token(self):
        return http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    def __get_environments(self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Accept': "application/json"}
        return http.get(url, headers)

    def __get_environment(self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Accept': "application/json"}
        return http.get(url, headers)

    def __delete_environments(self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Accept': "application/json"}
        return http.delete(url, headers)

    def __delete(self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Accept': "application/json"}
        response = http.delete(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to delete the environment ' + str(response.status)
            sys.exit(1)

    def __add_environment(self, url, environment_payload):
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml"}

        return http.post(url, headers, environment_payload)

    def __add_tier_environment(self, url, tier_payload):
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml"}

        response = http.post(url, headers, tier_payload)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to add a tier in an environment ' + str(response.status)
            sys.exit(1)

    def __update_environment(self, url, environment_payload):
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml"}

        return http.put(url, headers, environment_payload)

    def __update_tier_environment(self, url, tier_payload):
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml"}

        response = http.put(url, headers, tier_payload)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to add a tier in an environment ' + str(response.status)
            sys.exit(1)

    def get_abstract_environments(self):
        url = "%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/environment")

        self.__get_environments(url)

    def add_abstract_environment(self, environment_name, environment_description, tiers=None):
        url = "%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/environment")

        env = Environment(environment_name, environment_description, tiers)

        payload = tostring(env.to_env_xml())
        world.response = self.__add_environment(url, payload)

        """Store it in the world to tear it down later"""
        try:
            world.abstract_environments.append(environment_name)
        except AttributeError:
            world.instances = [environment_name]

    def add_environment(self, environment_name, environment_description, tiers=None):
        """
        Adds a new environment with the data provided.
        :param environment_name: Name of the environment.
        :param environment_description: Description of the environment.
        :param tiers: List of tiers of the environment.
        """
        url = "%s/%s/%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE", "vdc", self.vdc, "environment")

        env = Environment(environment_name, environment_description, tiers)

        payload = tostring(env.to_env_xml())
        world.response = self.__add_environment(url, payload)

        """Store it in the world to tear it down later"""
        try:
            world.environments.append(environment_name)
        except AttributeError:
            world.environments = [environment_name]

    def add_abstract_tier_environment(self, environment_name, tier):
        url = "%s/%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/environment", environment_name, "tier")

        payload = tostring(tier.to_tier_xml())
        self.__add_tier_environment(url, payload)

    def add_tier_environment(self, environment_name, tier):
        url = "%s/%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "vdc", self.vdc, "environment", environment_name, "tier")

        payload = tostring(tier.to_tier_xml())
        self.__add_tier_environment(url, payload)

    def add_tier_environment_network(self, environment_name, tier):
        url = "%s/%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "vdc", self.vdc, "environment", environment_name, "tier")

        print tostring(tier.to_tier_xml())
        payload = tostring(tier.to_tier_xml())
        print payload
        self.__add_tier_environment(url, payload)

    def __add_product_to_tier(self, url, products_information):
        product = self.process_products(products_information)
        payload = tostring(product[0].to_product_xml())

        self.__add_tier_environment(url, payload)

    def add_product_to_tier(self, environment_name, tier_name, products_information):
        url = "%s/%s/%s/%s/%s/%s/%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE", "vdc",
                                              self.vdc, "environment", environment_name, "tier", tier_name,
                                              "productRelease")
        self.__add_product_to_tier(url, products_information)

    def add_abstract_product_to_tier(self, environment_name, tier_name, products_information):
        url = "%s/%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "environment", environment_name, "tier", tier_name,
            "productRelease")
        print url
        self.__add_product_to_tier(url, products_information)

    def delete_abstract_environments(self, environment_name):
        url = "%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/environment", environment_name)
        world.response = self.__delete_environments(url)

        """Remove it from the world too"""
        try:
            world.abstract_environments.remove(environment_name)
        except:
            pass

    def delete_environment(self, environment_name):
        url = "%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "vdc", self.vdc, "environment", environment_name)
        world.response = self.__delete_environments(url)

        """Remove it from the world too"""
        try:
            world.environments.remove(environment_name)
        except:
            pass

    def delete_tier(self, environment_name, tier_name):
        url = "%s/%s/%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "vdc", self.vdc, "environment", environment_name, "tier",
            tier_name)

        self.__delete(url)

    def delete_abstract_tier(self, environment_name, tier_name):
        url = "%s/%s/%s/%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE", "environment", environment_name, "tier", tier_name)

        self.__delete(url)

        #   product_name =
        #   product = Product ()
        #   add_product

    def update_abstract_environment(self, environment_name, environment_description, tiers=None):
        url = "%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/environment", environment_name)

        env = Environment(environment_name, environment_description, tiers)

        payload = tostring(env.to_env_xml())
        world.response = self.__update_environment(url, payload)

    def update_environment(self, environment_name, environment_description, tiers=None):
        """
        Updates the environment with the name provided setting the data provided.
        :param environment_name: Name of the environment.
        :param environment_description: New description of the environment.
        :param tiers: New list of tiers of the environment.
        """
        url = "%s/%s/%s/%s/%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE",
                                  "vdc", self.vdc, "environment", environment_name)

        env = Environment(environment_name, environment_description, tiers)

        payload = tostring(env.to_env_xml())
        world.response = self.__update_environment(url, payload)

    ##
    ## get_environment - Obtiene la lista de environments ---
    ##
    def get_environments(self):
        url = "%s/%s" % (self.paasmanager_url, "catalog/org/FIWARE/vdc/" + self.vdc + "/environment")

        world.response = self.__get_environments(url)

    def get_environment(self, environment_name):
        url = "%s/%s/%s" % (
            self.paasmanager_url, "catalog/org/FIWARE/vdc/" + self.vdc + "/environment", environment_name)

        world.response = self.__get_environment(url)


def process_environments(environments):
    """
    Process the environments provided as a list of dictionaries.
    :param environments: environments to be processed.
    :return: a list of Environment objects.
    """
    processed_environments = []
    if isinstance(environments, list):
        for env in environments:
            processed_environments.append(process_environment(env))
    else:
        # Single environment received
        processed_environments.append(process_environment(environments))
    return processed_environments


def process_environment(environment):
    """
    Process the environment provided as a dictionary.
    :param environment: environment to be processed.
    :return: a Environment object.
    """
    processed_environment = Environment(environment['name'], environment['description'])
    try:
        tiers = tier.process_tiers(environment['tierDtos'])
        processed_environment.add_tiers(tiers)
    except:
        pass
    return processed_environment


def check_add_environment_response(response, expected_status_code):
    """
    Checks that the response for an add environment request is the
    expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    """
    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. Body content: %s" \
    % (response.status, expected_status_code, response.read())


def check_update_environment_response(response, expected_status_code):
    """
    Checks that the response for an update environment request is the
    expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    """
    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. Body content: %s" \
    % (response.status, expected_status_code, response.read())


def check_delete_environment_response(response, expected_status_code):
    """
    Checks that the response for a delete environment request is the
    expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    """
    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. Body content: %s" \
    % (response.status, expected_status_code, response.read())


def check_get_environments_response(response, expected_status_code,
                                   expected_environments_number=None):
    """
    Checks that the response for a get environments request is the
    expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    :param expected_environments_number: Expected number of environments.
    """
    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. Body content: %s" \
    % (response.status, expected_status_code, response.read())

    if expected_environments_number is not None:
        data = json.loads(response.read())
        #print data, "\n\n\n\n"
        if expected_environments_number == 0:
            # No content expected when the lists of environments is empty
            assert data == None, "Unexpected content received: %s" % data
        else:
            environments = data["environmentDto"]
            world.response.environments = process_environments(environments)

            assert len(world.response.environments) == expected_environments_number, \
            "Wrong number of envs received: %d. Expected: %d." \
            % (len(world.response.environments), expected_environments_number)


def check_environment_in_list(environments_list, environment_name,
                              environment_description, tiers_number=0):
    """
    Checks that a certain environment is in the list of environments provided.
    :param environments_list: List of environments to be checked.
    :param environment_name: Name of the environment to be found.
    :param environment_description: Description of the environment to be found.
    """
    for env in environments_list:
        if env.name == environment_name:  # Expected environment found
            assert env.description == environment_description, \
            "Wrong description received for environment %s: %s. Expected: %s." \
            % (env.name, env.description, environment_description)

            assert len(env.tiers) == tiers_number, \
            "Wrong number of tiers received for environment %s: %d. Expected: %d." \
            % (env.name, len(env.tiers), tiers_number)

            return

    assert False, \
    "No environment found in the list with name %s" \
    % (environment_name, environment_description)


def check_get_environment_response(response, expected_status_code,
                                   expected_environments_name=None,
                                   expected_environments_description=None,
                                   expected_environments_tiers=None):
    """
    Checks that the response for a get environments request is the
    expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    :param expected_environments_number: Expected number of environments.
    """
    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. Body content: %s" \
    % (response.status, expected_status_code, response.read())

    if expected_environments_name is not None:
        data = json.loads(response.read())
        environment = process_environment(data)

        assert environment.name == expected_environments_name, \
        "Wrong name received: %s. Expected: %s." \
        % (environment.name, expected_environments_name)

    if expected_environments_description is not None:
        assert environment.description == expected_environments_description, \
        "Wrong description received: %s. Expected: %s." \
        % (environment.description, expected_environments_description)

    if expected_environments_tiers is not None:
        assert len(environment.tiers) == len(expected_environments_tiers), \
        "Wrong number of tiers received: %d. Expected: %d." \
        % (len(environment.tiers), len(expected_environments_tiers))

        for expected_tier in expected_environments_tiers:
            # Find the tier that matches each of the expected ones and compare
            received_tier = None
            for tier in environment.tiers:
                if tier.name == expected_tier.name:
                    received_tier = tier
                    break

            assert received_tier is not None, \
            "Tier not found in response: %s" % (expected_tier.name)

            assert received_tier == expected_tier, \
            "The data for tier %s does not match the expected one. Received: %s. Expected: %s." \
            % (received_tier.name, tostring(received_tier.to_tier_xml()), tostring(expected_tier.to_tier_xml()))
