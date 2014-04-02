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

from enviornmentrequest import EnvironmentRequest
import sys

if __name__ == "__main__":
    total = len(sys.argv)

    # Get the arguments list
    cmd_args = sys.argv
    #total = 3
    #cmd_args = []
    #cmd_args.append('hola')
    #cmd_args.append('list')
    #cosmos_injection_node
    #cosmos_master_node
    #cosmos_slave_node
    #cmd_args.append('enviornmentname3')
    #cmd_args.append('mas10')

    #cmd_args.append('tomcat=6;mysql=1.2.4')
    #cmd_args.append('sdccoregroupid=cluster_name;cluster_name=test;openports=50030 50030 14000')


    config = {}
    execfile("sdc.conf", config)

    g = EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'],
        config['password'], config['vdc'], config['sdc_url'])

    if cmd_args[1].find("list") != -1:
        g.get_abstract_environments()

    elif cmd_args[1].find("delete") != -1:
        g.delete_abstract_environments(cmd_args[2])
    elif cmd_args[1].find("add-tier") != -1:
        g.add_abstract_tier_environment(cmd_args[2], cmd_args[3], cmd_args[4])
    elif cmd_args[1].find("add-product") != -1:
    #    g.add_tier_environment(cmd_args[2],cmd_args[3])
        pass
    elif cmd_args[1].find("add") != -1:
        tier = []
        metadata = None
        description = ''
        env_name = ''
        tier_name = ''
        product_information = ''
        product_version = ''
        index = 0
        for cmd in cmd_args:
            if cmd == '--name':
                env_name = cmd_args[index + 1]
            elif cmd == '--description':
                description = cmd_args[index + 1]
            elif cmd == '--tier':
                tier_name = cmd_args[index + 1]

            elif cmd == '--product':
                product_information = cmd_args[index + 1]
            index = index + 1

        if len(tier_name) == 0:
            g.add_abstract_environment(env_name, description)
        else:
            g.add_abstract_environment(env_name, description)
            g.add_abstract_tier_environment(env_name, tier_name, product_information)







