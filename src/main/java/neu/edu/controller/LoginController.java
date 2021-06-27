/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import neu.edu.applicationstats.Statistic;
import neu.edu.dao.BuyerDAO;
import neu.edu.dao.SellerDAO;
import neu.edu.pojo.Buyer;
import neu.edu.pojo.Seller;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class LoginController extends AbstractController {

    private static final Logger logger = Logger.getLogger(LoginController.class);

    public LoginController() {

    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long beginTime = System.currentTimeMillis();
        ModelAndView mv = null;

        try {
            String action = request.getParameter("action") != null ? request.getParameter("action") : "";
            if (action.equalsIgnoreCase("loginbuyer")) {
                String email = request.getParameter("email") != null ? request.getParameter("email") : "";
                String password = request.getParameter("password") != null ? request.getParameter("password") : "";
                if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
                    BuyerDAO buyerDAO = new BuyerDAO();
                    Buyer buyer = buyerDAO.authenticateBuyer(email, password);
                    if (buyer != null) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("buyer", buyer);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("buyer-home");
                    } else {
                        mv = new ModelAndView("error", "message", "Please recheck your Username and Password");
                    }
                } else {
                    mv = new ModelAndView("error", "message", "Please recheck your Username and Password");
                }
            } else if (action.equalsIgnoreCase("loginseller")) {
                String email = request.getParameter("email") != null ? request.getParameter("email") : "";
                String password = request.getParameter("password") != null ? request.getParameter("password") : "";
                if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
                    SellerDAO sellerDAO = new SellerDAO();
                    Seller seller = sellerDAO.authenticateSeller(email, password);
                    if (seller != null) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("seller", seller);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("seller-home");
                    } else {
                        mv = new ModelAndView("error", "message", "Please recheck your Username and Password");
                    }
                } else {
                    mv = new ModelAndView("error", "message", "Please recheck your Username and Password");
                }
            }else if (action.equalsIgnoreCase("forgotpwd")) {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                mv = new ModelAndView("forgot-password");
            }else if (action.equalsIgnoreCase("generateToken")) {
                String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
                if (!email.equalsIgnoreCase("")) {
                    SellerDAO sellerDAO = new SellerDAO();
                    boolean validEmail = sellerDAO.validUser(email);
                    if (validEmail) {
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        AmazonSNS snsClient = AmazonSNSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
                        List<Topic> topics = snsClient.listTopics().getTopics();
                        for (Topic topic : topics) {
                            if (topic.getTopicArn().endsWith("password_reset")) {
                                PublishRequest req = new PublishRequest(topic.getTopicArn(), email);
                                snsClient.publish(req);
                                break;
                            }
                        }
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                        mv = new ModelAndView("success", "message", "Link successfully sent to you email");
                    } else {
                        mv = new ModelAndView("error", "message", "Please recheck your email");
                    }
                } else {
                    mv = new ModelAndView("error", "message", "Please recheck your email");
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println(e.getMessage());
        }
        return mv;
    }

}
