/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.claudia.util;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public interface ClaudiaUtil {

    // VAPP VM DOCUMENT
    public final static String IPADRESS_NAMESPACE = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_IPProtocolEndpoint";
    public final static String IPADDRESS_NODENAME = "IPv4Address";
    public final static String IPV6ADDRESS_NODENAME = "Address";
    public final static String OVFITEM_NODENAME = "ovf:Item";
    public final static String OS_NODENAME = "ovf:OperatingSystemSection";
    public final static String OS_ID_ATTRIBUTENAME = "ovf:id";
    public final static String OVF_TYPE = "application/xml";
    public final static String OVFITEM_CONNECTION_NODENAME = "rasd:Connection";

    // VAPP Service Document
    public final static String VAPP_NODENAME = "VApp";
    public final static String VM_STATUS_ATTRIBUTE = "status";
    public final static String NETWORK_NODENAME = "ovf:Network";
    public final static String NETWORK_PUBLIC_ATTRIBUTE = "rsrvr:public";
    public final static String NETWORK_NAME_ATTRIBUTE = "ovf:name";

    /**
     * @param parameters
     * @return
     * @throws URLNotRetrievedException
     */
    String getUrl(List<String> parameters) throws URLNotRetrievedException;

    /**
     * @param url
     * @return
     * @throws ClaudiaRetrieveInfoException
     * @throws ClaudiaResourceNotFoundException
     */
    String getClaudiaResource(PaasManagerUser user, String url, String type) throws ClaudiaRetrieveInfoException,
            ClaudiaResourceNotFoundException;

    /**
     * @param url
     * @param payload
     * @return
     * @throws ClaudiaRetrieveInfoException
     */
    ClientResponse postClaudiaResource(PaasManagerUser user, String url, String payload)
            throws ClaudiaRetrieveInfoException;

    // Escalabilidad, sobra uno de los 2
    ClientResponse postClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException;

    /**
     * @param url
     * @throws ClaudiaRetrieveInfoException
     */
    void deleteClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException;

    /**
     * @param xmlSource
     * @return Document
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException;

    /**
     * @param doc
     * @return the string representation of a doc
     * @throws TransformerException
     */
    public String domToString(Document doc) throws TransformerException;

    /**
     * @param Node
     * @return the string representation of a node
     */
    public String nodeToString(Node node);

    /**
     * @param url
     * @param fqn
     * @return response of the petition
     * @throws ClaudiaPutException
     */

}
