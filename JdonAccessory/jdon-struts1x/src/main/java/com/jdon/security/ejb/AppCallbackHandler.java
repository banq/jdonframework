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

package com.jdon.security.ejb;

import java.io.*;
import javax.security.auth.callback.*;

public class AppCallbackHandler implements CallbackHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8968985553117878091L;
	public AppCallbackHandler(String userId, String password) {
		_userId = userId;
		_password = password;
	}

	public void handle(Callback[] callbacks) {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callbacks[i];
				nameCallback.setName(_userId);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback passCallback = (PasswordCallback) callbacks[i];
				passCallback.setPassword(_password.toCharArray());
			}
		}
	}

	private String _userId;
	private String _password;

}
