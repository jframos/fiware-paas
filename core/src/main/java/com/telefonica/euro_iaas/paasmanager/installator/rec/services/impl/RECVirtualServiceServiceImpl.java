/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl;

import java.text.MessageFormat;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.VirtualServiceInstallationException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECVirtualServiceService;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;

/**
 * @author jesus.movilla
 */
public class RECVirtualServiceServiceImpl extends AbstractBaseService implements RECVirtualServiceService {

    private MediaType APPLICATION_OVF_XML;
    private static Logger log = Logger.getLogger(RECVirtualServiceServiceImpl.class);

    public RECVirtualServiceServiceImpl(Client client, String baseUrl, String mediaType) {
        APPLICATION_OVF_XML = MediaType.register(mediaType, "XML OVF document");
        setBaseHost(baseUrl);
        setType(APPLICATION_OVF_XML);
        setClient(client);
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.services.
     * RECVirtualServiceService#createVirtualService(java.lang.String)
     */
    public void createVirtualService(String appId, String vmId, String picId, String acId, String payload)
            throws VirtualServiceInstallationException {

        log.debug("Virtual service payload " + payload);

        DomRepresentation data = null;
        Response postResponse = null;

        Reference urlGet = new Reference(getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_VIRTUALSERVICE_PATH, appId, vmId, picId) + "/" + acId);

        Reference urlPost = new Reference(getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_VIRTUALSERVICE_PATH, appId, vmId, picId));

        Response getResponse = client.get(urlGet);

        if (!getResponse.getStatus().equals(Status.SUCCESS_OK)) {
            Document doc;
            try {
                doc = getDocument(payload);

                data = new DomRepresentation(APPLICATION_OVF_XML, doc);
                log.info("createVirtualService.url():" + urlPost);
                postResponse = client.post(urlPost, data);

                checkRECTaskStatus(postResponse);
            } catch (ProductInstallatorException e) {
                String msg = " Error agregating a Virtual Service. Desc: " + e.getMessage();
                throw new VirtualServiceInstallationException(msg);
            }
        }
    }
}
