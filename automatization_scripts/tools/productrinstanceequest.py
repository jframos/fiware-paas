from tools import http

__author__ = 'henar'

from tools.productrelease import ProductRelease
import sys

import json

from tools.productinstance import ProductInstance
from tools.productrelease import Attribute
from tools.productinstancedto import ProductInstanceDto
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

    def deploy_product(self, ip, product_name, product_version, attributes):
        url="%s/%s/%s/%s" %(self.sdc_url,"vdc", self.vdc,"productInstance")
        headers={'Content-Type': 'application/xml'}

        productrequest=ProductRequest(self.keystone_url, self.sdc_url,self.tenant, self.user, self.password)

        product_release = productrequest.get_product_info ( product_name, product_version)
        attributes = self.__process_attributes(attributes)

        productInstanceDto = ProductInstanceDto(ip,product_release,attributes)


        payload=productInstanceDto.to_xml()
        print payload

        response= http.post(url,headers, tostring(payload))

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to add the product sdc'
            sys.exit(1)

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

            for product in products:
                product_release = ProductRelease (product['productRelease']['product']['name'],product['productRelease']['version'], product['productRelease']['product']['description'])
                productInstance = ProductInstance (product['vm']['hostname'], product['status'],product['vm']['ip'], product_release)
                productInstance.to_string()


