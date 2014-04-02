/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

public class OpenStackQuotaClientImpl implements QuotaClient {

    private static Logger log = Logger.getLogger(OpenStackQuotaClientImpl.class);
    private OpenStackUtil openStackUtil = null;

    public OpenStackUtil getOpenStackUtil() {
        return openStackUtil;
    }

    public void setOpenStackUtil(OpenStackUtil openStackUtil) {
        this.openStackUtil = openStackUtil;
    }

    /**
     * Connect to openstack and get absolute limits.
     * 
     * @param claudiaData
     * @return
     * @throws InfrastructureException
     */
    public Limits getLimits(ClaudiaData claudiaData, String region) throws InfrastructureException {

        try {
            String result = openStackUtil.getAbsoluteLimits(claudiaData.getUser(), region);

            JSONObject jsonObject = JSONObject.fromObject(result);

            String limitsString = jsonObject.getString("limits");

            JSONObject limitsJson = JSONObject.fromObject(limitsString);

            String absoluteString = limitsJson.getString("absolute");
            JSONObject absoluteJson = JSONObject.fromObject(absoluteString);

            Limits limits = new Limits();
            limits.fromJson(absoluteJson);
            return limits;

        } catch (OpenStackException e) {
            String message = "Error to obtain the absolute limits of the environment " + e.getMessage();
            log.error(message);
            throw new InfrastructureException(message, e);
        }

    }
}
