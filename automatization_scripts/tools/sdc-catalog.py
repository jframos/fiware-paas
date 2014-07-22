#!/usr/bin/env python
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

from productrequest import ProductRequest
import sys
import getopt
import argparse

def usage():
    print """
    *****************USAGE******************
    ACTIONS:
        product-add -n <name> -v <version> -m <metadata> -a <arguments> -d <description>
        product-delete -n <name> [-v <version>]
        product-list
        show -n <name> -v <version>

    OPTIONS:
        --help -h:		Usage help   
        --name -n :   		Product Name
        --description -d :	Product Description
        --version -v:		Product Release version
	--metadata -m:		Product Metadata
	--arguments -a:		Product Arguments

    EXAMPLE:
	python sdc-catalog.py product-add -a 'openports=1026 27017 27018 27019 28017; key1=valuekey1' -n orion -v 0.6.0
    """
    sys.exit();

if __name__ == "__main__":
    if (len(sys.argv) < 2 ):
        usage();
        #definimos las opciones
    name = ''
    version = ''
    description = ''
    arguments = ''
    metadata = ''

    parser = argparse.ArgumentParser()
    #argumento obligatorio
    parser.add_argument("option", type=str, help="type of action")
    #argumento opcional
    parser.add_argument("-n", "--name", help="product name")
    parser.add_argument("-d", "--description", help="product description")
    parser.add_argument("-a", "--arguments", help="product atributes")
    parser.add_argument("-m", "--metadata", help="product metadata")
    parser.add_argument("-v", "--version", help="product version")

    args = parser.parse_args()

    if args.name:
        name = args.name
    if args.version:
        version = args.version
    if args.description:
        description = args.description
    if args.name:
        metadata = args.metadata
    if args.name:
        arguments = args.arguments

    config = {}
    execfile("sdc.conf", config)
    g = ProductRequest(config['keystone_url'], config['sdc_url'], config['tenant'], config['user'], config['password'])

    if args.option == "product-list":
        g.get_products()

    elif args.option == "product-delete":
        if (name == ''):
            usage()
        if (version != ''):
            g.delete_product_release(name, version)
        else:
            g.delete_product(name)

    elif args.option == "product-add":
        if (name == ''):
            usage()
        else:
            g.add_product(name, description, arguments, metadata)
            if (version != ''):
                g.add_product_release(name, version)

    elif args.option == "show":
        if ((name != '') & (version != '')):
            g.get_product_info(name, version)
        else:
            usage();

