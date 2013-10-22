/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Represents a host where a OS is running. It is formed by
 * 
 * @author jesus M. Movilla
 */

@Embeddable
public class VM {

    /** The ip where the host is located. */
    @Column(length = 128)
    private String ip = "";

    /** The computer's hostname. */
    @Column(length = 128)
    private String hostname = "";

    /** The domain. */
    @Column(length = 128)
    private String domain = "";

    /** the fqn. ***/
    @Column(length = 512)
    private String fqn = "";

    private HashMap<String, String> networks = null;

    /** the OSType. ***/
    @Column(length = 8)
    private String osType = "";

    /** the VAPP. ***/
    @Column(length = 512)
    private String vmid = "";

    /**
     * Constructor.
     */
    public VM() {

    }

    /**
     * @param fqn
     */
    public VM(String fqn) {
        this.fqn = fqn;
    }

    /**
     * @param fqn
     * @param ip
     */
    public VM(String fqn, String ip) {
        this.fqn = fqn;
        this.ip = ip;
    }

    /**
     * @param fqn
     * @param hostname
     * @param domain
     */
    public VM(String fqn, String hostname, String domain) {

        this.fqn = fqn;
        this.hostname = hostname;
        this.domain = domain;
    }

    /**
     * @param ip
     * @param fqn
     * @param hostname
     * @param domain
     */
    public VM(String fqn, String ip, String hostname, String domain) {
        this.fqn = fqn;
        this.hostname = hostname;
        this.domain = domain;
        this.ip = ip;
    }

    /**
     * @param ip
     * @param hostname
     * @param domain
     * @param fqn
     * @param osType
     */
    public VM(String ip, String hostname, String domain, String fqn, String osType) {
        this.fqn = fqn;
        this.hostname = hostname;
        this.domain = domain;
        this.ip = ip;
        this.osType = osType;
    }

    // // ACCESSORS ////

    /**
     * Add a network to the VM.
     * 
     * @param network
     * @param ip
     */
    public void addNetwork(String network, String ip) {
        if (networks == null) {
            networks = new HashMap<String, String>();
        }

        this.networks.put(network, ip);
    }

    /**
     * The network to be deleted.
     * 
     * @param network
     */
    public void deleteNetwork(String network) {
        if (this.networks.get(network) != null) {
            this.networks.remove(network);
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VM other = (VM) obj;
        if (domain == null) {
            if (other.domain != null) {
                return false;
            }
        } else if (!domain.equals(other.domain)) {
            return false;
        }
        if (fqn == null) {
            if (other.fqn != null) {
                return false;
            }
        } else if (!fqn.equals(other.fqn)) {

            return false;
        }
        if (hostname == null) {
            if (other.hostname != null) {
                return false;
            }
        } else if (!hostname.equals(other.hostname)) {
            return false;
        }
        if (ip == null) {
            if (other.ip != null) {
                return false;
            }

        } else if (!ip.equals(other.ip)) {
            return false;
        }
        return true;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return the fqn
     */
    public String getFqn() {
        return fqn;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the network
     */
    public HashMap<String, String> getNetworks() {

        return networks;
    }

    /**
     * @return the osType
     */
    public String getOsType() {
        return osType;
    }

    /**
     * @return the vmid
     */
    public String getVmid() {
        return vmid;
    }

    /**
     * @return the vmOVF
     */
    /*
     * public String getVmOVF() { return vmOVF; }
     */

    /**
     * @param vmOVF
     *            the vmOVF to set
     */
    /*
     * public void setVmOVF(String vmOVF) { this.vmOVF = vmOVF; }
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + ((fqn == null) ? 0 : fqn.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @param fqn
     *            the fqn to set
     */
    public void setFqn(String fqn) {
        this.fqn = fqn;
    }

    /**
     * @param hostname
     *            the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param networks
     *            the networks to set
     */
    public void setNetworks(HashMap<String, String> networks) {
        this.networks = networks;
    }

    /**
     * @param osType
     *            the osType to set
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /**
     * @param vmid
     *            the vmid to set
     */
    public void setVmid(String vmid) {
        this.vmid = vmid;
    }

    /**
     * a Dto entity.
     * 
     * @return
     */
    public VMDto toDto() {

        return new VMDto(domain, fqn, hostname, ip, this.vmid);

    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VM [domain=" + domain + ", hostname=" + hostname + ", ip=" + ip + ", fqn=" + fqn + ", osType=" + osType
                + "]";
    }

}
