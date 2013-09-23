package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * Generic runtime exception for the application.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class PaasManagerServerRuntimeException extends RuntimeException {

	public PaasManagerServerRuntimeException() {
		super();
	}

	public PaasManagerServerRuntimeException(String msg) {
		super(msg);
	}

	public PaasManagerServerRuntimeException(Throwable e) {
		super(e);
	}

	public PaasManagerServerRuntimeException(String msg, Throwable e) {
		super(msg, e);
	}
}
