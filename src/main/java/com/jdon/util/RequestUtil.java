package com.jdon.util;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RequestUtil utility class Good ol' copy-n-paste from <event
 * href="http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt">
 * http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt</event> which
 * is referenced in the following article: <event
 * href="http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html">
 * http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html</event>
 */
public class RequestUtil {
	private static final String STOWED_REQUEST_ATTRIBS = "ssl.redirect.attrib.stowed";

	private static final String JDON_AUTOLOGIN_COOKIE = "jdon.autologin";

	// "Tweakable" parameters for the cookie password encoding. NOTE: changing
	// these and recompiling this class will essentially invalidate old cookies.
	private final static int ENCODE_XORMASK = 0x5A;
	private final static char ENCODE_DELIMETER = '\002';
	private final static char ENCODE_CHAR_OFFSET1 = 'A';
	private final static char ENCODE_CHAR_OFFSET2 = 'h';

	/**
	 * Creates query String from request body parameters
	 */
	public static String getRequestParameters(HttpServletRequest aRequest) {
		// set the ALGORIGTHM as defined for the application
		// ALGORITHM = (String) aRequest.getAttribute(Constants.ENC_ALGORITHM);
		Map m = aRequest.getParameterMap();

		return createQueryStringFromMap(m, "&").toString();
	}

	/**
	 * Builds event query string from event given map of parameters
	 * 
	 * @param m
	 *            A map of parameters
	 * @param ampersand
	 *            String to use for ampersands (e.g. "&" or "&amp;" )
	 * 
	 * @return query string (with no leading "?")
	 */
	public static StringBuilder createQueryStringFromMap(Map m, String ampersand) {
		StringBuilder aReturn = new StringBuilder("");
		Set aEntryS = m.entrySet();
		Iterator aEntryI = aEntryS.iterator();

		while (aEntryI.hasNext()) {
			Map.Entry aEntry = (Map.Entry) aEntryI.next();
			Object o = aEntry.getValue();

			if (o == null) {
				append(aEntry.getKey(), "", aReturn, ampersand);
			} else if (o instanceof String) {
				append(aEntry.getKey(), o, aReturn, ampersand);
			} else if (o instanceof String[]) {
				String[] aValues = (String[]) o;

				for (int i = 0; i < aValues.length; i++) {
					append(aEntry.getKey(), aValues[i], aReturn, ampersand);
				}
			} else {
				append(aEntry.getKey(), o, aReturn, ampersand);
			}
		}

		return aReturn;
	}

	/**
	 * Appends new key and value pair to query string
	 * 
	 * @param key
	 *            parameter name
	 * @param value
	 *            value of parameter
	 * @param queryString
	 *            existing query string
	 * @param ampersand
	 *            string to use for ampersand (e.g. "&" or "&amp;")
	 * 
	 * @return query string (with no leading "?")
	 */
	private static StringBuilder append(Object key, Object value, StringBuilder queryString, String ampersand) {
		if (queryString.length() > 0) {
			queryString.append(ampersand);
		}

		// Use encodeURL from Struts' RequestUtils class - it's JDK 1.3 and 1.4
		// compliant
		queryString.append(encodeURL(key.toString()));
		queryString.append("=");
		queryString.append(encodeURL(value.toString()));

		return queryString;
	}

	/**
	 * Stores request attributes in session
	 * 
	 * @param aRequest
	 *            the current request
	 */
	public static void stowRequestAttributes(HttpServletRequest aRequest) {
		if (aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS) != null) {
			return;
		}

