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

package com.telefonica.euro_iaas.paasmanager.exception;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when trying to Insert a ProductRelease that already exists.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class AlreadyExistsEntityException extends Exception {

    private ProductRelease productRelease;

    /**
     * Default constructor.
     */
    public AlreadyExistsEntityException() {
        super();
    }

    /**
     * Constructor.
     * @param productRelease    The product release of the exception.
     */
    public AlreadyExistsEntityException(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * Constructor.
     * @param msg   Message to be shown.
     */
    public AlreadyExistsEntityException(String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * @param e     Exception to be launched.
     */
    public AlreadyExistsEntityException(Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     * @param msg   Message to be shown.
     * @param e     Exception to be launched.
     */
    public AlreadyExistsEntityException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * Get the product release of the exception.
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * The productRelease to set.
     * @param productRelease
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }
}
