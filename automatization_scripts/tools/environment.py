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
from xml.etree.ElementTree import Element, SubElement, fromstring


class Environment:
    def __init__(self, environment_name, environment_description):
        self.name = environment_name
        self.description = environment_description
        self.tiers = []


    def to_env_xml(self):
        environment_dto = Element('environmentDto')
        name = SubElement(environment_dto, "name")
        name.text = self.name
        description = SubElement(environment_dto, "description")
        description.text = self.description

        for tier in self.tiers:
            tier_dto = tier.to_tier_xml()
            environment_dto.append(tier_dto)

        return environment_dto

    def to_xml(self):
        environment_dto = Element('environmentDto')
        name = SubElement(environment_dto, "name")
        name.text = self.name
        description = SubElement(environment_dto, "description")
        description.text = self.description

        for tier in self.tiers:
            tier_dto = tier.to_xml()
            environment_dto.append(tier_dto)

        return environment_dto

    def to_string(self):
        var = str(self.name).upper()
        for tier in self.tiers:
            var = var + '\t' + tier.name + '\t' + tier.region
            for product_release in tier.products:
                var = var + '\t' + product_release.product + '\t' + product_release.version
        print var

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def add_attribute(self, attribute):
        self.attributes.append(attribute)

    def add_tier(self, tier):
        self.tiers.append(tier)



