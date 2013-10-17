/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * Defines the data to be used with Claudia.
 * 
 * @author jesus.movilla
 */
public class ClaudiaData {

    /** The org. */
    private String org;
    /** The vdc. */
    private String vdc;
    /** The service */
    private String service;
    /** The vm */
    // private String vm;
    /** The vapp. */
    // private String vapp;

    /** The fqn */
    // private String fqn;

    /** The vmId **/
    // private String vmid;

    /** The user **/
    private PaasManagerUser user;

    /** The replica **/
    private String replica;

    /**
     * @param fqn
     */
    /*
     * public ClaudiaData(String fqn) { this.fqn = fqn; String[] tokens = fqn.split("\\."); if (tokens.length > 0) org =
     * tokens[0]; if (tokens.length > 2) vdc = tokens[2]; if (tokens.length > 4) service = tokens[4]; if (tokens.length
     * > 6) { vm = tokens[6]; vapp = this.vm + "Vapp"; } }
     */

    /**
     * @param org
     * @param vdc
     */
    /*
     * public ClaudiaData(String org, String vdc) { this.org = org; this.vdc = vdc; }
     */

    /**
     * @param org
     * @param vdc
     * @param service
     */
    public ClaudiaData(String org, String vdc, String service) {
        this.org = org;
        this.vdc = vdc;
        this.service = service;
    }

    /**
     * @param org
     * @param vdc
     * @param service
     * @param vm
     */
    /*
     * public ClaudiaData(String org, String vdc, String service, String vm) { this.org = org; this.vdc = vdc;
     * this.service = service; this.vm = vm; }
     */

    /**
     * @param org
     * @param vdc
     * @param service
     * @param vm
     * @param vapp
     */
    /*
     * public ClaudiaData(String org, String vdc, String service, String vm, String vapp) { this.org = org; this.vdc =
     * vdc; this.service = service; this.vm = vm; this.vapp = vapp; }
     */

    /**
     * @param org
     * @param vdc
     * @param service
     * @param vm
     * @param vapp
     * @param user
     */
    /*
     * public ClaudiaData(String org, String vdc, String service, String vm, String vapp, PaasManagerUser user) {
     * this.org = org; this.vdc = vdc; this.service = service; this.vm = vm; this.vapp = vapp; this.user = user; }
     */

    /**
     * @return the org
     */
    public String getOrg() {
        return org;
    }

    /**
     * @param org
     *            the org to set
     */
    /*
     * public void setOrg(String org) { this.org = org; }
     */

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    /*
     * public void setVdc(String vdc) { this.vdc = vdc; }
     */

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     *            the service to set
     */
    /*
     * public void setService(String service) { this.service = service; }
     */

    /**
     * @return the vm
     */
    /*
     * public String getVm() { return vm; }
     */

    /**
     * @param vm
     *            the vm to set
     */
    /*
     * public void setVm(String vm) { this.vm = vm; }
     */

    /**
     * @return the fqn
     */
    /*
     * public String getFqn() { return fqn; }
     */

    /**
     * @param fqn
     *            the fqn to set
     */
    /*
     * public void setFqn(String fqn) { this.fqn = fqn; }
     */

    /**
     * @return the vapp
     */
    /*
     * public String getVapp() { return vapp; }
     */

    /**
     * @param vapp
     *            the vapp to set
     */
    /*
     * public void setVapp(String vapp) { this.vapp = vapp; }
     */

    /**
     * @return the user
     */
    public PaasManagerUser getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(PaasManagerUser user) {
        this.user = user;
    }

    /**
     * @return the vmid
     */
    /*
     * public String getVmid() { return vmid; }
     */

    /**
     * @param vmid
     *            the vmid to set
     */
    /*
     * public void setVmId(String vmid) { this.vmid = vmid; }
     */

    /**
     * @return the replica
     */
    /*
     * public String getReplica() { return replica; } /**
     * @param replica the replica to set
     */
    /*
     * public void setReplica(String replica) { this.replica = replica; }
     */
}
