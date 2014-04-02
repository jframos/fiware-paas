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
import httplib
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import getDOMImplementation
from xml.etree.ElementTree import Element, SubElement, tostring
import sys



def doRequestHttpOperation(domine, port, resource, operation, data, headers):
  try:
    conn = httplib.HTTPConnection(domine,port)
 
    if operation == 'GET' or operation == 'DELETE':
      conn.request(operation, resource,None,headers)
    else:
      conn.request(operation, resource, data,headers)
    r1 = conn.getresponse()
    
    if  r1.status != 200 and  r1.status != 204:
      print ('   ERROR in the Operation' )
      print(r1.status,r1.reason)
      exit(1)
    #else:
     # print ('   Operation sucessfull')
    
    data1 = r1.read()
    return data1
  except httplib.HTTPException, e:
    print("An error has ocurred when connecting")
    print (e)
    sys.exit(1)
    

def obtainToken (user, password, project):
  payload='{"auth":{"tenantName":"'+project+'","passwordCredentials":{"username":"'+user+'","password":"'+password+'"}}}'
  headers = {'Content-Type': 'application/json',  'Accept':'application/xml'}
 
  data1 = doRequestHttpOperation("130.206.80.63", "35357", "/v2.0/tokens", 'POST',payload,headers)
  #print(data1)
  dom = parseString(data1)
  try:
    result = (dom.getElementsByTagName('token'))[0]
    token = result.attributes["id"].value

    #print(token)
    return token

  except:
	print ("Error in the processing enviroment")
	sys.exit(1)

    
def processTask (domine,port,headers,taskdom):
  try:
  
    dom = parseString(taskdom)
    task = (dom.getElementsByTagName('task'))[0]
    href = task.attributes["href"].value
    print (href)

    status = task.attributes["status"].value

  
    while status == 'RUNNING':
      data1 = doRequestHttpOperation(domine,port,href, 'GET',None,headers)

      dom = parseString(data1)
      task = (dom.getElementsByTagName('task'))[0]
      status = task.attributes["status"].value

    if status == 'ERROR':
      error = (dom.getElementsByTagName('error'))[0]
      message = error.attributes["message"].value
      majorErrorCode = error.attributes["majorErrorCode"].value
      print "ERROR : " + message + " " + majorErrorCode
    return status
  except:
    print ("Error in parsing the taskId")
    sys.exit(1)
    
def processProductInstanceStatus (productInstance):
  try:
    dom = parseString(productInstance)
    status = (dom.getElementsByTagName('status'))[0]
    return status.firstChild.nodeValue
  except:
    print ("Error in parsing the taskId")
    sys.exit(1)
    
def createProductInstanceDto (ip_string, fqn_string, product_name, product_version):
  impl = getDOMImplementation()
  productInstanceDto = Element('productInstanceDto')
  vm = SubElement(productInstanceDto, "vm")
  ip = SubElement(vm, "ip")
  ip.text = ip_string
  fqn = SubElement(vm, "fqn")
  fqn.text = fqn_string
  hostname = SubElement(vm, "hostname")
  hostname.text = "testchef2"
  domain = SubElement(vm, "domain")
  domain.text = ".test"

  product = SubElement(productInstanceDto, "product")
  name = SubElement(product, "name")
  name.text=product_name
  
  version = SubElement(product, "version")
  version.text=product_version

  return productInstanceDto
  
def createEnvironmentInstanceDto (environmentDto, bluerprintname):
	
  from xml.etree.ElementTree import XML, fromstring, tostring
  docEnvironmentDto = fromstring(environmentDto)

  environmentInstanceDto = Element('environmentInstanceDto')
  description = SubElement(environmentInstanceDto, 'description')
  description.text = 'description'
  blueprintName = SubElement(environmentInstanceDto, 'blueprintName')
  blueprintName.text = bluerprintname
  environmentInstanceDto.append(docEnvironmentDto)
 
  return environmentInstanceDto
  
def createCloneEnviornmentDto (environmentDto, enviornmentName, description):
	
  from xml.etree.ElementTree import XML, fromstring, tostring

  dom = parseString(environmentDto)

  for node in dom.getElementsByTagName('environmentDto')[0].childNodes:
    print node.nodeName
    if node.nodeName ==  'name':
      node.firstChild.nodeValue = enviornmentName
    if node.nodeName ==  'description':
      node.firstChild.nodeValue = description
  #dom.getElementsByTagName('description')[0].childNodes[0].nodeValue =description
  return dom.toxml()
  
def changeTier (tierDto, keypair, floatingip):
	
  from xml.etree.ElementTree import XML, fromstring, tostring

  dom = parseString(tierDto)

  for node in dom.getElementsByTagName('tierDto')[0].childNodes:
    print node.nodeName
    if node.nodeName ==  'keypair':
      node.appendChild(dom.createTextNode(keypair))
   
    if node.nodeName ==  'floatingip':
      node.appendChild(dom.createTextNode(floatingip)) 
  #dom.getElementsByTagName('description')[0].childNodes[0].nodeValue =description
  return dom.toxml()

	
