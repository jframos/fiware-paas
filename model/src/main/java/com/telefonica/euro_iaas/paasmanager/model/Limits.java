/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
