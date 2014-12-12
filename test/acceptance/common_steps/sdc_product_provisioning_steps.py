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

__author__ = 'jfernandez'

from lettuce import world
from lettuce_tools.dataset_utils.dataset_utils import DatasetUtils

dataset_utils = DatasetUtils()


def product_is_created_in_sdc_with_attributes(step, product_name, product_version):
    """
    Lettuce Step. Register the product in SDC and save register data in the world (product_list_with_attributes).
    Attributes will be defined in the step dataset.
    :param step: Lettuce step data.
    :param product_name: Name of the product
    :param product_version: Version of the product
    :return: None
    """
    product_data = dict()
    product_data['name'] = product_name

    attribute_list = list()
    for dataset_row in step.hashes:
        attribute_list.append(dataset_utils.prepare_data(dataset_row))
    product_data['attributes'] = attribute_list
    world.product_list_with_attributes.append(product_data)

    # Create product in SDC
    world.product_sdc_request.create_product_and_release_with_attributes_and_installator(product_name, product_version,
                                                                                         attribute_list,
                                                                                         world.product_installator)


def product_is_created_in_sdc_with_metadatas(step, product_name, product_version):
    """
    Lettuce Step. Register the product in SDC and save register data in the world (product_list_with_attributes).
    Metadatas will be defined in the step dataset.
    :param step: Lettuce step data.
    :param product_name: Name of the product
    :param product_version: Version of the product
    :return: None
    """

    product_data = dict()

    # Look for the product in the list (retrieve it if this one is already created for this test)
    for produc_with_attributes in world.product_list_with_attributes:
        if produc_with_attributes['name'] == product_name:
            product_data = produc_with_attributes
            break

    if len(product_data) == 0:
        product_data['name'] = product_name

    metadata_list = list()
    for dataset_row in step.hashes:
        metadata_list.append(dataset_utils.prepare_data(dataset_row))
    product_data['metadatas'] = metadata_list
    world.product_list_with_attributes.append(product_data)

    # Create product in SDC
    world.product_sdc_request.create_product_and_release_with_metadatas(product_name, product_version, metadata_list)
