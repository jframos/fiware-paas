import http

__author__ = 'henar'

import sys
from xml.etree.ElementTree import tostring
import json

from environment import Environment
from tier import Tier, Network
from productrelease import ProductRelease, Product
from productrequest import ProductRequest


class EnvironmentRequest:

    def __init__(self, keystone_url, paas_manager_url,tenant,user,password, vdc, image, sdc_url='' ):

        self.paasmanager_url=paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.environment = []
        self.keystone_url=keystone_url

        self.user=user
        self.password=password
        self.tenant = tenant
        self.image = image

        self.token = self.__get__token()
        self.environments = []

    def __get__token (self):
        return http.get_token(self.keystone_url+'/tokens',self.tenant, self.user, self.password)

    def __get_environments (self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
             'Accept': "application/json"}
        response= http.get(url, headers)
         ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to obtain the environment '+ str(response.status)
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return
            environments=data["environmentDto"]

            self.__process_enviorments (environments)

    def __get_environment (self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Accept': "application/json"}
        response= http.get(url, headers)
        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200:
            print 'error to obtain the environment'
            sys.exit(1)
        else:
            data = json.loads(response.read())
            if data == None:
                return

            environment =self.__process_environment(data)
            return environment

    def __delete_environments (self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Accept': "application/json"}
        response= http.delete(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200 and response.status!=204:
            print 'error to delete the environment ' + str(response.status)
            sys.exit(1)

    def __delete (self, url):
        #url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Accept': "application/json"}
        response= http.delete(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200 and response.status!=204:
            print 'error to delete the environment ' + str(response.status)
            sys.exit(1)

    def __add_environment(self, url,environment_payload):

        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Content-Type': "application/xml"}

        response= http.post(url, headers, environment_payload)
        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200 and response.status!=204:
            print 'error to add an environment ' + str(response.status)
            sys.exit(1)


    def __add_tier_environment(self, url, tier_payload):
        headers={'X-Auth-Token': self.token, 'Tenant-Id': self.vdc,
                 'Content-Type': "application/xml"}


        response= http.post(url, headers, tier_payload)


        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status!=200 and response.status!=204:
            print 'error to add a tier in an environment ' + str(response.status)
            sys.exit(1)

    def __process_product (self, product_information):

        products = []
        product_list = product_information.split(';')

        for prod in product_list:

            a = prod.split ('=')

            product = self.__check_product_exist(a[0],a[1])


            products.append(product)
        return products

    def __process_metwork (self, network_information):

        nets = []
        networks = network_information.split(';')

        for net in networks:
            object_net = Network (net)
            nets.append(object_net)
        return nets



    def __check_product_exist (self, product_name, product_version):

        #request=ProductRequest(self.keystone_url, self.sdc_url, self.tenant, self.user, self.password)
        #product = request.get_product_info(product_name,product_version)

      #  if product is None:
       #     print 'Error: the product ' +  product_name + ' ' + product_version + ' does not exit'
        product = ProductRelease (product_name, product_version)
        return product


    def get_abstract_environments(self):
        url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")

        self.__get_environments (url)

    def add_abstract_environment(self, environment_name, environment_description):
        url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment")

        env = Environment(environment_name,environment_description )

        payload=tostring(env.to_env_xml())
        self.__add_environment(url,payload)

    def add_environment(self, environment_name, environment_description):
        url="%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc", self.vdc, "environment")

        env = Environment(environment_name,environment_description )

        payload=tostring(env.to_env_xml())
        self.__add_environment(url,payload)


    def add_abstract_tier_environment(self, environment_name, tier_name, products_information):
        url="%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment",environment_name,"tier")
        tier = Tier (tier_name)
        if products_information:
            products = self.__process_product (products_information)
            for product in products:
                tier.add_product(product)

        payload=tostring(tier.to_tier_xml())
        self.__add_tier_environment(url, payload)

    def add_tier_environment(self, environment_name, tier_name, products_information=None):
        url="%s/%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc", self.vdc, "environment",environment_name,"tier")
        tier = Tier (tier_name, self.image)
        if products_information:
            products = self.__process_product (products_information)
            for product in products:
                tier.add_product(product)

        payload=tostring(tier.to_tier_xml())
        self.__add_tier_environment(url, payload)

    def add_tier_environment_network(self, environment_name, tier_name, products_information=None, networks=None):
        url="%s/%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc", self.vdc, "environment",environment_name,"tier")
        tier = Tier (tier_name)
        if products_information:
            products = self.__process_product (products_information)
            for product in products:
                tier.add_product(product)

        if networks:
            networks = self.__process_metwork(networks)
            for net in networks:
                tier.add_network(net)

        print tostring(tier.to_tier_xml())
        payload=tostring(tier.to_tier_xml())
        print payload
        self.__add_tier_environment(url, payload)

    def __add_product_to_tier (self,url,products_information):
        product = self.__process_product (products_information)
        payload=tostring(product[0].to_product_xml())

        self.__add_tier_environment(url, payload)

    def add_product_to_tier(self, environment_name, tier_name, products_information):
        url="%s/%s/%s/%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc",
                                     self.vdc, "environment",environment_name,"tier",tier_name,"productRelease")
        self.__add_product_to_tier(url, products_information)

    def add_abstract_product_to_tier(self, environment_name, tier_name, products_information):
        url="%s/%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE",  "environment",environment_name,"tier",tier_name,"productRelease")
        print url
        self.__add_product_to_tier(url, products_information)

    def delete_abstract_environments(self, environment_name):
        url="%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/environment",environment_name)
        self.__delete_environments (url)

    def delete_environment(self, environment_name):
        url="%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc", self.vdc, "environment",environment_name)

        self.__delete_environments (url)

    def delete_tier(self, environment_name, tier_name):
        url="%s/%s/%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "vdc", self.vdc, "environment",environment_name, "tier", tier_name)

        self.__delete (url)

    def delete_abstract_tier(self, environment_name, tier_name):
        url="%s/%s/%s/%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE", "environment",environment_name, "tier", tier_name)

        self.__delete (url)



            #   product_name =
                #   product = Product ()
                #   add_product

    def __process_enviorments (self,environments_list):
        environments = []
        for env in environments_list:
            environment = self.__process_environment (env)

            environments.append(environment)
        return environments

    def __process_environment (self,env):
        environment = Environment (env['name'],env['description'])
        try:
            tiers_string =env['tierDtos']
        except:
            environment.to_string()
            return environment

        if isinstance(tiers_string, list):
            for tier_string in tiers_string:
                tier = Tier (tier_string['name'], self.image)
                try:
                    products_string = tier_string['productReleaseDtos']

                    if isinstance(products_string, list):

                        for product_string in products_string:
                            product = ProductRelease (product_string['productName'], product_string['version'])
                            tier.add_product(product)
                    else:
                        product = ProductRelease (products_string['productName'], products_string['version'])
                        tier.add_product(product)

                except:
                    pass
                environment.add_tier(tier)
        else:
            tier = Tier (tiers_string['name'], self.image)

            try:
                products_string = tiers_string['productReleaseDtos']
                if isinstance(products_string, list):

                    for product_string in products_string:
                        producte = Product (product_string['productName'])
                        product_release = ProductRelease (producte, product_string['version'])
                        tier.add_product(product_release)
                else:
                    product = ProductRelease (products_string['productName'], products_string['version'])
                    tier.add_product(product)
                environment.add_tier(tier)
            except:
                print 'error'
                pass



        environment.to_string()
        return environment


    ##
    ## get_environment - Obtiene la lista de environments ---
    ##
    def get_environments(self):
        url="%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/vdc/"+self.vdc+ "/environment")

        self.__get_environments (url)

    def get_environment(self, environment_name):
        url="%s/%s/%s" %(self.paasmanager_url,"catalog/org/FIWARE/vdc/"+self.vdc+ "/environment",environment_name)
        print url

        env = self.__get_environment (url)
        return env
