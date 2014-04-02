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

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECACService;

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
