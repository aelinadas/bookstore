/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import neu.edu.applicationstats.Statistic;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class HomeController extends AbstractController {

    private static final Logger logger = Logger.getLogger(HomeController.class);

    public HomeController() {
    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long beginTime = System.currentTimeMillis();
        ModelAndView mv = null;
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        try {
            if (action == null || action == "") {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                mv = new ModelAndView("home");
                return mv;
            } else if (action.equalsIgnoreCase("loginBuyer")) {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                mv = new ModelAndView("login-buyer");
            } else if (action.equalsIgnoreCase("loginSeller")) {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                mv = new ModelAndView("login-seller");
            } else if (action.equalsIgnoreCase("signupUser")) {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                mv = new ModelAndView("signup");
            } else if (action.equalsIgnoreCase("logout")) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                mv = new ModelAndView("home");
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Page not found!");
        }
        return mv;
    }

}
