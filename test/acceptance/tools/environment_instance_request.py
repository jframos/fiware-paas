__author__ = 'henar'

import sys
import json
from xml.etree.ElementTree import tostring
from tools import http
from tools.environment_instance import EnvironmentInstance


class EnvironmentInstanceRequest:

    def __init__(self, keystone_url, paas_manager_url,
                 tenant, user, password, vdc, sdc_url=''):

        self.paasmanager_url = paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.keystone_url = keystone_url

        self.user = user
        self.password = password
        self.tenant = tenant

        self.token = self.__get__token()

    def __get__token(self):
        return http.get_token(self.keystone_url + '/tokens',
                              self.tenant, self.user, self.password)

    def __process_env_inst(self, data):
        envIns = EnvironmentInstance(data['blueprintName'], data['description'],
                                     None, data['status'])
        return envIns

    def add_blueprint_instance(self, environment_instance):

        url = "%s/%s/%s/%s" % (self.paasmanager_url, "envInst/org/FIWARE/vdc",
                               self.vdc, "environmentInstance")
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Content-Type': "application/xml", 'Accept': "application/json"}
        print headers
        payload = tostring(environment_instance.to_xml())
        response = http.post(url, headers, payload)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to deploy the environment ' + str(response.status)
            sys.exit(1)
        else:

            http.processTask(headers, json.loads(response.read()))

    def delete_blueprint_instance(self, environment_instance):

        url = "%s/%s/%s/%s/%s" % (self.paasmanager_url, "envInst/org/FIWARE/vdc", self.vdc,
                                  "environmentInstance", environment_instance)

        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml", 'Accept': "application/json"}

        response = http.delete(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to delete the environment ' + str(response.status)
            sys.exit(1)
        else:
            http.processTask(headers, json.loads(response.read()))

    def get_blueprint_instance(self, environment_instance_name):

        url = "%s/%s/%s/%s/%s" % (self.paasmanager_url, "envInst/org/FIWARE/vdc", self.vdc,
                                "environmentInstance", environment_instance_name)

        print url
        headers = {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                   'Content-Type': "application/xml", 'Accept': "application/json"}

        response = http.get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 204:
            print 'error to deploy the environment ' + str(response.status)
            sys.exit(1)
        else:
            envInstance = self.__process_env_inst(json.loads(response.read()))
            envInstance.to_string()
