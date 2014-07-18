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

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;


/**
 * * @author Henar Munoz
 */
public class ResourceValidatorImpl implements ResourceValidator {
    
    private static final int DESCRIPTION_LENGHT = 256;
    private static final int NAME_LENGHT = 30;


    /**
     * It validates a name.
     *
     * @param name  The name to validate.
     * @throws InvalidEntityException
     */
    public void validateName(String name)  throws InvalidEntityException {
        if (name == null) {
            throw new InvalidEntityException("The environment name is not valid. It is null");
        }

        /*Names with characters other than [a-z], [0-9] or "-" (hyphen)*/
        if (name.indexOf('.') != -1 || name.indexOf('_') != -1 || name.indexOf(' ') != -1) {
            throw new InvalidEntityException("The name is not valid. There is a strange name");
        }
        /* Empty names ("")*/
        if (name.length() == 0) {
            throw new InvalidEntityException("The name is not valid. It is empty");
        }

		/*Missing names (the name is not even present in the XML/JSON)*/
		  
		/*Names with more than 30 characters (i.e. 31 or more)*/
        if (name.length() > NAME_LENGHT) {
            throw new InvalidEntityException("The name is not valid. The name has mor than 30 characteres");
        }
    }

    /**
     * It validates a description.
     *
     * @param name  The name to validate.
     * @throws InvalidEntityException
     */
    public void validateDescription(String name)  throws InvalidEntityException {
        if (name == null) {
            throw new InvalidEntityException("The description is not valid. It is null");
        }
		
        /* Empty descriptions ("")*/
        if (name.length() == 0) {
            throw new InvalidEntityException("The description is not valid. It is empty");
        }
		
		/*Missing descriptions (the name is not even present in the XML/JSON)*/
   
		/*Descriptions with more than 256 characters (i.e. 257 or more))*/
        if (name.length() > DESCRIPTION_LENGHT) {
            throw new InvalidEntityException("The description  is not valid. The name has mor than 256 characteres");
        }

    }

}
