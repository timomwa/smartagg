package com.pixelandtag.smartagg.api.content;

public class ContentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2462544476030331655L;
	private String message;
	private Throwable throwable;
	private boolean writableStackTrace;
	private boolean enableSuppression;
	
	public ContentException() {
	}

	public ContentException(String message) {
		super(message);
		this.message = message;
	}

	public ContentException(Throwable cause) {
		super(cause);
		this.throwable = cause;
	}

	public ContentException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.throwable = cause;
	}

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean isWritableStackTrace() {
		return writableStackTrace;
	}

	public void setWritableStackTrace(boolean writableStackTrace) {
		this.writableStackTrace = writableStackTrace;
	}

	public boolean isEnableSuppression() {
		return enableSuppression;
	}

	public void setEnableSuppression(boolean enableSuppression) {
		this.enableSuppression = enableSuppression;
	}

	public ContentException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
		this.throwable = cause;
		this.writableStackTrace = writableStackTrace;
		this.enableSuppression = enableSuppression;
	}

}
