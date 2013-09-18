package com.telefonica.euro_iaas.paasmanager.client;

import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.client.services.impl.EnvironmentInstanceServiceImpl;

/**
 * Contains the uris, and other constants related to paas-manager-client
 *
 * @author Jesus M. Movilla
 *
 */
public class PaasManagerClient {

	private static Client client = new Client();

	/**
	 * Get the service to work with product instances.
	 * @param baseUrl the base url where the SDC is
	 * @param mediaType the media type (application/xml or application/json)
	 * @return the environment instance service.
	*/
	public EnvironmentInstanceService getEnvironmentInstanceService(
			String baseUrl, String mediaType) {
	        return new EnvironmentInstanceServiceImpl(client, baseUrl, mediaType);
	}

}
