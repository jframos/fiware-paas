/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.installator.rec.client;

//import com.sun.jersey.api.client.Client;
import org.restlet.Client;
import org.restlet.data.Protocol; /*import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;*/
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECACService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECPICService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECServiceService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECVMService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECVirtualServiceService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl.RECACServiceImpl;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl.RECPICServiceImpl;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl.RECServiceServiceImpl;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl.RECVMServiceImpl;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.impl.RECVirtualServiceServiceImpl;

/**
 * @author jesus.movilla
 * 
 */
public class RECManagerClient {

	private static Client client = new Client(Protocol.HTTP);

	/**
	 * Get the service to work with RECService
	 * 
	 * @param baseUrl
	 *            the base url where the RECManager is
	 * @param mediaType
	 *            the media type (application/xml or application/json)
	 * @return the RECService service.
	 */
	public RECServiceService getRECServiceService(String baseUrl,
			String mediaType) {
		return new RECServiceServiceImpl(client, baseUrl, mediaType);
	}

	/**
	 * Ge tthe service to work with RECPIC
	 * 
	 * @param baseUrl
	 * @param mediaType
	 * @return
	 */
	public RECPICService getRECPICService(String baseUrl, String mediaType) {
		return new RECPICServiceImpl(client, baseUrl, mediaType);
	}

	/**
	 * Ge tthe service to work withA RECVM
	 * 
	 * @param baseUrl
	 * @param mediaType
	 * @return
	 */
	public RECVMService getRECVMService(String baseUrl, String mediaType) {
		return new RECVMServiceImpl(client, baseUrl, mediaType);
	}

	/**
	 * Get the service to work with REC ACs
	 * 
	 * @param baseUrl
	 * @param mediaType
	 * @return
	 */
	public RECACService getRECACService(String baseUrl, String mediaType) {
		return new RECACServiceImpl(client, baseUrl, mediaType);
	}

	/**
	 * Get the service to work with Virtual Services
	 * 
	 * @param baseUrl
	 * @param mediaType
	 * @return
	 */
	public RECVirtualServiceService getRECVirtualServiceService(String baseUrl,
			String mediaType) {
		return new RECVirtualServiceServiceImpl(client, baseUrl, mediaType);
	}
}
