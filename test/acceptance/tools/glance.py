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
from urlparse import urlparse
import httplib, json
from tools import http

###
### http://docs.openstack.org/developer/glance/glanceapi.html
class GlanceDemo:
    def __init__(self, keystone_url, tenant, user, password, glance_url):
        self.keystone_url = keystone_url
        self.tenant = tenant
        self.user = user
        self.password = password
        self.public_url = glance_url
        self.ks_token = self.__get__token()
        self.images = None


    def __get__token(self):
        return http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def get_images(self):
        url = "%s/%s" % (self.public_url, "images/detail")
        headers = {'X-Auth-Token': self.ks_token,
                   'Accept': "application/json"}
        response = self.__get(url, headers)

        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status == 200:
            res_data = response.read()
            self.images = json.loads(res_data)

            ##
            ## Obtengo los metadatos de una imagen -- Los devuelve como lista de tuplas... [(,),...(,)]

        ## Y los devuelve en la cabecera....
    ##
    ## Usa metodo -- GET http://130.206.80.63:9292/v1/images/<image_id>
    def metadata(self, image_id):
        url = "%s/%s/%s" % (self.public_url, "images", image_id)
        headers = {'X-Auth-Token': self.ks_token}

        response = self.__get(url, headers)
        metadata = response.getheaders()
        return metadata

    ##
    ## Pone/Cambia el valor de un metadato en la imagen. Esta informacion va en las cabeceras HTTP
    ##
    ##
    ## Usa metodo -- PUT http://130.206.80.63:9292/v1/images/<image_id>
    def put_metadata(self, image_id, metadata):
        url = "%s/%s/%s" % (self.public_url, "images", image_id)
        metadata['X-Auth-Token'] = self.ks_token
        response = http.put(url, metadata)
        print "PUT: ", response.status

    ##
    ## Metodo que lista algunos datos de las imagenes por pantalla.....
    ## OJO -- El filtro!!!
    ##
    def list_images(self):
        if self.images:
            for i in self.images['images']:
                res = ""
                ### Si es "sdc_aware",
                try:
                    res = "****" if i['properties']['sdc_aware'] == 'True' else ""
                except:
                    #No existe la clave => tira excepcion.....
                    pass
                print res, i['id'], i['name'], '\t\t\t', i['size']  ## Lista un par de datos por pantalla

####
####
#### PROGRAMA PRINCIPAL....
###
if __name__ == "__main__":
    config = {}
    execfile("sdc.conf", config)
    g = GlanceDemo(config['keystone_url'], config['tenant'], config['user'], config['password'], config['glance_url'])

    g.get_images()  ### Consulto el listado de las imagenes
    g.list_images() ### Las listo ---

    ###
    ### Pongo a 'True' (por poner un valor) esta propiedad -- Para la imagen sdc-template-paas
    ### Tiene que ser  x-image-meta-property-* para que no casque!!!
    g.put_metadata('44dcdba3-a75d-46a3-b209-5e9035d2435e', {'x-image-meta-property-sdc_aware': 'True'})

    print "---------------------------"
    ## Obtengo, por obtener los metadatos de una imagen.
    m = g.metadata('add95618-4f13-4e46-9a38-a86cf4be80dd')
    print m
