/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jesus.movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstanceOvfDto {

    private String ovf;

    public EnvironmentInstanceOvfDto() {
    }

    /**
     * @param ovf
     */
    public EnvironmentInstanceOvfDto(String ovf) {
        this.ovf = ovf;
    }

    /**
     * @return the ovf
     */
    public String getOvf() {
        return ovf;
    }

    /**
     * @param ovf
     *            the ovf to set
     */
    public void setOvf(String ovf) {
        this.ovf = ovf;
    }

}
