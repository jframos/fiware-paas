/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl;

import java.text.MessageFormat;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECACService;
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
public class RECACServiceImpl extends AbstractBaseService implements RECACService {

    private MediaType APPLICATION_OVF_XML;
    private static Logger log = Logger.getLogger(RECACServiceImpl.class);

    public RECACServiceImpl(Client client, String baseUrl, String mediaType) {
        APPLICATION_OVF_XML = MediaType.register(mediaType, "XML OVF document");
        setBaseHost(baseUrl);
        setType(APPLICATION_OVF_XML);
        setClient(client);
    }

    public void createAC(String vapp, String appId, String picId, String vmName, String acId)
            throws ProductInstallatorException {

        log.info("createAC.STAR ");

        DomRepresentation data = null;
        Response postResponse = null;

        Reference urlPost = new Reference(getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_AC_PATH, appId, vmName, picId));

        Reference urlGet = new Reference(getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_PIC_PATH, appId, vmName, picId) + "/" + acId);

        Response getResponse = client.get(urlGet);

        if (!getResponse.getStatus().equals(Status.SUCCESS_OK)) {
            Document doc = getDocument(vapp);
            data = new DomRepresentation(APPLICATION_OVF_XML, doc);
            log.info("createAC().url: " + urlPost);
            postResponse = client.post(urlPost, data);

            checkRECTaskStatus(postResponse);
        }
    }
}
