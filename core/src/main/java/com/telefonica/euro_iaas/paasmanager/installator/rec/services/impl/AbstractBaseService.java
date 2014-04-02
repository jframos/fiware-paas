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

package com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.InfrastructureManagerClaudiaImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author jesus.movilla
 */
public class AbstractBaseService {

    protected Client client;
    private String baseHost;
    private MediaType type;

    /** The log. */
    private static Logger log = Logger.getLogger(InfrastructureManagerClaudiaImpl.class);

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return the baseHost
     */
    public String getBaseHost() {
        return baseHost;
    }

    /**
     * @param baseHost
     *            the baseHost to set
     */
    public void setBaseHost(String baseHost) {
        this.baseHost = baseHost;
    }

    /**
     * @return the type
     */
    public MediaType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(MediaType type) {
        this.type = type;
    }

    public MultivaluedMap<String, String> addParam(MultivaluedMap<String, String> queryparams, String key, Object value) {
        if (value != null) {
            queryparams.add(key, value.toString());
        }
        return queryparams;
    }

    public Builder addCallback(Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }

    public Document getDocument(String xml) throws ProductInstallatorException {
        Document doc;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            doc = docBuilder.parse(is);
        } catch (ParserConfigurationException e1) {
            String msg = "Error parsing vsstring. Desc: " + e1.getMessage();
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "SAXException. Desc: " + e.getMessage();
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException. Desc: " + e.getMessage();
            throw new ProductInstallatorException(msg);
        }
        return doc;
    }

    public void checkRECTaskStatus(Response response) throws ProductInstallatorException {

        String msgerror = null;
        switch (response.getStatus().getCode()) {
            case 401: // Unauthorized
            case 403: // Forbidden
                msgerror = "RECManagerException: Error Code: " + response.getStatus().getCode() + "Desc: "
                        + response.getStatus().getDescription();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);

            case 400: // Bad Request
            case 404: // Not found
                msgerror = "RECManagerException: Error Code: " + response.getStatus().getCode() + "Desc: "
                        + response.getStatus().getDescription();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);

            case 412:
            case 415:
                msgerror = "RECManagerException: Error Code: " + response.getStatus().getCode() + "Desc: "
                        + response.getStatus().getDescription();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);

            case 501:
            case 500:
                msgerror = "RECManagerException: Error Code: " + response.getStatus().getCode() + "Desc: "
                        + response.getStatus().getDescription();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);
            case 202:
            case 201:
            case 200:
                log.info("Operation suceesfully done.");
                try {
                    String job = response.getEntity().getText();
                    while (true) {
                        String status = getTaskStatus(baseHost + "/jobs/" + job);
                        log.info("Status " + baseHost + "/jobs/" + job + " " + status);

                        if (status.equals("FINISHED"))
                            return;
                        else if (status.equals("PENDING")) {
                            log.debug("PENDING");
                        } else if (status.equals("FAILED")) {
                            msgerror = "RECManagerException. Desc : " + getErrorMessage(baseHost + "/jobs/" + job);
                            log.error(msgerror);
                            throw new ProductInstallatorException(msgerror);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            msgerror = "RECManagerException. ThreadInterruptedException";
                            log.error(msgerror);
                            throw new ProductInstallatorException(msgerror);
                        }
                    }
                } catch (IOException e1) {
                    msgerror = "RECManagerException. Error to obtain the job.";
                    log.error(msgerror);
                    throw new ProductInstallatorException(msgerror);
                }
                // break;
        }
    }

    private String getTaskStatus(String url) throws ProductInstallatorException {
        Document responseXml = getTask(url);

        if (responseXml == null)
            return "unknown";

        NodeList tasks = responseXml.getElementsByTagName("status");
        if (tasks.getLength() != 0) {
            // If the task is Success or Error, store the result.
            return ((Element) tasks.item(0)).getFirstChild().getNodeValue();
        }
        return "unknown";
    }

    private String getErrorMessage(String url) throws ProductInstallatorException {
        Document responseXml = getTask(url);

        if (responseXml == null)
            return "unknown";

        NodeList tasks = responseXml.getElementsByTagName("message");
        if (tasks.getLength() != 0) {
            // If the task is Success or Error, store the result.
            return ((Element) tasks.item(0)).getFirstChild().getNodeValue();
        }
        return "unknown";
    }

    private Document getTask(String url) throws ProductInstallatorException {
        // Check the state of the task
        Reference urlTask = new Reference(url);

        Response response = client.get(urlTask);
        String msgerror = "RECManagerException. Error obtaining task " + url + "." + " Desc "
                + response.getStatus().getDescription();

        switch (response.getStatus().getCode()) {

            case 401: // Unauthorized
            case 403: // Forbidden
                log.info(msgerror);
                throw new ProductInstallatorException(msgerror);

            case 400: // Bad Request
            case 404: // Not found
                log.info(msgerror);
                throw new ProductInstallatorException(msgerror);

            case 501:
            case 500:
                log.info(msgerror);
                throw new ProductInstallatorException(msgerror);
            case 202:
                // The VEE has been accepted to be deployed, but the response will
                // be asynchronous.
                // Wait for the response actively.
            case 201:
            case 200:
                // The VirtualMachine has been started without errors. Parse the
                // response and
                // get the task id.
                try {
                    Document responseXml = response.getEntityAsDom().getDocument();
                    return responseXml;

                } catch (IOException e) {
                    throw new ProductInstallatorException(msgerror);
                } catch (Exception e) {
                    msgerror = "RECManagerEception. Internal error while " + "decoding the answer";
                    log.info(msgerror);
                    throw new ProductInstallatorException(msgerror);
                }
        }

        return null;
    }
}
