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

from xml.etree.ElementTree import Element, SubElement


class EnvironmentInstance:
    def __init__(self, blueprint_name, blueprint_description, environment=None, status='INIT'):
        self.blueprint_name = blueprint_name
        self.blueprint_description = blueprint_description
        self.status = status
        self.environment = environment
        self.tiers = []

    def add_environment(self, environment):
        self.environment = environment

    def get_environment(self):
        return self.environment

    def to_xml(self):
        blueprint_dto = Element('environmentInstanceDto')
        name = SubElement(blueprint_dto, "blueprintName")
        name.text = self.blueprint_name
        description = SubElement(blueprint_dto, "description")
        description.text = self.blueprint_description
        blueprint_dto.append(self.environment.to_xml())
        return blueprint_dto

    def to_string(self):
        var = str(self.name).upper()
        for tier in self.tiers:
            var = var + '\t' + tier.name
            for product in tier.products:
                var = var + '\t' + product.name + '\t' + product.version
        print var

    def add_tiers(self, tiers):
        self.tiers.extend(tiers)

    def get_tiers(self):
        return self.tiers
