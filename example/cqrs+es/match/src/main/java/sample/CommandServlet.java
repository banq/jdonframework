package sample;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.domain.Match;
import sample.domain.Score;
import sample.service.CommandService;

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

		String homename = req.getParameter("homename");
		String awayname = req.getParameter("awayname");
		System.out.println("home team name :" + homename + " join ");
		System.out.println("home away name :" + awayname + " join ");

		CommandService commandService = (CommandService) WebAppUtil.getService("commandService", this.getServletContext());
		String matchid = commandService.createMatchAggregate(homename, awayname);
		resp.getWriter().print("event Match created :" + matchid);
		resp.getWriter().print("\n");

		// waiting for event while
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Match match = commandService.getMatch(matchid);
		Date startDate = new Date();
		match.startMatch(startDate);
		resp.getWriter().print("the Match started at :" + startDate);
		resp.getWriter().print("\n");

		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Date fDate = new Date();
		Score score = new Score(1, 2);
		match.finishWithScore(score, fDate);
		resp.getWriter().print("the Match finished at :" + fDate + " score:" + score.getHomeGoals() + "/" + score.getAwayGoals());
		resp.getWriter().print("\n");

		resp.getWriter().close();
	}
}