def createEnvironmentDto (environment_name, tier_name, product_name, product_version):

  environmentDto = Element('environmentDto')
  name = SubElement(environmentDto, "name")
  name.text = environment_name
  description = SubElement(environmentDto, "description")
  description.text = "description"
  
  tierDtos = SubElement(environmentDto, "tierDtos")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text='tier16'+tier_name
  image_tier= SubElement(tierDtos, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  productVersion= SubElement(productReleaseDtos, "version")
  productVersion.text=product_version


  return environmentDto
  


def createEnvironmentDtoNoTiers (environment_name):
  impl = getDOMImplementation()
  environmentDto = Element('environmentDto')
  name = SubElement(environmentDto, 'name')
  name.text = environment_name
  description = SubElement(environmentDto, "description")
  description.text = "description"

  return environmentDto
  
def createEnvironment2TiersDto (environment_name, product_name, product_version, product_name2, product_version2):
  impl = getDOMImplementation()
  environmentDto = Element('environmentDto')
  name = SubElement(environmentDto, "name")
  name.text = environment_name
  description = SubElement(environmentDto, "description")
  description.text = "description enviroment " + environment_name
  
  tierDtos = SubElement(environmentDto, "tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text='tier16'+product_name
  image_tier= SubElement(tierDtos, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version
  
  tierDtos2 = SubElement(environmentDto, "tierDto")
  minimumNumberInstances = SubElement(tierDtos2, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos2, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos2, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos2, "name")
  name_tier.text='tier16'+product_name2
  image_tier= SubElement(tierDtos2, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos2, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos2, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos2, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos2, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name2
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version2

  return environmentDto
  
def createEnvironment3TiersDto (environment_name, tier_name, product_name, product_version, product_name2, product_version2,  product_name3, product_version3):
  impl = getDOMImplementation()
  environmentDto = Element('environmentDto')
  name = SubElement(environmentDto, "name")
  name.text = environment_name
  description = SubElement(environmentDto, "description")
  description.text = "description enviroment " + environment_name
  
  tierDtos = SubElement(environmentDto, "tierDtos")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text= tier_name + '1' + product_name
  image_tier= SubElement(tierDtos, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version
  
  tierDtos2 = SubElement(environmentDto, "tierDtos")
  minimumNumberInstances = SubElement(tierDtos2, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos2, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos2, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos2, "name")
  name_tier.text= tier_name + '2' + product_name2
  image_tier= SubElement(tierDtos2, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos2, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos2, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos2, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos2, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name2
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version2

  tierDtos3 = SubElement(environmentDto, "tierDtos")
  minimumNumberInstances = SubElement(tierDtos3, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos3, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos3, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos3, "name")
  name_tier.text= tier_name + '3' + product_name3
  image_tier= SubElement(tierDtos3, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos3, "flavour")
  flavour_tier.text='2'
  keypair= SubElement(tierDtos3, "keypair")
  keypair.text='testpaas'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos3, "floatingip")
  floatingip.text='false'
  productReleaseDtos = SubElement(tierDtos3, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name3
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version3
  
  return environmentDto
  
  
def createNoFIWAREEnvironmentDto (environment_name, product_name, product_version):
  impl = getDOMImplementation()
  environmentDto = Element('environmentDto')
  name = SubElement(environmentDto, "name")
  name.text = environment_name
  description = SubElement(environmentDto, "description")
  description.text = "description"
  tierDtos = SubElement(environmentDto, "tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text='tomcat_tier25'
  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version

  return environmentDto
  
def createTier( product_name, product_version):

  impl = getDOMImplementation()
 
  tierDtos = Element("tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text='tomcat_tier32'
  image_tier= SubElement(tierDtos, "image")
  image_tier.text='image_tier'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='flavour_tier'
  icono= SubElement(tierDtos, "icono")
  icono.text='icono'
  security_group= SubElement(tierDtos, "security_group")
  security_group.text='security_group'
  keypair= SubElement(tierDtos, "keypair")
  #keypair.text='testpaas'
  keypair.text='henar-test-2'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='floatingip'


  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version

  return tierDtos

def createProductRelease ( product_name, product_version):

  impl = getDOMImplementation()

  productReleaseDtos = Element("productReleaseDto")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version

  return productReleaseDtos
  
def createProductReleaseNode ( product_name, product_version):

  impl = getDOMImplementation()

  productReleaseDtos = Element("productReleaseDto")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version
  
  attributes = SubElement(productReleaseDtos, "attributes")
  attkey = SubElement(attributes, "key")
  attkey.text="application"
  attvalue = SubElement(attributes, "value")
  attvalue.text="nodejstest"
  
  attributes2 = SubElement(productReleaseDtos, "attributes")
  attkey2 = SubElement(attributes2, "key")
  attkey2.text="url_repo_git"
  attvalue2 = SubElement(attributes2, "value")
  attvalue2.text="https://github.com/hmunfru/nodejstest.git"

  return productReleaseDtos
  
  
def createTierNoProduct( tiername):

  impl = getDOMImplementation()
 
  tierDtos = Element("tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text=tiername
  image_tier= SubElement(tierDtos, "image")
  #image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  #image_tier.text='945689be-1231-4414-8c3e-09bcee4e8e63'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='2'
  icono= SubElement(tierDtos, "icono")
  icono.text='icono'
  security_group= SubElement(tierDtos, "security_group")
  security_group.text='security_group'
  keypair= SubElement(tierDtos, "keypair")
  keypair.text='henar-test-2'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='true'
  return tierDtos

def createTierNoProductFlavor( tiername, flavour):

  impl = getDOMImplementation()
 
  tierDtos = Element("tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text=tiername
  image_tier= SubElement(tierDtos, "image")
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text=flavour
  icono= SubElement(tierDtos, "icono")
  icono.text='icono'
  security_group= SubElement(tierDtos, "security_group")
  security_group.text='security_group'
 # keypair= SubElement(tierDtos, "keypair")
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='true'
  return tierDtos
  
def createTierProduct( tiername, product_name, product_version):

  impl = getDOMImplementation()
 
  tierDtos = Element("tierDto")
  minimumNumberInstances = SubElement(tierDtos, "minimumNumberInstances")
  minimumNumberInstances.text='1'
  initialNumberInstances = SubElement(tierDtos, "initialNumberInstances")
  initialNumberInstances.text='1'
  maximumNumberInstances = SubElement(tierDtos, "maximumNumberInstances")
  maximumNumberInstances.text='1'
  name_tier= SubElement(tierDtos, "name")
  name_tier.text=tiername
  image_tier= SubElement(tierDtos, "image")
  #image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  image_tier.text='44dcdba3-a75d-46a3-b209-5e9035d2435e'
  #image_tier.text='945689be-1231-4414-8c3e-09bcee4e8e63'
  flavour_tier= SubElement(tierDtos, "flavour")
  flavour_tier.text='2'
  icono= SubElement(tierDtos, "icono")
  icono.text='icono'
  security_group= SubElement(tierDtos, "security_group")
  security_group.text='security_group'
  keypair= SubElement(tierDtos, "keypair")
  keypair.text='henar-test-2'
  #keypair.text='jesusmovilla'
  floatingip= SubElement(tierDtos, "floatingip")
  floatingip.text='true'
  
  productReleaseDtos = SubElement(tierDtos, "productReleaseDtos")
  productName = SubElement(productReleaseDtos, "productName")
  productName.text=product_name
  
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version
  return tierDtos
  
def createApplicationDto (application_name, application_version, product_name, product_version, artifact_name):
  impl = getDOMImplementation()
  applicationReleaseDto = Element('applicationReleaseDto')
  applicationName = SubElement(applicationReleaseDto, "applicationName")
  applicationName.text = application_name
  applicationVersion = SubElement(applicationReleaseDto, "version")
  applicationVersion.text = application_version
  artifacts = SubElement(applicationReleaseDto, "artifactsDto")
  #artifact = SubElement(artifacts, "artifacts")
  name = SubElement(artifacts, "name")
  name.text=artifact_name
  path = SubElement(artifacts, "path")
  path.text=artifact_name
  productRelease = SubElement(artifacts, "productReleaseDto")
  #product = SubElement(productRelease, "product")
  name = SubElement(productRelease, "productName")
  name.text=product_name
  version = SubElement(productRelease, "version")
  version.text=product_version
  
  productReleaseDtos = SubElement(artifact, "ProductRelease")
  productName = SubElement(productReleaseDtos, "product")
  productName.text=product_name
  name = SubElement(productReleaseDtos, "name")
  name.text=product_name+"_"+product_version
  version = SubElement(productReleaseDtos, "version")
  version.text=product_version
  

  return applicationReleaseDto
  

  
  
   
 
#<application>
#<  <applicationName>recordmanagementsap</applicationName>
#<  <version>1.0</version>
 #< <artifacts>
 #< <artifact>
 #<   <name>thewarfile</name>
 #<   <attributes>
 #<      <key>webapps_url</key><value>http://Artefacts/WAR/tomcatFixedLocalPostgresDB/flipper.war</value>
 #<      <key>webapps_name</key><value>flipper.war</value>
 #<      <key>type</key><value>warr</value>
  #<   <productRelease>
  #<      <version>7.0</version>
  #<      <product>tomcat</product>
 #<   </productRelease> 
 #<   <antiafinity>the properties file </antiafinity>
    
#<    <product diferent al del ac the properties file>
#<  </artifact>


  
 
  
def createArtifactDto ( artifact_name,product_name, product_version):
  impl = getDOMImplementation()
  artifact = Element('artifactDto')
  name = SubElement(artifact, "name")
  name.text=artifact_name
  productRelease = SubElement(artifact, "productRelease")
  product = SubElement(productRelease, "product")
  name = SubElement(product, "name")
  name.text=product_name
  version = SubElement(product, "version")
  version.text=product_version
 
  
  
  return artifact



  

