package org.htomar.openakamai.edge.auth.exception;

/**
 * Exception representing errors during request signing.
 * @author Himanshu Tomar
 */
public class RequestSigningException extends Exception {

    /**
     * Serial Version UID for serialization.
     */
    private static final long serialVersionUID = -4716437270940718895L;

    /**
     * Constructor for {@link RequestSigningException}.
     *
     * @param message   the exception message.
     * @param throwable the original thrown exception.
     */
    public RequestSigningException(String message, Throwable throwable) {
        super(message, throwable);
    }
}