package org.htomar.openakamai.edge.auth.exception;

/**
 * Exception representing errors during request signing.
 *
 */
public class RequestSigningException extends Exception {

	/**
	 */
	private static final long serialVersionUID = -4716437270940718895L;

	public RequestSigningException() {
		super();
	}

	public RequestSigningException(String message) {
		super(message);
	}

	public RequestSigningException(Throwable t) {
		super(t);
	}

	public RequestSigningException(String message, Throwable t) {
		super(message, t);
	}
}