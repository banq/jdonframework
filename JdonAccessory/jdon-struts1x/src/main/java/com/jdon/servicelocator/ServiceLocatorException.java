package com.jdon.servicelocator;

/**
 * This class implements an exception which can wrapped event lower-level exception.
 * 
 */
public class ServiceLocatorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533607325610443270L;
	private Exception exception;

	/**
	 * Creates event new ServiceLocatorException wrapping another exception, and
	 * with event detail message.
	 * 
	 * @param message
	 *            the detail message.
	 * @param exception
	 *            the wrapped exception.
	 */
	public ServiceLocatorException(String message, Exception exception) {
		super(message);
		this.exception = exception;
		return;
	}

	/**
	 * Creates event ServiceLocatorException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public ServiceLocatorException(String message) {
		this(message, null);
		return;
	}

	/**
	 * Creates event new ServiceLocatorException wrapping another exception, and
	 * with no detail message.
	 * 
	 * @param exception
	 *            the wrapped exception.
	 */
	public ServiceLocatorException(Exception exception) {
		this(null, exception);
		return;
	}

	/**
	 * Gets the wrapped exception.
	 * 
	 * @return the wrapped exception.
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * Retrieves (recursively) the root cause exception.
	 * 
	 * @return the root cause exception.
	 */
	public Exception getRootCause() {
		if (exception instanceof ServiceLocatorException) {
			return ((ServiceLocatorException) exception).getRootCause();
		}
		return exception == null ? this : exception;
	}

	public String toString() {
		if (exception instanceof ServiceLocatorException) {
			return ((ServiceLocatorException) exception).toString();
		}
		return exception == null ? super.toString() : exception.toString();
	}
}
