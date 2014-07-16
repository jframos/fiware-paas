#! /usr/bin/env python
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

from tools.productrinstanceequest import ProductInstanceRequest

if __name__ == "__main__":


# total = len(sys.argv)

    # Get the arguments list
    #cmd_args = sys.argv
    total = 3
    cmd_args = []
    cmd_args.append('hoddla')
    cmd_args.append('add')
    #cosmos_injection_node
    #cosmos_master_node
    #cosmos_slave_node
    cmd_args.append('ddfa24523-tiernamd-1-000115')
    cmd_args.append('tomcat')
    cmd_args.append('6')
    cmd_args.append('sdccoregroupid=cluster_name;cluster_name=test;openports=50030 50031 14000')



    # Print it
    if total < 2 or total > 6:
        print 'Error in the arguments. Please use --help for obtaining help'

    if cmd_args[1].find("help"):
        print "use --syncronize or --add_project_quota idtenant role (campus, normal, advance)"

    config = {}
    execfile("sdc.conf", config)

    g = ProductInstanceRequest(config['keystone_url'], config['sdc_url'], config['tenant'], config['user'],
        config['password'], config['vdc'])

    if cmd_args[1].find("list") != -1:
        g.get_product_instances()

    elif cmd_args[1].find("delete") != -1:
        g.delete_product(cmd_args[2])
    elif cmd_args[1].find("add") != -1:
        g.deploy_product(cmd_args[2], cmd_args[3], cmd_args[4], cmd_args[5])
    elif cmd_args[1].find("show") != -1:
        g.get_product_info(cmd_args[2], cmd_args[3])





