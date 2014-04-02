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
    public Limits getLimits(ClaudiaData claudiaData, String region) throws InfrastructureException {

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
