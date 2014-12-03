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
    def __init__(self, att_name, att_value, att_type):
        self.key = att_name
        self.value = att_value
        self.type = att_type


class Product:
    def __init__(self, product_name, product_description=''):
        self.name = product_name
        self.description = product_description
        self.attributes = []
        self.metadatas = []

    def add_attribute(self, attribute):
        self.attributes.append(attribute)

    def add_attributes(self, attributes):
        self.attributes = attributes

    def add_metadata(self, metadata):
        self.metadatas.append(metadata)

    def add_metadatas(self, metadatas):
        self.metadatas = metadatas

    def to_product_xml(self):
        product = Element('product')
        name = SubElement(product, 'name')
        name.text = self.name
        description = SubElement(product, "description")
        description.text = self.description
        if len(self.attributes) == 0:
            return tostring(product)
        for att in self.attributes:
            attribute = SubElement(product, "attributes")
            key = SubElement(attribute, "key")
            key.text = att.key
            value = SubElement(attribute, "value")
            value.text = att.value
            if att.type is not None:
                value = SubElement(attribute, "type")
                value.text = att.type
        for met in self.metadatas:
            metadata = SubElement(product, "metadatas")
            key = SubElement(metadata, "key")
            key.text = att.key
            value = SubElement(metadata, "value")
            value.text = met.value
        return product

    def to_product_xml_env(self):
        product = Element('productReleaseDtos')
        name = SubElement(product, 'productName')
        name.text = self.name
        description = SubElement(product, "productDescription")
        description.text = self.description
        version = SubElement(product, 'version')
        version.text = self.version

        if len(self.attributes) == 0:
            return tostring(product)
        for att in self.attributes:
            attribute = SubElement(product, "attributes")
            key = SubElement(attribute, "key")
            key.text = att.key
            value = SubElement(attribute, "value")
            value.text = att.value
            if att.type is not None:
                value = SubElement(attribute, "type")
                value.text = att.type
        for met in self.metadatas:
            metadata = SubElement(product, "metadatas")
            key = SubElement(metadata, "key")
            key.text = met.key
            value = SubElement(metadata, "value")
            value.text = met.value
        return product

    def to_string(self):
        var = self.name + "\t" + self.description + '\t' + self.version + '\t'
        for att in self.attributes:
            var = var + att.key + ':' + att.value
        print var


class ProductRelease:
    def __init__(self, product, product_version, attribute_list=None):
        self.version = product_version
        self.product = product
        self.attribute_list = []

    def add_atribute(self, attribute_key, attribute_value, attribute_type):
        attribute = Attribute(attribute_key, attribute_value, attribute_type)
        self.attribute_list.append(attribute)

    def add_attribute(self, attribute):
        self.attribute_list.append(attribute)

    def add_attributes(self, attributes):
        self.attribute_list = attributes

    def __eq__(self, other):
        return self.product == other.product\
        and self.version == other.version

    def __gt__(self, other):
        return self.product > other.product\
        or self.product == other.product and self.version > other.version

    def to_product_xml(self):
        product_release = Element('productReleaseDtos')
        version = SubElement(product_release, 'version')
        version.text = self.version
        return product_release

    def to_product_xml_env(self):
        product = Element('productReleaseDtos')
        name = SubElement(product, 'productName')
        name.text = self.product
        version = SubElement(product, 'version')
        version.text = self.version

        for att in self.attribute_list:
            attribute = SubElement(product, "attributes")
            key = SubElement(attribute, "key")
            key.text = att.key
            value = SubElement(attribute, "value")
            value.text = att.value
            if att.type is not None:
                value = SubElement(attribute, "type")
                value.text = att.type

        return product

    def to_string(self):
        var = self.product + "\t" + '\t' + self.version + '\t'
        for att in self.attribute_list:
            var = var + att.key + ':' + att.value
        print var

        ##
        ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
        ##


def parse_attribute_from_dict(attributes_dict):
    attribute_list = list()
    if isinstance(attributes_dict, list):
        for attribute in attributes_dict:
            attribute_list.append(Attribute(attribute['key'], attribute['value'], attribute['type']))
    else:
        attribute_list.append(Attribute(attributes_dict['key'], attributes_dict['value'], attributes_dict['type']))
    return attribute_list
