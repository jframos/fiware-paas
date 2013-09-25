from tools import http

__author__ = 'henar'

import sys
from xml.etree.ElementTree import tostring
import json

from tools.environment import Environment
from tools.tier import Tier
from tools.productrelease import ProductRelease
from tools.productrequest import ProductRequest


class EnvironmentInstanceRequest:

    def __init__(self, keystone_url, paas_manager_url,tenant,user,password, vdc, sdc_url=''):

        self.paasmanager_url=paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.environment = []
        self.keystone_url=keystone_url

        self.user=user
        self.password=password
        self.tenant = tenant

        self.token = self.__get__token()
        self.environments = []

    def __get__token (self):
        return http.get_token(self.keystone_url+'/tokens',self.tenant, self.user, self.password)

    def add_blueprint_instance (self, environment_instance):

        url="%s/%s%s/%s" %(self.paasmanager_url,"envInst/org/FIWARE/vdc", self.vdc,"environmentInstance")
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Accept': "application/json"}
        payload = environment_instance.to_xml()
        response= http.post(url, headers,tostring(payload))

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200 and response.status!=204:
            print 'error to deploy the environment ' + str(response.status)
            sys.exit(1)
