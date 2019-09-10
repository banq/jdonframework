/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.bussinessproxy.remote.auth;

public class AuthException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5825798808750497139L;
	private Exception exception;

	public AuthException(String message, Exception exception) {
		super(message);
		this.exception = exception;
		return;
	}

	public AuthException(String message) {
		this(message, null);
		return;
	}

	public AuthException(Exception exception) {
		this(null, exception);
		return;
	}

	public Exception getException() {
		return exception;
	}

}
