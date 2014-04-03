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

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECServiceService;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;

/*
 * import com.sun.jersey.api.client.Client; import com.sun.jersey.api.client.WebResource; import
 * com.sun.jersey.api.client.WebResource.Builder;
 */

/*
 * import com.sun.jersey.api.client.Client; import com.sun.jersey.api.client.WebResource; import
 * com.sun.jersey.api.client.WebResource.Builder;
 */

/*
 * import com.sun.jersey.api.client.Client; import com.sun.jersey.api.client.WebResource; import
 * com.sun.jersey.api.client.WebResource.Builder;
 */

/*
 * import com.sun.jersey.api.client.Client; import com.sun.jersey.api.client.WebResource; import
 * com.sun.jersey.api.client.WebResource.Builder;
 */

/**
 * @author jesus.movilla
 */
public class RECServiceServiceImpl extends AbstractBaseService implements RECServiceService {

    private MediaType APPLICATION_OVF_XML;
    private static Logger log = Logger.getLogger(RECServiceServiceImpl.class);

    public RECServiceServiceImpl(Client client, String baseUrl, String mediaType) {
        APPLICATION_OVF_XML = MediaType.register(mediaType, "XML OVF document");
        setBaseHost(baseUrl);
        setType(APPLICATION_OVF_XML);
        setClient(client);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.rec.services.RECServiceService
     * #createService(org.dmtf.schemas.ovf.envelope._1.Envelope)
     */
    public void createService(String envelopeVapp, String appId, String callback) throws ProductInstallatorException {

        log.info("createService.STAR ");

        /*
         * String url = getBaseHost() + ClientConstants.BASE_SERVICE_PATH; WebResource wr = getClient().resource(url);
         * Builder builder = wr.accept(getType()).type(getType()).entity(env); builder = addCallback(builder, callback);
         * builder.post();
         */
        DomRepresentation data = null;
        Response postResponse = null;

        Reference urlGet = new Reference(getBaseHost() + ClientConstants.BASE_SERVICE_PATH + "/" + appId);

        Reference urlPost = new Reference(getBaseHost() + ClientConstants.BASE_SERVICE_PATH);

        Response getResponse = client.get(urlGet);

        if (!getResponse.getStatus().equals(Status.SUCCESS_OK)) {
            Document doc = getDocument(envelopeVapp);
            data = new DomRepresentation(APPLICATION_OVF_XML, doc);
            log.info("createService.url():" + urlPost);
            postResponse = client.post(urlPost, data);
            checkRECTaskStatus(postResponse);
        }
    }

    public void configureService(String envelopeVapp, String appId, Object object)
            throws ProductReconfigurationException {
        log.info("reconfigureService.STAR ");

        Reference urlGet = new Reference(getBaseHost() + ClientConstants.BASE_SERVICE_PATH + "/" + appId);

        Response getResponse = client.get(urlGet);

        if (!getResponse.getStatus().equals(Status.SUCCESS_OK)) {

            throw new ProductReconfigurationException("The service is not" + "register before the reconfiguration");
        }

    }

    // private Document getEnvelope(String serviceFile) throws
    // ProductInstallatorException {

    /*
     * EnvelopeType env = new EnvelopeType(); AnnotationSectionType annot = new AnnotationSectionType(); MsgType info =
     * new MsgType (); info.setMsgid("org.fourcaast.application.id"); info.setValue(servicename); annot.setInfo(info);
     * annot.setAnnotation(null); ReferencesType ref = new ReferencesType();
     */

    /*
     * <ovfenvelope:Envelopexmlns:cimresallocdata=
     * "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData"
     * xmlns:ovfenvelope="http://schemas.dmtf.org/ovf/envelope/1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
     * <ovfenvelope:References/> <ovfenvelope:AnnotationSection ovfenvelope:required="true"> <ovfenvelope:Info
     * ovfenvelope:msgid="org.fourcaast.application.id" >app-00001</ovfenvelope:Info>
     * <ovfenvelope:Annotation></ovfenvelope:Annotation> </ovfenvelope:AnnotationSection> </ovfenvelope:Envelope>
     */

    /*
     * OVFSerializer ovfSerializer = OVFSerializer.getInstance(); OVFEnvelopeUtils.addSection(env,annot);
     */

    /*
     * try { InputStream is = new FileInputStream(serviceFile); BufferedReader reader = new BufferedReader(new
     * InputStreamReader(is)); StringBuffer ruleFile = new StringBuffer(); String actualString; while ((actualString =
     * reader.readLine()) != null) { ruleFile.append(actualString).append("\n"); } return
     * getDocument(ruleFile.toString()); } catch (IOException e) { String msg =
     * " The file EnvelopeTemplate.xml could not be found in" + "the resource directory"; throw new
     * ProductInstallatorException(msg); } }
     */

}
