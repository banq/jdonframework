package client;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ship.domain.HandlingEvent;
import ship.service.HandlingEventService;

import com.jdon.controller.WebAppUtil;

public class MyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String mode = req.getParameter("mode");
		if (mode.equals("yy")) {
			try {
				yy(req);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (mode.equals("unload")) {
			try {
				unload(req);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		resp.getWriter().print("server say hello to:");
		resp.getWriter().close();
	}

	private void yy(HttpServletRequest req) throws Exception {
		Date completionTime = new Date();
		String trackingId = req.getParameter("trackingId");
		String carrierMovementId = req.getParameter("carrierMovementId");
		String unlocode = req.getParameter("unlocode");
		HandlingEvent.Type type = HandlingEvent.Type.LOAD;
		HandlingEventService handlingEventService = (HandlingEventService) WebAppUtil.getService("handlingEventService", req);
		handlingEventService.register(completionTime, trackingId, carrierMovementId, unlocode, type);

	}

	private void unload(HttpServletRequest req) throws Exception {
		String trackingId = req.getParameter("trackingId");
		String carrierMovementId = req.getParameter("carrierMovementId");
		String unlocode = req.getParameter("unlocode");
		HandlingEventService handlingEventService = (HandlingEventService) WebAppUtil.getService("handlingEventService", req);
		handlingEventService.unLoad(carrierMovementId, unlocode, trackingId);
	}

}
