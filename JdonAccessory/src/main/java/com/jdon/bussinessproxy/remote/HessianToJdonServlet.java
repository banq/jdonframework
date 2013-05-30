package com.jdon.bussinessproxy.remote;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdon.bussinessproxy.remote.hessian.HessianToJdonRequestProcessor;

/**
 * 
 * web.xml: 
 *  <servlet>
        <servlet-name>Hessian2Jdon</servlet-name>
        <servlet-class>com.jdon.bussinessproxy.remote.HessianToJdonServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>Hessian2Jdon</servlet-name>
        <url-pattern>/remote/*</url-pattern>
    </servlet-mapping>
 * 
 * * client code:
 * HessianProxyFactory factory = new HessianProxyFactory();
   HelloService	_service = (HelloService) factory.create(HelloService.class, _url);
   _service.hello(s);
 * 
 * project example in demo: remote_javafx
 *  ref: http://www.jdon.com/jivejdon/thread/36483.html
 *
 */
public class HessianToJdonServlet extends GenericServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2038788488170819251L;
	private HessianToJdonRequestProcessor htorp;

    public void init() throws javax.servlet.ServletException{
    	htorp = new HessianToJdonRequestProcessor();
    }
    
    /**
     * Servlet to handle incoming Hessian requests and invoke HessianToJdonRequestProcessor.
     * 
     * @param req ServletRequest
     * @param resp ServletResponse
     * @throws javax.servlet.ServletException If errors occur
     * @throws java.io.IOException If IO errors occur
     */
    @Override
    public void service(final ServletRequest req, final ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        final String beanName = request.getPathInfo().substring(1); // remove "/"
        htorp.process(beanName, request, response);
    }
    
    public void destroy(){
    	htorp.clear();
    }
}
