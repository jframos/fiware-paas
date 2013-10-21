/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
