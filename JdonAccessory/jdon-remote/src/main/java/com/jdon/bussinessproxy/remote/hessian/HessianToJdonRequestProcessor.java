package com.jdon.bussinessproxy.remote.hessian;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.services.server.AbstractSkeleton;
import com.jdon.bussinessproxy.remote.hessian.exception.ServiceInvokationException;
import com.jdon.bussinessproxy.remote.hessian.io.JdonSerializerFactory;
import com.jdon.controller.WebAppUtil;

public class HessianToJdonRequestProcessor {

	/**
	 * Holds value of major version Hessian 2.0 Web Service Protocol.
	 */
	public static final int HESSIAN_PROTOCOL_MAJOR_VERSION = 2;

	/*
	 * Map to store from call to call mangled method names calculated by Hessian
	 */
	private final Map<Class<?>, Map<String, Method>> methodsByComponentType = Collections
			.synchronizedMap(new HashMap<Class<?>, Map<String, Method>>());
	

	/**
	 * Process servlet requests and writes bean's method result to output
	 * 
	 * @param beanName
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws javax.servlet.ServletException
	 *             If error occur
	 * @throws java.io.IOException
	 *             If error occur
	 */
	public void process(final String beanName,
			final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		InputStream is = request.getInputStream();
		OutputStream os = response.getOutputStream();

		Hessian2Input in = new Hessian2Input(is);
		AbstractHessianOutput out;

		SerializerFactory serializerFactory = new SerializerFactory();

		serializerFactory.setAllowNonSerializable(true);

		serializerFactory.addFactory(new JdonSerializerFactory());
		in.setSerializerFactory(serializerFactory);

		int code = in.read();

		if (code != 'c') {
			// XXX: deflate
			throw new IOException("expected 'c' in hessian input at " + code);
		}

		int major = in.read();
		in.read();

		if (major >= HESSIAN_PROTOCOL_MAJOR_VERSION) {
			out = new Hessian2Output(os);
		} else {
			out = new HessianOutput(os);
		}

		out.setSerializerFactory(serializerFactory);
		// backward compatibility for some frameworks that don't read
		// the call type first
		in.skipOptionalCall();

		out.startReply();

		readHeaders(in); // read headers from call

		try {
			out.writeObject(makeCall(in, beanName, request));
		} catch (Exception e) {
			writeException(out, e);
		}

		// The complete call needs to be after the invoke to handle event
		// trailing InputStream
		in.completeCall();
		out.completeReply();
		out.close();

	}

	public Object makeCall(Hessian2Input in, String beanName,
			HttpServletRequest request) throws IOException,
			ServiceInvokationException {
		Object result = null;
		Object bean = WebAppUtil.getService(beanName, request);

		if (bean == null) {
			throw new ServiceInvokationException(MessageFormat.format(
					"Could not find bean: {0}", beanName));
		} else {
			try {
				String mangledMethodName = in.readMethod();
				Method method = getMethod(bean.getClass(), mangledMethodName);
				if ("_hessian_getAttribute".equals(mangledMethodName))
					result = bean.getClass();
				else {

					Class<?>[] argsTypes = method.getParameterTypes();
					Object[] args = new Object[argsTypes.length];

					for (int i = 0; i < argsTypes.length; i++) {
						args[i] = in.readObject(argsTypes[i]);
					}

					result = WebAppUtil.callService(beanName,
							mangledMethodName, args, request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}
	}

	/*
	 * Finds method of class by using mangled name from incoming request
	 */
	public Method getMethod(Class clz, String mangledMethodName) {

		Map<String, Method> methods = methodsByComponentType.get(clz);

		if (methods == null) {
			methods = new HashMap<String, Method>();
			methodsByComponentType.put(clz, methods);

			for (Method method : clz.getDeclaredMethods()) {
				Class<?>[] param = method.getParameterTypes();
				String mangledName1 = method.getName() + "__" + param.length;
				String mangledName2 = AbstractSkeleton
						.mangleName(method, false);

				if (param.length == 0 || !methods.containsKey(method.getName())) {
					methods.put(method.getName(), method);
				}

				methods.put(mangledName1, method);
				methods.put(mangledName2, method);
			}
		}

		return (Method) methods.get(mangledMethodName);
	}

	/**
	 * Reads headers from call.
	 * 
	 * @param hessian2Input -
	 *            HessianInput
	 * @return Map of headers
	 * @throws java.io.IOException
	 *             If error occurs
	 */
	public Map readHeaders(Hessian2Input hessian2Input) throws IOException {
		Map headers = new HashMap();
		String header = hessian2Input.readHeader();
		while (header != null) {
			headers.put(header, hessian2Input.readObject());
			header = hessian2Input.readHeader();
		}
		return headers;
	}

	/**
	 * Writes Exception information to Hessian Output.
	 * 
	 * @param out
	 *            Hessian Output
	 * @param ex
	 *            Exception
	 * @throws java.io.IOException
	 *             If i/o error occur
	 */
	protected void writeException(final AbstractHessianOutput out, Exception ex)
			throws IOException {
		OutputStream os = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(os));
		out.writeFault(ex.getClass().toString(), os.toString(), null);
	}
	
	public void clear(){
		this.methodsByComponentType.clear();
	}

}
