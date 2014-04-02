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

from xml.dom.minidom import parse, parseString
from productclass import Product
import sys, http
import time
import chef

from Multipart import Multipart

from urlparse import urlparse
import httplib, json

###
### http://docs.openstack.org/developer/glance/glanceapi.html
class OpenstackRequest:
    def __init__(self, token):
        self.url_openstack = 'http://130.206.80.63:8774/v2'
        self.keystone_url = "http://130.206.80.63:35357/v2.0"
        self.id_tenant = '6571e3422ad84f7d828ce2f30373b3d4'
        self.token = token


    def deploy_vm(self, name, os):
        url = "%s/%s/%s" % (self.url_openstack, self.id_tenant, "servers")
        print url
        headers = {'X-Auth-Token': self.token,
                   'Content-Type': "application/json"}

        if os == 'ubuntu':
            payload = '{"server": '\
                      '{"name": " ' + name + '", '\
                                             '"imageRef": "5babf339-0c4f-4947-a4cd-d87bd3b10a97", '\
                                             '"flavorRef": "2"}}'
        else:
            payload = '{"server": '\
                      '{"name": " ' + name + '", '\
                                             '"imageRef": "df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4", '\
                                             '"flavorRef": "2"}}'
        response = http.post(url, headers, payload)


        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 200 and response.status != 202:
            print 'error to deploy the vm ' + str(response.status)
            sys.exit(1)
        else:
            var = response.read()
            server = json.loads(var)
            self.id = server['server']['id']

        self.ip = self.while_till_deployed(self.id)
        return self.ip

    def get_vm(self, id):
        url = "%s/%s/%s/%s" % (self.url_openstack, self.id_tenant, "servers", id)

        headers = {'X-Auth-Token': self.token,
                   'Content-Type': "application/json"}
        response = http.get(url, headers)
        if response.status != 200 and response.status != 202:
            print url
            print str(response.status)
            print 'error to add the product'
            sys.exit(1)
        else:
            return response.read()
            #server = json.loads(var)
            #print server['server']['status']

    def while_till_deployed(self, id):
        vm_info = self.get_vm(id)
        server = json.loads(vm_info)
        status = server['server']['status']

        while status != 'ACTIVE' and status != 'ERROR':
            vm_info = self.get_vm(id)
            server = json.loads(vm_info)
            status = server['server']['status']
        vm_info = self.get_vm(id)
        server = json.loads(vm_info)
        addresses = server['server']['addresses']['private']
        ip = addresses[0]['addr']
        return ip


    def delete_vm(self):
        url = "%s/%s/%s/%s" % (self.url_openstack, self.id_tenant, "servers", self.id)
        headers = {'X-Auth-Token': self.token,
                   'Content-Type': "application/json"}
        response = http.delete(url, headers)


        ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
        if response.status != 204:
            print 'error deleting vm' + str(response.status) + response.reason
            sys.exit(1)
        else:
            print 'Deleting VM ........'
            print response.msg

    def install_software_in_node(self, node_name, product_name):
        api = chef.autoconfigure('knife.rb')
        node = chef.Node(node_name + '.novalocal')
        print node.exists
        if not node.exists:
            print 'node ' + node_name + '.novalocal'
            print 'no node in chef server'
            time.sleep(20)
        print node.exists
        if not node.exists:
            print 'node ' + node_name + '.novalocal'
            print 'no node in chef server'
            time.sleep(20)
        print node.exists
        if not node.exists:
            print 'Node not registed in the chef sever'
            sys.exit()
        node.run_list.append(product_name)
        node.save()
        print node
        return node

    def remove_node(self, mynode):
        mynode.delete()


    def test(self, product):
        m = Multipart()
        m.field(product, 'searchish term')
        m.file('greet', 'greet.txt', 'Hello multipart world!', {'Content-Type': 'text/text'})
        ct, body = m.get()

        print body
        print ct
        h = httplib.HTTP("130.206.80.119", "8081")
        h.putrequest('POST', "sdc2/rest/catalog/product")
        h.putheader('content-type', ct)
        h.putheader('content-length', str(len(body)))
        h.endheaders()
        h.send(body)
        errcode, errmsg, headers = h.getreply()
        print errcode
        return h.file.read()

    def connect_ssh(self, ip, os):
        print ip
        import paramiko, base64

        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        if os == 'ubuntu':
            ssh.connect(ip, username='root', password='2 b @bl3 2 3nt3r')
        else:
            ssh.connect(ip, username='root', password='f1-cl0ud')
        stdin, stdout, stderr = ssh.exec_command('chef-client')
        stdin.flush()
        print stderr
        result = ''
        for line in stdout:
            print line
            result = result + line.strip('\n')
        ssh.close()
        if "FATAL" in result:
            print 'ERROR to execute the recipe'

####
#### PROGRAMA PRINCIPAL....
###
if __name__ == "__main__":
    #Coommand Line: name, os,cookbook, recipe
    if len(sys.argv) != 6:
        print "***ERROR***"
        print "Usage: openstack.py <node_name> <so> <cookbook> <recipe> <deleteVM [yes][no]>"
        print "Example: python openstack.py bmm06 centos orion 0.8.1_install no"
        print "***********"
        sys.exit(1)

    parts = sys.argv[4].split('.')
    if ((parts[len(parts) - 1]) == 'rb'):
        print "***ERROR***"
        print "Recipe extension should NOT be added: .rb"
        print "Usage: openstack.py <node_name> <so> <cookbook> <recipe> <deleteVM [yes][no]>"
        print "Example: python openstack.py bmm06 centos orion 0.8.1_install no"
        print "***********"
        sys.exit(1)

    node = sys.argv[1]
    os = sys.argv[2]
    if ( sys.argv[4] == ''):
        softwareInstall = sys.argv[3]
    else:
        softwareInstall = sys.argv[3] + '::' + sys.argv[4]
    print node
    token = http.get_token('http://130.206.80.63:35357/v2.0/tokens', 'admin', 'admin', 'openstack')
    print token
    g = OpenstackRequest(token)
    ip = g.deploy_vm(node, os)
    print ip
    time.sleep(60)
    mynode = g.install_software_in_node(node, softwareInstall)
    g.connect_ssh(ip, os)

    if (sys.argv[5] == 'yes'):
    #       g.remove_node(mynode)
        g.delete_vm()

    print '**********'
    print '   END    '
    print '**********'