		Enumeration e = aRequest.getAttributeNames();
		Map map = new HashMap();

		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			map.put(name, aRequest.getAttribute(name));
		}

		aRequest.getSession().setAttribute(STOWED_REQUEST_ATTRIBS, map);
	}

	/**
	 * Returns request attributes from session to request
	 * 
	 * @param aRequest
	 *            DOCUMENT ME!
	 */
	public static void reclaimRequestAttributes(HttpServletRequest aRequest) {
		Map map = (Map) aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS);

		if (map == null) {
			return;
		}

		Iterator itr = map.keySet().iterator();

		while (itr.hasNext()) {
			String name = (String) itr.next();
			aRequest.setAttribute(name, map.get(name));
		}

		aRequest.getSession().removeAttribute(STOWED_REQUEST_ATTRIBS);
	}

	public static void saveAuthCookie(HttpServletResponse response, String username, String password) {
		// Save the cookie value for 1 month
		setCookie(response, JDON_AUTOLOGIN_COOKIE, encodePasswordCookie(username, password), "/");
	}

	public static String[] getAuthCookie(HttpServletRequest request) {
		Cookie cookie = getCookie(request, JDON_AUTOLOGIN_COOKIE);
		String[] values = null;
		if (cookie != null) {
			try {
				values = decodePasswordCookie(cookie.getValue());
			} catch (Exception e) {
				System.err.print("getAuthCookie() err:" + e);
			}
		}
		return values;
	}

	/**
	 * Convenience method to set event cookie
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @return HttpServletResponse
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path) {

		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setPath(path);
		cookie.setMaxAge(3600 * 24 * 30); // 30 days

		response.addCookie(cookie);
	}

	/**
	 * Convenience method to get event cookie by name
	 * 
	 * @param request
	 *            the current request
	 * @param name
	 *            the name of the cookie to find
	 * 
	 * @return the cookie (if found), null if not found
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		Cookie returnCookie = null;

		if (cookies == null) {
			return returnCookie;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie thisCookie = cookies[i];

			if (thisCookie.getName().equals(name)) {
				// cookies with no value do me no good!
				if (!thisCookie.getValue().equals("")) {
					returnCookie = thisCookie;

					break;
				}
			}
		}

		return returnCookie;
	}

	/**
	 * Convenience method for deleting event cookie by name
	 * 
	 * @param response
	 *            the current web response
	 * @param cookie
	 *            the cookie to delete
	 * 
	 * @return the modified response
	 */
	public static void deleteCookie(HttpServletResponse response, Cookie cookie, String path) {
		if (cookie != null) {
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setPath(path);
			response.addCookie(cookie);
		}
	}

	/**
	 * Convenience method to get the application's URL based on request
	 * variables.
	 */
	public static String getAppURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80; // Work around java.net.URL bug
		}
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		return url.toString();
	}

	public static String encodeURL(String url) {
		return encodeURL(url, "UTF-8");
	}

	/**
	 * Use the new URLEncoder.encode() method from Java 1.4 if available, else
	 * use the old deprecated version. This method uses reflection to find the
	 * appropriate method; if the reflection operations throw exceptions, this
	 * will return the url encoded with the old URLEncoder.encode() method.
	 * 
	 * @param enc
	 *            The character encoding the urlencode is performed on.
	 * @return String The encoded url.
	 */
	public static String encodeURL(String url, String enc) {
		try {

			if (enc == null || enc.length() == 0) {
				enc = "UTF-8";
			}

			return URLEncoder.encode(url, enc);

		} catch (Exception e) {
			System.err.print(e);
		}
		return null;
	}

	/**
	 * Builds event cookie string containing event username and password.
	 * <p>
	 * 
	 * Note: with open source this is not really secure, but it prevents users
	 * from snooping the cookie file of others and by changing the XOR mask and
	 * character offsets, you can easily tweak results.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password.
	 * @return String encoding the input parameters, an empty string if one of
	 *         the arguments equals <code>null</code>.
	 */
	private static String encodePasswordCookie(String username, String password) {
		StringBuffer buf = new StringBuffer();
		if (username != null && password != null) {
			byte[] bytes = (username + ENCODE_DELIMETER + password).getBytes();
			int b;

			for (int n = 0; n < bytes.length; n++) {
				b = bytes[n] ^ (ENCODE_XORMASK + n);
				buf.append((char) (ENCODE_CHAR_OFFSET1 + (b & 0x0F)));
				buf.append((char) (ENCODE_CHAR_OFFSET2 + ((b >> 4) & 0x0F)));
			}
		}
		return buf.toString();
	}

	/**
	 * Unrafels event cookie string containing event username and password.
	 * 
	 * @param value
	 *            The cookie value.
	 * @return String[] containing the username at index 0 and the password at
	 *         index 1, or <code>{ null, null }</code> if cookieVal equals
	 *         <code>null</code> or the empty string.
	 */
	private static String[] decodePasswordCookie(String cookieVal) {

		// check that the cookie value isn't null or zero-length
		if (cookieVal == null || cookieVal.length() <= 0) {
			return null;
		}

		// unrafel the cookie value
		char[] chars = cookieVal.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int b;
		for (int n = 0, m = 0; n < bytes.length; n++) {
			b = chars[m++] - ENCODE_CHAR_OFFSET1;
			b |= (chars[m++] - ENCODE_CHAR_OFFSET2) << 4;
			bytes[n] = (byte) (b ^ (ENCODE_XORMASK + n));
		}
		cookieVal = new String(bytes);
		int pos = cookieVal.indexOf(ENCODE_DELIMETER);
		String username = (pos < 0) ? "" : cookieVal.substring(0, pos);
		String password = (pos < 0) ? "" : cookieVal.substring(pos + 1);

		return new String[] { username, password };
	}

}
