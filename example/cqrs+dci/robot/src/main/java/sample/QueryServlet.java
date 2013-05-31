package sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.domain.Robot;
import sample.query.RobotsQuery;

import com.jdon.controller.WebAppUtil;

/**
 * CQRS's query
 * 
 * @author banq
 * 
 */
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RobotsQuery robotsQuery = (RobotsQuery) WebAppUtil.getComponentInstance("robotsQuery", this.getServletContext());
		for (Robot robot : robotsQuery.getAllRobots()) {
			resp.getWriter().print(robot.getName());
			resp.getWriter().print("\n");
		}
		resp.getWriter().close();
	}
}
