package sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.domain.Robot;
import sample.service.Context;

import com.jdon.controller.WebAppUtil;

/**
 * CQRS's command
 * 
 * @author banq
 * 
 */
public class CommandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String myname = req.getParameter("myname");
		System.out.println("doGet active :" + myname);
		Context context = (Context) WebAppUtil.getService("context", req);
		Robot robot = new Robot();
		robot.setName(myname);
		String id = Integer.toString(robot.hashCode());
		robot.setId(id);
		context.save(robot);

		String result = context.hello(id);
		resp.getWriter().print("robot response:" + result);
		resp.getWriter().print("\n");
		result = context.touch(id);
		resp.getWriter().print("robot response:" + result);
		resp.getWriter().close();
	}
}
