/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.rest.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * The Class RestAuthenticationEntryPoint.
 * 
 * @author dbermejo
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */
	public final void commence(final HttpServletRequest request,
			final HttpServletResponse response,
			final AuthenticationException authException) throws IOException,
			ServletException {

		// if (authException instanceof AuthenticationServiceException) {
		// LOG.error(authException);
		// response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
		// authException.getMessage());
		// }

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
