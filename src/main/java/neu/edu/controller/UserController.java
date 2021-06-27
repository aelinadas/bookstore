/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import neu.edu.applicationstats.Statistic;
import neu.edu.dao.BuyerDAO;
import neu.edu.dao.SellerDAO;
import neu.edu.utility.ValidateInputs;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class UserController extends AbstractController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    public UserController() {
    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long beginTime = System.currentTimeMillis();
        ModelAndView modelAndView = null;

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        try {
            if (action == null || action == "") {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                modelAndView = new ModelAndView("home");
                return modelAndView;
            } else if (action.equalsIgnoreCase("signup")) {
                SellerDAO sellerDAO = new SellerDAO();
                BuyerDAO buyerDAO = new BuyerDAO();
                String fname = request.getParameter("fname") == null ? "" : request.getParameter("fname");
                String lname = request.getParameter("lname") == null ? "" : request.getParameter("lname");
                String email = request.getParameter("email") == null ? "" : request.getParameter("email");
                String password = request.getParameter("password") == null ? "" : request.getParameter("password");
                String confirmpassword = request.getParameter("confirmpassword") == null ? "" : request.getParameter("confirmpassword");
                if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("") && !email.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !confirmpassword.equalsIgnoreCase("")) {
                    ValidateInputs vi = new ValidateInputs();
                    if (vi.isName(fname) && vi.isName(lname) && vi.isEmail(email) && vi.isPassword(password) && vi.isPassword(confirmpassword)) {
                        int seller = sellerDAO.addNewSeller(fname, lname, email, password);
                        int buyer = buyerDAO.addNewBuyer(fname, lname, email, password);
                        logger.info("User Added");
                        if (seller == 1 && buyer == 1) {
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                            modelAndView = new ModelAndView("success", "message", fname + "!! You have been successfully registered with us in both the accounts.");
                        } else {
                            modelAndView = new ModelAndView("error", "message", "Not able to add you to both accounts");
                        }
                    } else {
                        modelAndView = new ModelAndView("error", "message", "Not able to add you to both accounts");
                    }
                } else {
                    modelAndView = new ModelAndView("error", "message", "Not able to add you to both accounts");
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Page not found!");
        }
        return modelAndView;
    }
}
