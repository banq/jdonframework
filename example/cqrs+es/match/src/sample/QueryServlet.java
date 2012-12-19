package sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.domain.Match;
import sample.query.MatchListQuery;

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

		MatchListQuery matchListQuery = (MatchListQuery) WebAppUtil.getComponentInstance("matchListQuery", this.getServletContext());
		for (Match match : matchListQuery.getAllMatchs()) {
			resp.getWriter().print(match.getId() + " finished state:" + match.isFinished());
			resp.getWriter().print("\n");
		}
		resp.getWriter().close();
	}
}
