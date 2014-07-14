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

package com.telefonica.euro_iaas.paasmanager.rest.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.EntityNotFoundException;

public class APIExceptionTest {

    @Test
    public void shouldReturnErrorCode10AfterHibernateException() {
        // given

        APIException apiException = new APIException(
                new Exception(
                        "org.hibernate.PropertyAccessException: Null value was assigned to a property of "
                      + "primitive type setter of com.telefonica.euro_iaas.paasmanager.model.NetworkInstance"
                      + ".adminStateUp"));

        // when
        apiException.parseCause();

        // then
        assertEquals(ErrorCode.HIBERNATE.getCode(), apiException.getCode());
    }

    @Test
    public void shouldReturnError20AfterConnectionException() {
        // given

        APIException apiException = new APIException(new Exception(
                "org.hibernate.exception.JDBCConnectionException: An I/O error occured while sending to the backend."));
        // when
        apiException.parseCause();

        // then
        assertEquals(ErrorCode.DB_CONNECTION.getCode(), apiException.getCode());
    }

    @Test
    public void shouldReturnDefaultErrorAfterGenericException() {
        // given

        APIException apiException = new APIException(new Exception("generic message"));
        // when
        apiException.parseCause();

        // then
        assertEquals(ErrorCode.DEFAULT.getCode(), apiException.getCode());

    }

    @Test
    public void shouldParseEntityNotFound() {
        // given

        EntityNotFoundException entityNotFoundException = new EntityNotFoundException("error");
        APIException apiException = new APIException(entityNotFoundException);
        // when
        apiException.parseCause();

        // then
        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), apiException.getCode());
    }

}
