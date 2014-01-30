/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.EntityNotFoundException;

public class APIExceptionTest {

    @Test
    public void shouldReturnErrorCode10AfterHibernateException() {
        // given

        // when
        APIException apiException = new APIException(
                new Exception(
                        "org.hibernate.PropertyAccessException: Null value was assigned to a property of primitive type setter of com.telefonica.euro_iaas.paasmanager.model.NetworkInstance.adminStateUp"));

        // then
        assertEquals(ErrorCode.HIBERNATE.getCode(), apiException.getCode());
    }

    @Test
    public void shouldReturnError20AfterConnectionException() {
        // given

        // when
        APIException apiException = new APIException(new Exception(
                "org.hibernate.exception.JDBCConnectionException: An I/O error occured while sending to the backend."));

        // then
        assertEquals(ErrorCode.DB_CONNECTION.getCode(), apiException.getCode());
    }

    @Test
    public void shouldReturnDefaultErrorAfterGenericException() {
        // given

        // when
        APIException apiException = new APIException(new Exception("generic message"));

        // then
        assertEquals(ErrorCode.DEFAULT.getCode(), apiException.getCode());

        // then
    }

    @Test
    public void should() {
        // given

        // when

        EntityNotFoundException entityNotFoundException = new EntityNotFoundException("error");
        APIException apiException = new APIException(entityNotFoundException);

        // then
        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), apiException.getCode());
    }

}
