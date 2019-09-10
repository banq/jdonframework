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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;

import com.jdon.util.Base64;
import com.jdon.util.Debug;

public class HttpConnectionHelper {

	public final static String module = HttpConnectionHelper.class.getName();

	/**
	 * 连接Http Server， 准备传送serialized-object
	 * 
	 * @param httpServerParam
	 * @return
	 * @throws java.lang.Exception
	 */
	public HttpURLConnection connectService(HttpServerParam httpServerParam, String userPassword) throws Exception {
		HttpURLConnection httpURLConnection = null;
		URL url = null;
		try {

			url = new URL("http", httpServerParam.getHost(), httpServerParam.getPort(), httpServerParam.getServletPath());

			Debug.logVerbose("[JdonFramework]Service url=" + url, module);

			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

			if ((userPassword != null) && (!userPassword.equals("null"))) {
				String encoded = "Basic " + Base64.encodeBytes(userPassword.getBytes("UTF-8"));
				httpURLConnection.setRequestProperty("Authorization", encoded);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] connectServer " + url + " error: " + ex, module);
			throw new Exception(ex);
		}
		return httpURLConnection;
	}

	/**
	 * 连接Http Server， 准备传送HttpServletRequest
	 * 
	 * @param httpServerParam
	 * @return
	 * @throws java.lang.Exception
	 */
	public HttpURLConnection connectLogin(HttpServerParam httpServerParam, String UserPassword) throws Exception {

		HttpURLConnection httpURLConnection = null;
		URL url = null;
		try {

			url = new URL("http", httpServerParam.getHost(), httpServerParam.getPort(), httpServerParam.getLoginPath());

			Debug.logVerbose("[JdonFramework]login url=" + url, module);

			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String encoded = "Basic " + Base64.encodeBytes(UserPassword.getBytes("UTF-8"));
			httpURLConnection.setRequestProperty("Authorization", encoded);

		} catch (Exception ex) {
			Debug.logError("[JdonFramework] connectServer " + url + " error: " + ex, module);
			throw new Exception(ex);
		}
		return httpURLConnection;
	}

	/**
	 * 将可序列化Object发往Http Server
	 * 
	 * @param httpURLConnection
	 * @param request
	 * @throws java.lang.Exception
	 */
	public void sendObjectRequest(HttpURLConnection httpURLConnection, Object request) throws Exception {
		try {

			// send the request query object to the server
			ObjectOutputStream oos = new ObjectOutputStream(httpURLConnection.getOutputStream());
			oos.writeObject(request);
			oos.close();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		}
	}

	/**
	 * 将字符串发往Http
	 * 
	 * @param httpURLConnection
	 * @param sendText
	 * @throws java.lang.Exception
	 */
	public void sendDataRequest(HttpURLConnection httpURLConnection, Hashtable param) throws Exception {
		try {
			// write out parameters, assume parameters are put in the hashtable
			PrintWriter out = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
			// open up the output stream of the connection
			// DataOutputStream output = new
			// DataOutputStream(conn.getOutputStream() );

			String paramString = "";
			for (Enumeration e = param.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) param.get(key);
				// no harm for an extra & at the end of the parameter list
				paramString += key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
			}
			paramString = paramString.substring(0, paramString.length() - 1);

			// output.writeBytes( paramString );
			// output.close();

			out.println(paramString);
			out.close();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		}

	}

	/**
	 * 从回复中获取Object
	 * 
	 * @param httpURLConnection
	 * @return
	 * @throws java.lang.Exception
	 */
	public Object getObjectResponse(HttpURLConnection httpURLConnection) throws Exception {
		Object object = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(httpURLConnection.getInputStream());
			object = ois.readObject();
			ois.close();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		}
		return object;
	}

	public String getStringResponse(HttpURLConnection httpURLConnection) throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			// 测试时打印
			BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String buffer = "";
			while ((buffer = in.readLine()) != null) {
				sb.append(buffer);
				Debug.logVerbose(buffer, module);
			}
			in.close();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		}
		return sb.toString();
	}

}
