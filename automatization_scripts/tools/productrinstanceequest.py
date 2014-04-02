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
from tools import http

__author__ = 'henar'

from tools.productrelease import ProductRelease
import sys

import json

from tools.productinstance import ProductInstance
from tools.productrelease import Attribute
from tools.productinstancedto import ProductInstanceDto
from tools.productinstancedto import ProductReleaseDto
from tools.productrequest import ProductRequest
from xml.etree.ElementTree import tostring

###
### http://docs.openstack.org/developer/glance/glanceapi.html
class ProductInstanceRequest:

    def __init__(self, keystone_url, sdc_url,tenant,user,password, vdc):

        self.keystone_url=keystone_url
        self.sdc_url=sdc_url
        self.user=user
        self.password=password
        self.tenant = tenant
        self.token = self.__get__token()
        self.vdc =vdc
        self.products = []

    def __get__token (self):
        self.token = http.get_token(self.keystone_url+'/tokens',self.tenant, self.user, self.password)

    def deploy_product(self, ip, product_name, product_version, attributes_string):
        url="%s/%s/%s/%s" %(self.sdc_url,"vdc", self.vdc,"productInstance")
        headers={'Content-Type': 'application/xml', 'Accept': 'application/json'}

        productrequest=ProductRequest(self.keystone_url, self.sdc_url,self.tenant, self.user, self.password)

        productrequest.get_product_info ( product_name)
        attributes = self.__process_attributes(attributes_string)

        product_release = ProductReleaseDto (product_name, product_version)

        productInstanceDto = ProductInstanceDto(ip,product_release,attributes)
        payload=productInstanceDto.to_xml()

        response= http.post(url,headers, tostring(payload))

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to add the product sdc ' + str (response.status)
            sys.exit(1)
        else:
            http.processTask (headers,json.loads(response.read()))

    def __process_attributes (self, attributes_string):
        attributes = []
        atts = attributes_string.split(';')
        for att in atts:

            a = att.split ('=')

            attribute = Attribute (a[0],a[1])
            attributes.append(attribute)
        return attributes

    def get_product_instances(self):
        url="%s/%s/%s/%s" %(self.sdc_url,"vdc", self.vdc,"productInstance")
        print url

        headers={'X-Auth-Token': self.token,
                 'Accept': "application/json"}
        response= http.get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to obtain the token'
            sys.exit(1)
        else:
            data = json.loads(response.read())

            products=data["productInstance"]
            print products

            if isinstance(products,list):
                for product in products:
                    print product
                    product_release = ProductRelease (product['productRelease']['product']['name'],product['productRelease']['version'], product['productRelease']['product']['description'])
                    productInstance = ProductInstance (product['vm']['hostname'], product['status'],product['vm']['ip'], product_release)
                    productInstance.to_string()
            else:
                product_release = ProductRelease (products['productRelease']['product']['name'],products['productRelease']['version'], products['productRelease']['product']['description'])
                productInstance = ProductInstance (products['vm']['hostname'], products['status'],products['vm']['ip'], product_release)
                productInstance.to_string()

