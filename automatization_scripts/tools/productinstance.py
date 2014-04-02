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

from xml.etree.ElementTree import Element, SubElement, tostring

class Attribute:
    def __init__(self, att_name, att_value):
        self.key = att_name
        self.value = att_value


class ProductInstance:
    def __init__(self, hostname, status, ip, product_release):
        self.hostname = hostname
        self.status = status
        self.ip = ip
        self.product_release = product_release

    def add_attribute(self, attribute):
        self.attributes.append(attribute)


    def to_string(self):
        var = self.hostname + "\t" + self.status + '\t' + self.ip + '\t' + self.product_release.name + '\t' + self.product_release.version
        print var

        ##
        ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
        ##

