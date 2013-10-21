#!/usr/bin/env python
__author__ = 'henar'


from productrequest import ProductRequest
import sys

if __name__ == "__main__":


    total = len(sys.argv)

    # Get the arguments list
    cmd_args = sys.argv
    #total = 3
    # cmd_args = []
    # cmd_args.append('hola')
    # cmd_args.append('list')
    #cosmos_injection_node
    #cosmos_master_node
    #cosmos_slave_node
    # cmd_args.append('otro10')
    # cmd_args.append('0.1')
    # cmd_args.append('descrition')
    # cmd_args.append(None)
 #   cmd_args.append('sdccoregroupid=cluster_name;cluster_name=test;openports=50030 50031 14000')


    config = {}
    execfile("sdc.conf", config)

    g=ProductRequest(config['keystone_url'], config['sdc_url'], config['tenant'], config['user'], config['password'])


    if cmd_args[1].find("product-list") != -1:
        g.get_products()
    elif cmd_args[1].find("product-release-list") != -1:
        g.get_products()
    elif cmd_args[1].find("product-delete") != -1:
        g.delete_product(cmd_args[2])
    elif cmd_args[1].find("product-add") != -1:
        args = None
        metadata = None
        description = ''
        product_name = ''
        product_version = ''
        index = 0
        for cmd in cmd_args:
            if cmd == '--args':
                args = cmd_args[index+1]
            elif cmd == '--metadatas':
                metadata = cmd_args[index+1]
            elif cmd == '--name':
                product_name = cmd_args[index+1]
            elif cmd == '--version':
                product_version = cmd_args[index+1]
            index = index +1

        g.add_product(product_name,description,args, metadata)
        if len(product_version) != 0:
            g.add_product_release(product_name,product_version)
    elif cmd_args[1].find("show") != -1:
        g.get_product_info(cmd_args[2],cmd_args[3])
    elif cmd_args[1].find("demo") != -1:
        print "python sdc-catalog.py product-add --name henar5 --args 'username=hola;otro=2' --version 3"





