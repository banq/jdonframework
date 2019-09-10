package com.jdon.bussinessproxy.remote.hessian.exception;

public class ServiceInvokationException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3575833201780697521L;

	/**
     * Constructs event new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by event
     * call to {@link #initCause}.
     */
    public ServiceInvokationException() {
        super();
    }

    /**
     * Constructs event new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * event call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ServiceInvokationException(String message) {
        super(message);
    }

    /**
     * Constructs event new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public ServiceInvokationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs event new exception with the specified cause and event detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public ServiceInvokationException(Throwable cause) {
        super(cause);
    }

}
