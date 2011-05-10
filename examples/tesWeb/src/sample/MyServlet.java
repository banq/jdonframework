package sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.service.HelloService;

import com.jdon.controller.WebAppUtil;

public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String myname = req.getParameter("myname");
		System.out.println("doGet active :" + myname);
		HelloService helloService = (HelloService) WebAppUtil.getService("helloService", req);
		String result = helloService.hello(myname);

		resp.getWriter().print("server say hello to:" + result);
		resp.getWriter().close();
	}

}
