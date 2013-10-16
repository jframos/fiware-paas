#! /usr/bin/env python
__author__ = 'henar'

from tools.enviornmentrequest import EnvironmentRequest

if __name__ == "__main__":


   # total = len(sys.argv)

    # Get the arguments list
    #cmd_args = sys.argv
    total = 3
    cmd_args = []
    cmd_args.append('hola')
    cmd_args.append('list')
    #cosmos_injection_node
    #cosmos_master_node
    #cosmos_slave_node
    cmd_args.append('henar5')
    cmd_args.append('mas10')

    cmd_args.append('tomcat=6;mysql=1.2.4')
    cmd_args.append('sdccoregroupid=cluster_name;cluster_name=test;openports=50030 50030 14000')



    # Print it
    if total < 2 or total > 6:
        print 'Error in the arguments. Please use --help for obtaining help'

    if cmd_args[1].find("help"):
        print "use --syncronize or --add_project_quota idtenant role (campus, normal, advance)"

    config = {}
    execfile("sdc.conf", config)

    g=EnvironmentRequest(config['keystone_url'], config['paasmanager_url'], config['tenant'], config['user'],
                         config['password'], config['vdc'],config['sdc_url'])


    if cmd_args[1].find("list") != -1:
        g.get_abstract_environments()

    elif cmd_args[1].find("delete") != -1:
        g.delete_abstract_environments(cmd_args[2])
    elif cmd_args[1].find("add-tier") != -1:
        g.add_abstract_tier_environment(cmd_args[2],cmd_args[3],cmd_args[4])
    elif cmd_args[1].find("add-product") != -1:
    #    g.add_tier_environment(cmd_args[2],cmd_args[3])
        pass
    elif cmd_args[1].find("add") != -1:
        g.add_abstract_environment(cmd_args[2],cmd_args[3])






