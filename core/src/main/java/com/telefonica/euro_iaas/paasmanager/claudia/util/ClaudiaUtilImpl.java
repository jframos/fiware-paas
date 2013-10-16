/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.claudia.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.claudia.client.CommandCreationException;
import com.telefonica.claudia.client.CommandFactory;
import com.telefonica.claudia.client.RestCommand;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaPutException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.InfrastructureManagerClaudiaImpl;
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

/**
 * @author jesus.movilla
 */
public class ClaudiaUtilImpl implements ClaudiaUtil {

    private SystemPropertiesProvider systemPropertiesProvider;

    /** The log. */
    private static Logger log = Logger.getLogger(InfrastructureManagerClaudiaImpl.class);

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

    /**
     * Obtain the xml corresponding to the GET to a url
     * 
     * @param url
     * @return the xml corresponding to do a GET to the url
     * @throws ClaudiaRetrieveInfoException
     */
    public String getClaudiaResource(PaasManagerUser user, String url, String type)
            throws ClaudiaResourceNotFoundException, ClaudiaRetrieveInfoException {
        Client client = new Client();
        String output = "";
        log.debug("url: " + url);
        try {

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(type);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                Map<String, String> header = getHeaders(user);
                for (String key : header.keySet()) {
                    builder = builder.header(key, header.get(key));
                }
            }
            ClientResponse response = builder.get(ClientResponse.class);
            output = response.getEntity(String.class);

            /*
             * ClientResponse response = wr.accept(type) .get(ClientResponse.class); output =
             * response.getEntity(String.class);
             */

        } catch (UniformInterfaceException e) {
            // if (e.getMessage().contains("status of 404")){
            String errorMessage = "UniformInterfaceException: " + " Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage);
            // }
        } catch (Exception e) {
            String errorMessage = "Error performing GET on the resource " + url;
            log.error(errorMessage);
            throw new ClaudiaRetrieveInfoException(errorMessage);
        }
        return output;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil#postClaudia (java.lang.String,
     * java.lang.String)
     */
    public ClientResponse postClaudiaResource(PaasManagerUser user, String url, String payload)
            throws ClaudiaRetrieveInfoException {
        Client client = new Client();
        log.debug("url: " + url);
        ClientResponse response = null;

        try {
            /*
             * WebResource wr = client.resource(url); response = wr.accept("application/xml").type("application/xml").
             * entity(payload).post(ClientResponse.class);
             */

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML).entity(payload);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                Map<String, String> header = getHeaders(user);
                for (String key : header.keySet()) {
                    builder = builder.header(key, header.get(key));
                }
            }

            response = builder.post(ClientResponse.class);

        } catch (Exception e) {
            String errorMessage = "Error performing post on the resource: " + url + " with payload: " + payload;
            log.error(errorMessage);
            throw new ClaudiaRetrieveInfoException(errorMessage);
        }
        return response;
    }

    public void deleteClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException {
        Client client = new Client();
        log.debug("url: " + url);
        try {
            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML);
            // if
            // (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
            // {
            Map<String, String> header = getHeaders(user);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
                // }
            }
            builder.delete(ClientResponse.class);
        } catch (Exception e) {
            String errorMessage = "Error performing DELETE on the resource: " + url;
            log.error(errorMessage);
            throw new ClaudiaRetrieveInfoException(errorMessage);
        }
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
            log.error("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public ClientResponse postClaudiaResource(PaasManagerUser user, String url) throws ClaudiaRetrieveInfoException {

        Client client = new Client();
        log.debug("url: " + url);
        ClientResponse response = null;
        try {

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_XML).type(MediaType.TEXT_PLAIN);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                Map<String, String> header = getHeaders(user);
                for (String key : header.keySet()) {
                    builder = builder.header(key, header.get(key));
                }
            }

            response = builder.post(ClientResponse.class);

            // WebResource wr = client.resource(url);
            // response =
            // wr.accept("application/xml").type("text/plain").post(ClientResponse.class);

        } catch (Exception e) {

            String errorMessage = "Error performing post on the resource: " + url;
            log.error(errorMessage);
            throw new ClaudiaRetrieveInfoException(errorMessage);
        }
        return response;
    }

    public ClientResponse putClaudiaResource(String url, String fqn) throws ClaudiaPutException {
        Client client = new Client();
        log.debug("url: " + url);
        ClientResponse response = null;
        try {
            WebResource wr = client.resource(url);

            response = wr.accept("text/plain").type("text/plain").entity(fqn).put(ClientResponse.class);

            log.debug(response);

        } catch (Exception e) {
            String errorMessage = "Error performing put on the resource: " + url + " with fqn: " + fqn;
            log.error(errorMessage);
            throw new ClaudiaPutException(errorMessage);
        }
        return response;

    }

    private Map<String, String> getHeaders(PaasManagerUser user) {

        /*
         * PaasManagerUser user = (PaasManagerUser) SecurityContextHolder
         * .getContext().getAuthentication().getPrincipal();
         */

        Map<String, String> headers = new HashMap<String, String>();

        headers.put(SystemPropertiesProvider.TCLOUD_METADATA_USER, user.getUsername());
        headers.put(SystemPropertiesProvider.TCLOUD_METADATA_TOKEN, user.getToken());
        headers.put(SystemPropertiesProvider.TCLOUD_METADATA_TENANT, user.getTenantId());

        return headers;

    }

}
