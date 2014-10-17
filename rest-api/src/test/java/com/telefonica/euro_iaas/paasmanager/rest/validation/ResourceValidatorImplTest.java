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

package com.telefonica.euro_iaas.paasmanager.rest.validation;


import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;

/**
 * Test the ResourceValidatorImpl class.
 */
public class ResourceValidatorImplTest {

    private ResourceValidatorImpl resourceValidator;

    /**
     * Initialize the Unit Test.
     */
    @Before
    public void setUp() {
        resourceValidator = new ResourceValidatorImpl();
    }

    /**
     * Validate the null name resource.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    public void shouldValidateNullName() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName(null);
    }

    /**
     * Test the validation of a null description.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    public void shouldValidateNullDescription() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateDescription(null);
    }

    /**
     * Test the launched exception  when the name is empty.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    @Test(expected = InvalidEntityException.class)
    public void shouldValidateEmptyName() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName("");

    }

    /**
     * Test the launched exception when the description is empty.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    @Test(expected = InvalidEntityException.class)
    public void shouldValidateEmptyDescription() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateDescription("");

    }

    /**
     * Test the launched exception when some strange character appear in the name.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    @Test(expected = InvalidEntityException.class)
    public void shouldValidateStrangeCharacterinName() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName("name.name");

    }

    /**
     * Test the launched exception when the name is too long.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    @Test(expected = InvalidEntityException.class)
    public void shouldValidateNameTooLong() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    }

    /**
     * Test the launched exception when the description is too long.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     */
    @Test(expected = InvalidEntityException.class)
    public void shouldValidateDescriptionTooLong() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaajjjjjjjjjjjjjjjjjjjjjj"
                + "jjjjjjjjjjjjjjjjjjjjjjjdddddddddddddddddddddddddddddddddddddddjjjjjjjjaaaaaaaaaaaaaaaaaaaddddddddd"
                + "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
                + "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");

    }

}

