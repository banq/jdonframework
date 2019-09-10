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

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.rmi.RemoteException;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.remote.auth.AuthException;
import com.jdon.util.Debug;

/**
 * 将客户端的方法调用等参数通过http协议发往J2EE服务器
 * 
 * 安全验证是采取服务器端基于Http的Basice Auth，因此J2EE服务器端需要设置。 可以使用一个专门的EJB网关服务器来专门供远程调用。
 * 
 * 
 * @author banq
 */
public class HttpClient {

	public final static String module = HttpClient.class.getName();

	private HttpServerParam httpServerParam;

	private static int requestNb;
	private String sessionId;

	private String userPasswordPair = null;

	/** Default Server File */
	private static final int DEFAULT_THREAD_COUNT = 1;

	/** The number of parallel thread used to perform the http call. */
	private int maxThreadCount = DEFAULT_THREAD_COUNT;

	/** The number of thread that are currently making event call */
	private int curUsedThread = 0;

	// Singleton attribute
	protected static HttpClient httpClient = new HttpClient();

	public static HttpClient getInstance() {
		return httpClient;
	}

	private HttpClient() {
		httpServerParam = new HttpServerParam();

	}

	/**
	 * 外界可以设置改变httpServerParam中的参数
	 * 
	 * @param httpServerParam
	 */
	public void setHttpServerParam(HttpServerParam httpServerParam) {
		this.httpServerParam = httpServerParam;
	}

	/**
	 * Invokes EJB service
	 */
	public Object invoke(TargetMetaDef targetMetaDef, Method m, Object[] args) throws Throwable {
		Object result = null;

		getThreadLock();

		int currentRequestNb = requestNb++;
		Debug.logVerbose("[JdonFramework]Start remote call " + currentRequestNb + " " + m.getName(), module);

		// 准备参数
		HttpRequest request = new HttpRequest(targetMetaDef, m.getName(), m.getParameterTypes(), args);

		StringBuilder sb = new StringBuilder(httpServerParam.getServletPath().toString());
		if (sessionId != null) {
			sb.append(";jsessionid=");
			sb.append(sessionId);
		}
		httpServerParam.setServletPath(sb.toString());

		result = invokeHttp(request, args);

		Debug.logVerbose("[JdonFramework]Ending remote call " + currentRequestNb, module);
		releaseThreadLock();

		return result;
	}

	/**
	 * Performs the http call.
	 */
	public Object invokeHttp(HttpRequest request, Object[] args) throws Throwable {
		HttpResponse httpResponse;
		try {
			HttpConnectionHelper httpConnectionHelper = new HttpConnectionHelper();

			HttpURLConnection httpURLConnection;
			if (httpServerParam.isDebug()) {// 调试方式无需安全验证
				// 连接服务器
				Debug.logVerbose("[JdonFramework]connect service..", module);
				httpURLConnection = httpConnectionHelper.connectService(httpServerParam, null);
				// 发出request
				Debug.logVerbose("[JdonFramework]send request: class=" + request.getTargetMetaDef().getClassName(), module);
				Debug.logVerbose("[JdonFramework]method=" + request.getMethodName(), module);
				httpConnectionHelper.sendObjectRequest(httpURLConnection, request);
			} else {
				httpURLConnection = httpConnectionHelper.connectService(httpServerParam, getUserPassword(args));
				// 发出request
				httpConnectionHelper.sendObjectRequest(httpURLConnection, request);
				// 接受response
				if (httpURLConnection.getResponseCode() == 401) {
					throw new AuthException(" http Server authentication failed!");
				}
			}

			// 接受response
			httpResponse = (HttpResponse) httpConnectionHelper.getObjectResponse(httpURLConnection);
			// 获得jsessionid
			sessionId = httpURLConnection.getHeaderField("jsessionid");

			// 断开连接
			httpURLConnection.disconnect();

			if (httpResponse.isExceptionThrown())
				throw httpResponse.getThrowable();
			return httpResponse.getResult();

		} catch (ClassNotFoundException e) {
			Debug.logError(e, module);
			throw new RemoteException(" Class Not Found ", e);
		} catch (AuthException ae) {
			throw new AuthException(ae.getMessage());
		} catch (Exception e) {
			String message = "invokeHttp error:";
			Debug.logError(message + e, module);
			throw new RemoteException(message, e);
		}

	}

	/**
	 * 用户第一次 Login调用
	 */
	public Object invokeAuth(Object[] args) throws Throwable {
		Object result = null;
		try {

			Debug.logVerbose("[JdonFramework] begin to auth from J2EE Server", module);

			HttpConnectionHelper httpConnectionHelper = new HttpConnectionHelper();

			// 连接服务器
			HttpURLConnection httpURLConnection = httpConnectionHelper.connectLogin(httpServerParam, getUserPassword(args));
			// 发出request

			// 传递一个参数，可以不用
			java.util.Hashtable params = new java.util.Hashtable();
			params.put("login", "1");
			httpConnectionHelper.sendDataRequest(httpURLConnection, params);

			// 接受response
			int status = httpURLConnection.getResponseCode();
			if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {
				throw new AuthException(" http Server authentication failed!");
			}

			result = httpConnectionHelper.getStringResponse(httpURLConnection);

			// 断开连接
			httpURLConnection.disconnect();

			// Debug.logVerbose("[JdonFramework]result:"+text, module);
		} catch (AuthException ae) {
			throw new AuthException(ae.getMessage());
		} catch (Exception e) {
			String message = "invokeAuth error:";
			Debug.logError(message + e, module);
			throw new RemoteException(message, e);
		}

		return result;
	}

	/**
	 * "username:password"
	 * 
	 * @param args
	 * @return
	 * @throws AuthException
	 */
	private String getUserPassword(Object[] args) throws AuthException {
		if (args == null)
			throw new AuthException("auth error: args is null");
		if ((userPasswordPair == null) || userPasswordPair.equals("")) {
			try {
				StringBuilder sb = new StringBuilder();
				if ((args[0] != null) && (args[1] != null)) {
					sb.append(args[0]);
					sb.append(":");
					sb.append(args[1]);
					userPasswordPair = sb.toString();
				}
			} catch (Exception e) {
				throw new AuthException("auth error: args is null");
			}
		}
		Debug.logVerbose("[JdonFramework] url param is" + userPasswordPair, module);
		return userPasswordPair;
	}

	/**
	 * This method is used to limit the concurrent http call to the max fixed by
	 * maxThreadCount and to wait the end of the first call that will return the
	 * session id.
	 */
	private synchronized void getThreadLock() {
		while (sessionId == null && curUsedThread > 1) {
			try {
				Debug.logVerbose("No session. Only one thread is authorized. Waiting ...", module);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while (curUsedThread >= maxThreadCount) {
			try {
				Debug.logVerbose("[JdonFramework]Max concurent http call reached. Waiting ...", module);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		curUsedThread++;
	}

	private synchronized void releaseThreadLock() {
		curUsedThread--;
		notify();
	}

	public int getThreadCount() {
		return maxThreadCount;
	}

	public void setThreadCount(int threadCount) {
		this.maxThreadCount = threadCount;

		Debug.logVerbose("[JdonFramework]Max concurrent thread set to " + threadCount, module);
	}

}
