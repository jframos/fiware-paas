/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.claudia.client.CommandCreationException;
import com.telefonica.claudia.client.CommandFactory;
import com.telefonica.claudia.client.RestCommand;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaPutException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_BASEURL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_IP;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_PORT;

// import javax.ws.rs.core.MultivaluedMap;
// import com.sun.jersey.core.util.MultivaluedMapImpl;

// import javax.ws.rs.core.MultivaluedMap;
// import com.sun.jersey.core.util.MultivaluedMapImpl;

// import javax.ws.rs.core.MultivaluedMap;
// import com.sun.jersey.core.util.MultivaluedMapImpl;

// import javax.ws.rs.core.MultivaluedMap;
// import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author jesus.movilla
 */
public class ClaudiaDummyUtilImpl implements ClaudiaUtil {

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(ClaudiaDummyUtilImpl.class);

    /**
     * Return the url of the corresponding Claudia Resource
     * 
     * @param parameters
     * @return the URL where to invoke to obtain info about Claudia Resources
     * @throws URLNotRetrievedException
     */
    public String getUrl(List<String> parameters) throws URLNotRetrievedException {

        CommandFactory cf = new CommandFactory();
        cf.initiateFactory();
        RestCommand cr;

        String command = "";

        if (parameters.size() == 2)
            command = command + "browseVDC ";
        else if (parameters.size() == 3)
            command = command + "browseService ";
        else if (parameters.size() == 4)
            command = command + "browseVM ";

        for (int i = 0; i < parameters.size(); i++) {
            command = command + parameters.get(i) + " ";
        }
        command = command.substring(0, command.length() - 1);

        try {
            cr = (RestCommand) cf.createCommand(command);
            String actionUri = cr.getActionUri();

            return MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        } catch (CommandCreationException e) {
            throw new URLNotRetrievedException();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil#postClaudia (java.lang.String,
     * java.lang.String)
     */
    public ClientResponse postClaudiaResource(PaasManagerUser user, String url, String payload)
            throws ClaudiaRetrieveInfoException {
        return null;
    }

    public void deleteClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException {
        return;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public ClientResponse postClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException {

        return null;
    }

    public ClientResponse putClaudiaResource(String url, String fqn) throws ClaudiaPutException {
        return null;

    }

    public Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

    public String domToString(Document doc) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(doc);

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);

        transformer.transform(source, result);
        return sw.toString();
    }

    public String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            log.warn("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public String getClaudiaResource(PaasManagerUser user, String url, String type)
            throws ClaudiaRetrieveInfoException, ClaudiaResourceNotFoundException {
        String payload = null;
        try {
            payload = readFile(systemPropertiesProvider.getProperty("vappTestServiceLocation"));
        } catch (FileUtilsException e) {
            throw new ClaudiaResourceNotFoundException("Error in the Claudia Dummy Utils " + e.getMessage());
        }
        return payload;
    }

    public SystemPropertiesProvider getSystemPropertiesProvider() {
        return this.systemPropertiesProvider;
    }

    public String readFile(String property) throws FileUtilsException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(property))));
        } catch (FileNotFoundException e) {

            throw new FileUtilsException("The file " + property + "is not found");
        }
        StringBuffer ruleFile = new StringBuffer();
        String actualString = null;

        try {
            while ((actualString = reader.readLine()) != null) {
                ruleFile.append(actualString + "\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new FileUtilsException("Error in reading the file " + property);
        }

        return ruleFile.toString();
    }

}
