/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import net.sf.json.JSONObject;

import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;

public class OpenStackQuotaDummyClientImpl implements QuotaClient {

    /**
     * Connect to openstack and get absolute limits.
     * 
     * @param claudiaData
     * @return
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException
     */
    public Limits getLimits(ClaudiaData claudiaData) throws InfrastructureException {

        String result = "{\"limits\": {\"rate\": [{\"regex\": \".*\", \"limit\": [{\"next-available\": \"2013-10-29T15:17:15Z\", \"unit\": \"MINUTE\", \"verb\": \"POST\", \"remaining\": 7, \"value\": 10}, {\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"MINUTE\", \"verb\": \"PUT\", \"remaining\": 10, \"value\": 10}, {\"next-available\": \"2013-10-29T15:15:55Z\", \"unit\": \"MINUTE\", \"verb\": \"DELETE\", \"remaining\": 98, \"value\": 100}], \"uri\": \"*\"}, {\"regex\": \"^/servers\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"DAY\", \"verb\": \"POST\", \"remaining\": 50, \"value\": 50}], \"uri\": \"*/servers\"}, {\"regex\": \".*changes-since.*\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"MINUTE\", \"verb\": \"GET\", \"remaining\": 3, \"value\": 3}], \"uri\": \"*changes-since*\"}, {\"regex\": \"^/os-fping\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"HOUR\", \"verb\": \"GET\", \"remaining\": 12, \"value\": 12}], \"uri\": \"*/os-fping\"}], \"absolute\": {\"maxServerMeta\": 128, \"maxPersonality\": 5, \"maxImageMeta\": 128, \"maxPersonalitySize\": 10240, \"maxSecurityGroupRules\": 20, \"maxTotalKeypairs\": 100, \"totalRAMUsed\": 4096, \"totalInstancesUsed\": 2, \"maxSecurityGroups\": 10, \"totalFloatingIpsUsed\": 0, \"maxTotalCores\": 20, \"totalSecurityGroupsUsed\": 0, \"maxTotalFloatingIps\": 10, \"maxTotalInstances\": 10, \"totalCoresUsed\": 2, \"maxTotalRAMSize\": 51200}}}";

        JSONObject jsonObject = JSONObject.fromObject(result);

        String limitsString = jsonObject.getString("limits");

        JSONObject limitsJson = JSONObject.fromObject(limitsString);

        String absoluteString = limitsJson.getString("absolute");
        JSONObject absoluteJson = JSONObject.fromObject(absoluteString);

        Limits limits = new Limits();
        limits.fromJson(absoluteJson);
        return limits;

    }
}
