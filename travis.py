#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import os.path
import xml.dom.minidom


class Config(object):
    def configureServer(self, home_dir=os.path.expanduser("~")):
        try:
            os.environ["TRAVIS_SECURE_ENV_VARS"]
        except KeyError:
            print "no secure env vars available, please declare it first"
            sys.exit()

        m2 = xml.dom.minidom.parse(home_dir + '/.m2/settings.xml')

        settings = m2.getElementsByTagName("settings")[0]

        mirrors = m2.createElement("mirrors")
        settings.appendChild(mirrors)

        mirror = m2.createElement("mirror")
        mirror_id = m2.createElement("id")
        mirror_id_text = m2.createTextNode("nexus")
        mirror_mirrorOf = m2.createElement("mirrorOf")
        mirror_mirrorOf_text = m2.createTextNode("*")
        mirror_url = m2.createElement("url")
        mirror_url_value = m2.createTextNode("http://130.206.80.169/nexus/content/groups/public")

        mirrors.appendChild(mirror)
        mirror_id.appendChild(mirror_id_text)
        mirror_mirrorOf.appendChild(mirror_mirrorOf_text)
        mirror_url.appendChild(mirror_url_value)

        mirror.appendChild(mirror_id)
        mirror.appendChild(mirror_mirrorOf)
        mirror.appendChild(mirror_url)

        serversNodes = settings.getElementsByTagName("servers")
        if not serversNodes:
            serversNode = m2.createElement("servers")
            settings.appendChild(serversNode)
        else:
            serversNode = serversNodes[0]

        sonatypeServerNode = m2.createElement("server")
        sonatypeServerId = m2.createElement("id")
        sonatypeServerUser = m2.createElement("username")
        sonatypeServerPass = m2.createElement("password")

        idNode = m2.createTextNode("sonatype-nexus-snapshots")
        userNode = m2.createTextNode(os.environ["SONATYPE_USERNAME"])
        passNode = m2.createTextNode(os.environ["SONATYPE_PASSWORD"])

        sonatypeServerId.appendChild(idNode)
        sonatypeServerUser.appendChild(userNode)
        sonatypeServerPass.appendChild(passNode)

        sonatypeServerNode.appendChild(sonatypeServerId)
        sonatypeServerNode.appendChild(sonatypeServerUser)
        sonatypeServerNode.appendChild(sonatypeServerPass)

        serversNode.appendChild(sonatypeServerNode)

        m2Str = m2.toxml()
        f = open(home_dir + '/.m2/mySettings.xml', 'w')
        f.write(m2Str)
        f.close()


def main(prog_args):
    config = Config()
    config.configureServer()

if __name__ == "__main__":
    sys.exit(main(sys.argv))

