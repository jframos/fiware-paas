'''
Created on 31/05/2013

@author: bmmanso
'''
import utils
import sys
from xml.dom.minidom import parseString

file= "C:\\Users\\bmmanso\\Desktop\\OVF\\pfc.xml"
vdc = '6571e3422ad84f7d828ce2f30373b3d4'
org = '4Caast'
environment_name = "pruebaPython"
domine = "109.231.77.132"
port = "8080"

resource_environment_instance = "/paasmanager/rest/envInst/org/"+org+"/vdc/"+vdc+"/environmentInstance"
try:
    dom = parseString((open(file, 'r')).read())
except:
    print("Error opening the OVF file:" + file)
    sys.exit(1)
try:
    dom.getElementsByTagName('ovf:VirtualSystemCollection')[0].setAttribute("ovf:id", environment_name)
    dom.getElementsByTagName('InstantiateOvfParams')[0].setAttribute("name", environment_name)
    payload = dom.toxml()
except:
    print("OVF not well formed")
    print("failed -> ovf:VirtualSystemCollection ovf:id")
    print("failed -> InstantiateOvfParams name")
    sys.exit(1)
    
print('Deploy an environment ' + environment_name )