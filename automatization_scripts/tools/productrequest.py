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

__author__ = 'henar'

from productrelease import ProductRelease, Product
import sys

import json
import http
from productrelease import Attribute
from xml.etree.ElementTree import tostring

###
### http://docs.openstack.org/developer/glance/glanceapi.html
class ProductRequest:
    def __init__(self, keystone_url, sdc_url, tenant, user, password):
        self.keystone_url = keystone_url
        self.sdc_url = sdc_url
        self.user = user
        self.password = password
        self.tenant = tenant
        #self.token = self.__get__token()
        self.products = []
        print "init"

    def __get__token(self):
        self.token = http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    def add_product(self, product_name, product_description, attributes, metadatas):
        url = "%s/%s" % (self.sdc_url, "catalog/product")
        headers = {'Content-Type': 'application/xml'}

        attributes = self.__process_attributes(attributes)
        metadatas = self.__process_attributes(metadatas)

        product = Product(product_name, product_description)

        for att in attributes:
            product.add_attribute(att)

        for meta in metadatas:
            product.add_metadata(meta)

        payload = product.to_product_xml()

        response = http.post(url, headers, tostring(payload))

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200:
            print 'error to add the product sdc ' + str(response.status)
            sys.exit(1)
        else:
            self.products.append(product)


    def add_product_release(self, product_name, version):
        url = "%s/%s/%s/%s" % (self.sdc_url, "catalog/product", product_name, "release")
        headers = {'Content-Type': 'application/xml'}

        #  product = self.get_product_info (product_name)
        product = Product(product_name)
        product_release = ProductRelease(product, version)

        payload = product_release.to_product_xml()
        response = http.post(url, headers, tostring(payload))

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200:
            print 'error to add the product release to sdc ' + str(response.status)
            sys.exit(1)
        else:
            self.products.append(product)

    def __process_attributes(self, attributes_string):
        attributes = []
        if attributes_string == None:
            return attributes
        atts = attributes_string.split(';')
        for att in atts:
            a = att.split('=')

            attribute = Attribute(a[0], a[1])
            attributes.append(attribute)
        return attributes

    def get_product_release(self, product_name):
        #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        #get product release
        url = "%s/%s/%s/%s" % (self.sdc_url, "catalog/product", product_name, "release" )

        response = http.get(url, headers)

        if response.status != 200:
            print 'error to get the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return None

            return data['productRelease']['version']

    def get_product_release_info(self, product_name, product_version):
        #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        #get product release
        url = "%s/%s/%s/%s/%s" % (self.sdc_url, "catalog/product", product_name, "release", product_version )

        response = http.get(url, headers)

        if response.status != 200:
            print 'error to get the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return None
            product = ProductRelease(data['product']['name'], data['version'], data['product']['description'])
            try:
                for att in data['attributes']:
                    attribute = Attribute(att['key'], att['version']);
                    product.add_attribute(attribute)
            except:
                pass

            return product

    def get_product_info(self, product_name):
        #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        #get product release
        url = "%s/%s/%s" % (self.sdc_url, "catalog/product", product_name )

        response = http.get(url, headers)

        if response.status != 200:
            print 'error to get the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            print data
            if data == None:
                return None
            product = Product(data['name'], data['description'])
            try:
                for att in data['attributes']:
                    attribute = Attribute(att['key'], att['version']);
                    product.add_attribute(attribute)
            except:
                pass

            return product

    def delete_product_release(self, product_name, version):
        #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        #get product release
        url = "%s/%s/%s/%s/%s" % (self.sdc_url, "catalog/product", product_name, "release", version)

        response = http.delete(url, headers)

        if response.status != 200 and response.status != 204:
            print 'error to delete the product release ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            pass


    def delete_product(self, product_name):
        version = self.get_product_release(product_name);

        if version != None:
            self.delete_product_release(product_name, version);
            #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        #get product release
        url = "%s/%s" % (self.sdc_url, "catalog/product/" + product_name)
        response = http.delete(url, headers)

        if response.status != 200 and response.status != 204:
            print 'error to delete the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)

    def get_products(self):
        url = "%s/%s" % (self.sdc_url, "catalog/product")
        headers = {'Accept': "application/json"}
        #headers={'X-Auth-Token': self.token,
        #         'Accept': "application/json"}
        response = http.get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200:
            print 'error to obtain the token'
            sys.exit(1)
        else:
            data = json.loads(response.read())

            products_string = data["product"]

            for product_string in products_string:
                product = Product(product_string['name'])

                try:
                    attributes = product['attributes']
                    var = var + '  atts:'
                    for att in attributes:
                        var = var + '\t' + att['key'] + ":" + att['value']
                except:
                    pass
                try:
                    metadatas = product['metadatas']
                    var = var + '  metas:'
                    for meta in metadatas:
                        var = var + '\t' + meta['key'] + ":" + meta['value']
                except:
                    pass
                print var

                #   dom = parseString(res_data)
                #  xml_products = (dom.getElementsByTagName('product'))


                #   product_name =
                #   product = Product ()
                #   add_product


    ##
    ## products - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def get_products(self):
        url = "%s/%s" % (self.sdc_url, "catalog/product")
        #headers={'X-Auth-Token': self.token,
        #     'Accept': "application/json"}
        headers = {'Accept': "application/json"}
        print url
        response = http.get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200:
            print 'error to obtain the token'
            sys.exit(1)
        else:
            data = json.loads(response.read())

            products = data["product"]

            for product in products:
                var = product['name']
                prod = Product(product['name'])
                try:
                    attributes = product['attributes']
                    var = var + '  atts:'
                    for att in attributes:
                        attri = Attribute(att['key'], att['value'])
                        prod.add_attribute(attri)
                        var = var + '\t' + att['key'] + ":" + att['value']
                except:
                    pass
                try:
                    metadatas = product['metadatas']
                    var = var + '  metas:'
                    for meta in metadatas:
                        var = var + '\t' + meta['key'] + ":" + meta['value']
                except:
                    pass
                print var


                # print g.products
                # p = Product('contextbroker-standalon','contextbroker-standalon','1.0.0')
                #p = Product('cosmos_slave_node','cosmos_slave_node','0.9.0')
                #g.add_product(p)
                #p = Product('cosmos_master_node','cosmos_master_node','0.9.0')
                #g.add_product(p)
                #p = Product('cosmos_injection_node','cosmos_injection_node','0.9.0')
                #g.add_product(p)
