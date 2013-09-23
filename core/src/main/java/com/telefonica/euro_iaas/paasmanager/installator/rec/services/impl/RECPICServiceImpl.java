/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl;

/*import com.sun.jersey.api.client.Client;
 import com.sun.jersey.api.client.WebResource;
 import com.sun.jersey.api.client.WebResource.Builder;
 */
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.data.Status;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECPICService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;

/**
 * @author jesus.movilla
 * 
 */
public class RECPICServiceImpl extends AbstractBaseService implements
		RECPICService {

	private MediaType APPLICATION_OVF_XML;
	private static Logger log = Logger.getLogger(RECPICServiceImpl.class);

	public RECPICServiceImpl(Client client, String baseUrl, String mediaType) {
		APPLICATION_OVF_XML = MediaType.register(mediaType, "XML OVF document");
		setBaseHost(baseUrl);
		setType(APPLICATION_OVF_XML);
		setClient(client);
	}

	public void configurePIC(String vapp, String appId, String picId,
			String vmName) throws ProductReconfigurationException,
			ProductInstallatorException {

		log.info("reconfigurePIC.START");

		vapp = vapp.replace("<ovfenvelope:ProductSection>",
				"<ovfenvelope:ProductSection xmlns:ovfenvelope=\"http://schemas.dmtf"
						+ ".org/ovf/envelope/1\" ovfenvelope:class=\"4caast.vm"
						+ ".application\">");

		DomRepresentation data = null;
		Response putResponse = null;

		Reference urlPut = new Reference(getBaseHost()
				+ MessageFormat.format(ClientConstants.BASE_PIC_PATH, appId,
						vmName) + "/" + picId);

		Reference urlGet = new Reference(getBaseHost()
				+ MessageFormat.format(ClientConstants.BASE_PIC_PATH, appId,
						vmName) + "/" + picId);

		Response getResponse = client.get(urlGet);

		if (getResponse.getStatus().equals(Status.SUCCESS_OK)) {
			Document doc = getDocument(vapp);
			data = new DomRepresentation(APPLICATION_OVF_XML, doc);
			log.info("createPIC.url():" + urlPut);
			putResponse = client.put(urlPut, data);

			checkRECTaskStatus(putResponse);
		} else {
			throw new ProductReconfigurationException("The PIC is not"
					+ "created before");
		}

	}

	public void createPIC(String vapp, String appId, String picId, String vmName)
			throws ProductInstallatorException {

		log.info("createPIC.START");

		vapp = vapp.replace("<ovfenvelope:ProductSection>",
				"<ovfenvelope:ProductSection xmlns:ovfenvelope=\"http://schemas.dmtf"
						+ ".org/ovf/envelope/1\" ovfenvelope:class=\"4caast.vm"
						+ ".application\">");
		log.debug(vapp);

		DomRepresentation data = null;
		Response postResponse = null;

		Reference urlPost = new Reference(getBaseHost()
				+ MessageFormat.format(ClientConstants.BASE_PIC_PATH, appId,
						vmName));

		Reference urlGet = new Reference(getBaseHost()
				+ MessageFormat.format(ClientConstants.BASE_PIC_PATH, appId,
						vmName) + "/" + picId);

		Response getResponse = client.get(urlGet);
		log.debug ("Result " + getResponse.getStatus());

		if (!getResponse.getStatus().equals(Status.SUCCESS_OK)) {
			Document doc = getDocument(vapp);
			data = new DomRepresentation(APPLICATION_OVF_XML, doc);
			log.info("createPIC.url():" + urlPost);
			postResponse = client.post(urlPost, data);

			checkRECTaskStatus(postResponse);
		}
	}
}
