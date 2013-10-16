package com.telefonica.euro_iaas.paasmanager.claudia;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;

public interface KeystoneClient {

    String obtainToken(String tenantId) throws OpenStackException;

}
