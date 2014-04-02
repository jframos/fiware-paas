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


from xml.etree.ElementTree import Element, SubElement,tostring

class Attribute:
    def __init__(self, att_name, att_value):
        self.key=att_name
        self.value=att_value


class ProductReleaseDto:
    def __init__(self, name, version):
        self.name = name
        self.version = version

    def to_xml (self):
        product = Element('product')
        name = SubElement(product, 'name')
        name.text = self.name
        version = SubElement(product, 'version')
        version.text = self.version
        return product



class ProductInstanceDto:
    def __init__(self, ip,product_release, attributes):
        self.ip=ip
        self.product_release=product_release
        self.attributes=attributes

    def to_xml (self):
        product_instance = Element('productInstanceDto')
        product_instance.append(self.product_release.to_xml())
        vm = SubElement(product_instance, 'vm')
        ip = SubElement(vm, 'ip')
        ip.text = self.ip
        for att in self.attributes:
            attri = SubElement(product_instance, 'attributes')
            key = SubElement(attri, "key")
            key.text = att.key
            value = SubElement(attri, "value")
            value.text = att.value

        print product_instance
        return product_instance

