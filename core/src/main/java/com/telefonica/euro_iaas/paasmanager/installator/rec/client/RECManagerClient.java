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

package com.telefonica.euro_iaas.paasmanager.installator.rec.client;

// import com.sun.jersey.api.client.Client;

import org.restlet.Client;
import org.restlet.data.Protocol;

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
    public RECServiceService getRECServiceService(String baseUrl, String mediaType) {
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
    public RECVirtualServiceService getRECVirtualServiceService(String baseUrl, String mediaType) {
        return new RECVirtualServiceServiceImpl(client, baseUrl, mediaType);
    }
}
