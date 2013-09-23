/**
 * 
 */
package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * @author jesus.movilla
 *
 */
public class OpenStackSynchronizationException extends Exception {

	public OpenStackSynchronizationException(String msg) {
		super(msg);
	}

	public OpenStackSynchronizationException(Throwable e) {
		super(e);
	}
	
	public OpenStackSynchronizationException(String msg, Throwable e) {
		super(msg, e);
	}
}
