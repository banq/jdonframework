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

package com.jdon.bussinessproxy.remote.http;


import java.io.Serializable;

/**
 * This class holds the result of event client call.
 * Depending on the server, the response can be event result or an exception.
 */
public class HttpResponse implements Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 6611705280584660228L;
	private Throwable throwable;
    private Object result;

    public HttpResponse(Throwable throwable) {
        this.throwable = throwable;
    }

    public HttpResponse(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isExceptionThrown() {
        if (throwable != null) return true;
        return false;
    }
}
