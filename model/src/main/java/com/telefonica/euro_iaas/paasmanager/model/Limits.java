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

package com.telefonica.euro_iaas.paasmanager.model;

import net.sf.json.JSONObject;

public class Limits {
    private Integer maxTotalFloatingIps;
    private Integer totalFloatingIpsUsed;

    private Integer maxTotalInstances;
    private Integer totalInstancesUsed;

    private Integer maxSecurityGroups;
    private Integer totalSecurityGroups;

    public void setTotalFloatingIpsUsed(Integer totalFloatingIpsUsed) {
        this.totalFloatingIpsUsed = totalFloatingIpsUsed;
    }

    public void setMaxTotalInstances(Integer maxTotalInstances) {
        this.maxTotalInstances = maxTotalInstances;
    }

    public void setTotalInstancesUsed(Integer totalInstancesUsed) {
        this.totalInstancesUsed = totalInstancesUsed;
    }

    public Integer getMaxTotalFloatingIps() {
        return maxTotalFloatingIps;
    }

    public void setMaxTotalFloatingIps(Integer maxTotalFloatingIps) {
        this.maxTotalFloatingIps = maxTotalFloatingIps;
    }

    public Integer getTotalFloatingIpsUsed() {
        return totalFloatingIpsUsed;
    }

    public Integer getMaxTotalInstances() {
        return maxTotalInstances;
    }

    public Integer getTotalInstancesUsed() {
        return totalInstancesUsed;
    }

    public void fromJson(JSONObject jsonAbsolute) {
        if (jsonAbsolute.containsKey("maxTotalFloatingIps")) {
            maxTotalFloatingIps = jsonAbsolute.getInt("maxTotalFloatingIps");
        }
        if (jsonAbsolute.containsKey("totalFloatingIpsUsed")) {
            totalFloatingIpsUsed = jsonAbsolute.getInt("totalFloatingIpsUsed");
        }
        if (jsonAbsolute.containsKey("maxTotalInstances")) {
            maxTotalInstances = jsonAbsolute.getInt("maxTotalInstances");
        }
        if (jsonAbsolute.containsKey("totalInstancesUsed")) {
            totalInstancesUsed = jsonAbsolute.getInt("totalInstancesUsed");
        }
        if (jsonAbsolute.containsKey("maxSecurityGroups")) {
            maxSecurityGroups = jsonAbsolute.getInt("maxSecurityGroups");
        }
        if (jsonAbsolute.containsKey("totalSecurityGroupsUsed")) {
            totalSecurityGroups = jsonAbsolute.getInt("totalSecurityGroupsUsed");
        }

    }

    public boolean checkTotalInstancesUsed() {
        return (maxTotalInstances != null) && (totalInstancesUsed != null);
    }

    public boolean checkTotalFloatingsIpsUsed() {

        return (maxTotalFloatingIps != null) && (totalFloatingIpsUsed != null);
    }

    public boolean checkTotalSecurityGroupsUsed() {

        return (maxSecurityGroups != null) && (totalSecurityGroups != null);
    }

    public Integer getMaxSecurityGroups() {
        return maxSecurityGroups;
    }

    public void setMaxSecurityGroups(Integer maxSecurityGroups) {
        this.maxSecurityGroups = maxSecurityGroups;
    }

    public Integer getTotalSecurityGroups() {
        return totalSecurityGroups;
    }

    public void setTotalSecurityGroups(Integer totalSecurityGroups) {
        this.totalSecurityGroups = totalSecurityGroups;
    }
}
