PaaS Manager - User and Programmers Guide
______________________________________________

Introduction
============

Welcome the User and Programmer Guide for the PaaS Manager GE. 
This generic enabler is built on a proprietary solution using standard 
interface to communicate with and so where possible this guide points to 
the appropriate online content that has been created for this specific API. 
The online documents are being continuously updated and improved, and so 
will be the most appropriate place to get the most up to date information on using this interface.



Accessing PaaS Manager from the CLI 
==========

The access through the CLI is made using the curl program. Curl [http://curl.haxx.se/] is a client to get documents/files from or send documents to a server, using any of the supported protocols (HTTP, HTTPS, FTP, GOPHER, DICT, TELNET, LDAP or FILE) and therefore is also usable for OpenStack Compute API. Use the curl command line tool or use libcurl from within your own programs in C. Curl is free and open software that compiles and runs under a wide variety of operating systems.

The normal operations sequence to deploying an environment and an application on top of it could be summarized in the following list:


API Authentication
------------------
All the operations in the PaaS Manager API needs to have a valid token to access it. To obtain the token, you need to have an account in FIWARE Lab (account.lab.fi-ware.org).
With the credentials you can obtain a valid token

$ curl -v -H "Content-Type: application json" -H "Accept: application/json" -X
POST "http://cloud.lab.fi-ware.org:4731/v2.0/tokens" -d '{"auth":{"tenantName":
"yourtenantid","passwordCredentials":{"username":"youruser","password":"yourpassword"}}}'

You will receive the following answer, with a valid token (id).
  {
    access: {
       token: {
          expires: "2015-07-09T15:16:07Z"
          id: "756cfb31e062216544215f54447e2716"
          tenant: {
	  ..

For all the PaaS manager request, you will need to include the following header:
  X-Auth-Token: 756cfb31e062216544215f54447e2716
  Tenant-Id: tenantd id
  
  
 For the rest of the explanation, we are going to configure a set of variables:
    export PAAS_MANAGER_IP =  pegasus.lab.fi-ware.org
    export PAAS_MANAGER_IP
    
  
Catalogue Management API
------------------
Some operations related to the management are the software can involve:

'''1.- Get the blueprint template list from the catalogue'''

$ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
"X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: 000000000000000
00000000000000081" -X GET "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest
/catalog/org/FIWARE/environment"
