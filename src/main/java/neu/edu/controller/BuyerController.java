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
import neu.edu.dao.BuyerDAO;
import neu.edu.pojo.Buyer;
import neu.edu.utility.ValidateInputs;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class BuyerController extends AbstractController {

    private static final Logger logger = Logger.getLogger(BuyerController.class);

    public BuyerController() {
    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long beginTime = System.currentTimeMillis();
        ModelAndView modelAndView = null;

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        try {
            if (action == null || action == "") {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                modelAndView = new ModelAndView("buyer-home");
                return modelAndView;
            } else if (action.equalsIgnoreCase("updateBuyer")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("buyer") != null) {
                    statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                    
                    modelAndView = new ModelAndView("update-buyer");
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Update function requested is not available");
                }
            } else if (action.equalsIgnoreCase("updatePassword")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("buyer") != null) {
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                    
                    modelAndView = new ModelAndView("update-buyer-password");
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Update function requested is not available");
                }
            } else if (action.equalsIgnoreCase("signup")) {
                BuyerDAO buyerDAO = new BuyerDAO();
                String fname = request.getParameter("fname") == null ? "" : request.getParameter("fname");
                String lname = request.getParameter("lname") == null ? "" : request.getParameter("lname");
                String email = request.getParameter("email") == null ? "" : request.getParameter("email");
                String password = request.getParameter("password") == null ? "" : request.getParameter("password");
                String confirmpassword = request.getParameter("confirmpassword") == null ? "" : request.getParameter("confirmpassword");
                if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("") && !email.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !confirmpassword.equalsIgnoreCase("")) {
                    ValidateInputs vi = new ValidateInputs();
                    if (vi.isName(fname) && vi.isName(lname) && vi.isEmail(email) && vi.isPassword(password) && vi.isPassword(confirmpassword)) {
                        int result = buyerDAO.addNewBuyer(fname, lname, email, password);
                        logger.info("Buyer added");
                        if (result == 1) {
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                            
                            modelAndView = new ModelAndView("buyer-success", "message", fname + "!! You have been successfully registered with us.");
                        } else {
                            modelAndView = new ModelAndView("buyer-error", "message", "This Email ID already exists");
                        }
                    } else {
                        modelAndView = new ModelAndView("buyer-error", "message", "Not able to add Buyer");
                    }
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Not able to add Buyer");
                }
            } else if (action.equalsIgnoreCase("update")) {
                BuyerDAO buyerDAO = new BuyerDAO();
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("buyer") != null) {
                    Buyer buyer = (Buyer) session.getAttribute("buyer");
                    String fname = request.getParameter("fname") == null ? "" : request.getParameter("fname");
                    String lname = request.getParameter("lname") == null ? "" : request.getParameter("lname");
                    if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("")) {
                        ValidateInputs vi = new ValidateInputs();
                        if (vi.isName(fname) && vi.isName(lname)) {
                            int result = buyerDAO.updateBuyer(buyer, fname, lname);
                            logger.info("Buyer Updated");
                            if (result == 1) {
                                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                                
                                modelAndView = new ModelAndView("buyer-success", "message", fname + "!! Your information has been successfully updated");
                            } else {
                                modelAndView = new ModelAndView("buyer-error", "message", "Sorry " + fname + " cannot be updated at this point of time");
                            }
                        } else {
                            modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                        }

                    } else {
                        modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                    }

                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                }
            } else if (action.equalsIgnoreCase("updatePwd")) {
                BuyerDAO buyerDAO = new BuyerDAO();
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("buyer") != null) {
                    Buyer buyer = (Buyer) session.getAttribute("buyer");
                    String newpassword = request.getParameter("newpassword") == null ? "" : request.getParameter("newpassword");
                    String confirmpassword = request.getParameter("confirmpassword") == null ? "" : request.getParameter("confirmpassword");
                    if (!newpassword.equalsIgnoreCase("") && !confirmpassword.equalsIgnoreCase("")) {
                        ValidateInputs vi = new ValidateInputs();
                        if (vi.isPassword(newpassword) && vi.isPassword(confirmpassword)) {
                            int result = buyerDAO.updatePassword(buyer, newpassword, confirmpassword);
                            logger.info("Buyer Pasword Updated");
                            if (result == 1) {
                                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                                
                                modelAndView = new ModelAndView("buyer-success", "message", "Your information has been successfully updated");
                            } else {
                                modelAndView = new ModelAndView("buyer-error", "message", "Sorry " + "Cannot be updated at this point of time");
                            }
                        } else {
                            modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                        }
                    } else {
                        modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                    }
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Cannot be updated at this point of time");
                }
            } else if (action.equalsIgnoreCase("viewBuyer")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("buyer") != null) {
                    Buyer buyer = (Buyer) session.getAttribute("buyer");
                    if (buyer != null) {
                        BuyerDAO buyerDAO = new BuyerDAO();
                        Buyer b1 = buyerDAO.getBuyerById(buyer.getId());
                        if (b1 != null) {
                            session.removeAttribute("buyer");
                            session.setAttribute("buyer", b1);
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                            
                            modelAndView = new ModelAndView("buyer-details");
                        } else {
                            modelAndView = new ModelAndView("buyer-error", "message", "Not able to view User");
                        }
                    } else {
                        modelAndView = new ModelAndView("buyer-error", "message", "Not able to view User");
                    }
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Sorry the page is currently unavailable please login after some time");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return modelAndView;
    }
}
