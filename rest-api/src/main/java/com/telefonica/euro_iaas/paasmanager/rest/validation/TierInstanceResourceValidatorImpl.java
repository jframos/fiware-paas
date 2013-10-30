/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidTierInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * * @author bmmanso
 */
public class TierInstanceResourceValidatorImpl implements TierInstanceResourceValidator {

    public void validateScaleUpTierInstance(String org, String vdc, EnvironmentInstance environmentInstance,
            String tierInst) throws InvalidTierInstanceRequestException, InvalidEnvironmentRequestException {
        // We check if the tierInstance is in the BD; if not, we
        // cannot scale it

        if (!environmentInstance.getVdc().equals(vdc)) {
            throw new InvalidEnvironmentRequestException("EnvironmentInstance does" + "not exists in that vdc");
        }
        boolean existTier = false;
        for (TierInstance tier : environmentInstance.getTierInstances()) {
            existTier = tier.getName().equals(tierInst);
            if (existTier) {
                break;
            }
        }
        if (existTier) {
            throw new InvalidTierInstanceRequestException("TierInstance does" + " exists in that Enviroment");
        }

    }

    public void validateScaleDownTierInstance(String org, String vdc, EnvironmentInstance environmentInstance,
            String tierInstance) throws InvalidTierInstanceRequestException, InvalidEnvironmentRequestException {
        // TODO Auto-generated method stub
        if (!environmentInstance.getVdc().equals(vdc)) {
            throw new InvalidEnvironmentRequestException("EnvironmentInstance does" + "not exists in that vdc");
        }
        boolean existTier = false;
        for (TierInstance tier : environmentInstance.getTierInstances()) {
            existTier = tier.getName().equals(tierInstance);
            if (existTier) {
                break;
            }
        }
        if (!existTier) {
            throw new InvalidTierInstanceRequestException("TierInstance does" + " exists in that Enviroment");
        }
    }

}
