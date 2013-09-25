from tools import http

__author__ = 'henar'

from tools.productrelease import ProductRelease
import sys

import json
from tools.productrelease import Attribute

###
### http://docs.openstack.org/developer/glance/glanceapi.html
class ProductRequest:

    def __init__(self, keystone_url, sdc_url,tenant,user,password):

        self.keystone_url=keystone_url
        self.sdc_url=sdc_url
        self.user=user
        self.password=password
        self.tenant = tenant
        self.token = self.__get__token()
        self.products = []

    def __get__token (self):
        self.token = http.get_token(self.keystone_url+'/tokens',self.tenant, self.user, self.password)

    def add_product(self, product_name, product_version, product_description, attributes):
        url="%s/%s" %(self.sdc_url,"catalog/product")
        headers={'Content-Type': 'application/xml'}

        attributes = self.__process_attributes(attributes)

        product = ProductRelease(product_name,product_version,product_description
        )

        for att in attributes:


            product.add_attribute(att)

        payload=product.to_product_xml()


        response= http.post(url,headers, payload)



        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to add the product sdc'
            sys.exit(1)
        else:
            self.products.append(product)


    def __process_attributes (self, attributes_string):
        attributes = []
        atts = attributes_string.split(';')
        for att in atts:

            a = att.split ('=')

            attribute = Attribute (a[0],a[1])
            attributes.append(attribute)
        return attributes

    def get_product_release (self,product_name):
        headers={'X-Auth-Token': self.token,
                 'Accept': "application/json"}
        #get product release
        url="%s/%s/%s/%s" %(self.sdc_url,"catalog/product", product_name,"release" )

        response= http.get(url, headers)

        if response.status!=200:
            print 'error to get the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return None

            return data['productRelease']['version']

    def get_product_info (self, product_name, product_version):
        headers={'X-Auth-Token': self.token,
                 'Accept': "application/json"}
        #get product release
        url="%s/%s/%s/%s/%s" %(self.sdc_url,"catalog/product", product_name,"release",product_version )

        response= http.get(url, headers)

        if response.status!=200:
            print 'error to get the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return None
            product = ProductRelease(data['product']['name'], data['version'], data['product']['description'])
            try:
                for att in data['attributes']:
                    attribute = Attribute (att ['key'], att['version']);
                    product.add_attribute(attribute)
            except:
                pass

            return product


    def delete_product_release (self,product_name, version):
        headers={'X-Auth-Token': self.token,
                 'Accept': "application/json"}
        #get product release
        url="%s/%s/%s/%s/%s" %(self.sdc_url,"catalog/product", product_name, "release",version)

        response= http.delete(url, headers)

        if response.status!=200 and response.status!=204:
            print 'error to delete the product release ' + product_name + ' ' + str(response.status)
            sys.exit(1)
        else:
            pass


    def delete_product(self,product_name):
        version = self.get_product_release (product_name);

        if version != None:
            self.delete_product_release (product_name,version);
        headers={'X-Auth-Token': self.token,
                 'Accept': "application/json"}
        #get product release
        url="%s/%s" %(self.sdc_url,"catalog/product/"+product_name)
        response= http.delete(url, headers)

        if response.status!=200 and response.status!=204:
            print 'error to delete the product ' + product_name + ' ' + str(response.status)
            sys.exit(1)


    ##
    ## products - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def get_products(self):
        url="%s/%s" %(self.sdc_url,"catalog/product")

        headers={'X-Auth-Token': self.token,
             'Accept': "application/json"}
        response= http.get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to obtain the token'
            sys.exit(1)
        else:
            data = json.loads(response.read())

            products=data["product"]

            for product in products:
                var = product['name']
                try:
                    attributes = product['attributes']
                    for att in attributes:
                        var = var + '\t' + att['key']+":" +att['value']
                except:
                    pass
                print var

         #   dom = parseString(res_data)
          #  xml_products = (dom.getElementsByTagName('product'))


            #   product_name =
            #   product = Product ()
            #   add_product



   # print g.products
    # p = Product('contextbroker-standalon','contextbroker-standalon','1.0.0')
    #p = Product('cosmos_slave_node','cosmos_slave_node','0.9.0')
    #g.add_product(p)
    #p = Product('cosmos_master_node','cosmos_master_node','0.9.0')
    #g.add_product(p)
    #p = Product('cosmos_injection_node','cosmos_injection_node','0.9.0')
    #g.add_product(p)
