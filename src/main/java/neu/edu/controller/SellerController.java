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
import neu.edu.dao.SellerDAO;
import neu.edu.pojo.Seller;
import neu.edu.utility.ValidateInputs;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class SellerController extends AbstractController {

    public SellerController() {
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

                modelAndView = new ModelAndView("seller-home");
                return modelAndView;
            } else if (action.equalsIgnoreCase("updateSeller")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("seller") != null) {
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                    modelAndView = new ModelAndView("update-seller");
                } else {
                    modelAndView = new ModelAndView("seller-error", "message", "Update function requested is not available");
                }
            } else if (action.equalsIgnoreCase("updatePassword")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("seller") != null) {
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                    modelAndView = new ModelAndView("update-seller-password");
                } else {
                    modelAndView = new ModelAndView("seller-error", "message", "Update function requested is not available");
                }
            } else if (action.equalsIgnoreCase("signup")) {
                SellerDAO sellerDAO = new SellerDAO();
                String fname = request.getParameter("fname") == null ? "" : request.getParameter("fname");
                String lname = request.getParameter("lname") == null ? "" : request.getParameter("lname");
                String email = request.getParameter("email") == null ? "" : request.getParameter("email");
                String password = request.getParameter("password") == null ? "" : request.getParameter("password");
                String confirmpassword = request.getParameter("confirmpassword") == null ? "" : request.getParameter("confirmpassword");
                if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("") && !email.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !confirmpassword.equalsIgnoreCase("")) {
                    ValidateInputs vi = new ValidateInputs();
                    if (vi.isName(fname) && vi.isName(lname) && vi.isEmail(email) && vi.isPassword(password) && vi.isPassword(confirmpassword)) {
                        int result = sellerDAO.addNewSeller(fname, lname, email, password);
                        if (result == 1) {
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                            modelAndView = new ModelAndView("success", "message", fname + "!! You have been successfully registered with us.");
                        } else {
                            modelAndView = new ModelAndView("error", "message", "This Email ID already exists");
                        }
                    } else {
                        modelAndView = new ModelAndView("error", "message", "Not able to add Seller");
                    }
                } else {
                    modelAndView = new ModelAndView("error", "message", "Not able to add Seller");
                }
            } else if (action.equalsIgnoreCase("update")) {
                SellerDAO sellerDAO = new SellerDAO();
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("seller") != null) {
                    Seller seller = (Seller) session.getAttribute("seller");
                    String fname = request.getParameter("fname") == null ? "" : request.getParameter("fname");
                    String lname = request.getParameter("lname") == null ? "" : request.getParameter("lname");
                    if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("")) {
                        ValidateInputs vi = new ValidateInputs();
                        if (vi.isName(fname) && vi.isName(lname)) {
                            int result = sellerDAO.updateSeller(seller, fname, lname);
                            if (result == 1) {
                                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                                modelAndView = new ModelAndView("seller-success", "message", fname + "!! Your information has been successfully updated");
                            } else {
                                modelAndView = new ModelAndView("sellet-error", "message", "Sorry " + fname + " cannot be updated at this point of time");
                            }
                        } else {
                            modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                        }

                    } else {
                        modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                    }

                } else {
                    modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                }
            } else if (action.equalsIgnoreCase("updatePwd")) {
                SellerDAO sellerDAO = new SellerDAO();
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("seller") != null) {
                    Seller seller = (Seller) session.getAttribute("seller");
                    String newpassword = request.getParameter("newpassword") == null ? "" : request.getParameter("newpassword");
                    String confirmpassword = request.getParameter("confirmpassword") == null ? "" : request.getParameter("confirmpassword");
                    if (!newpassword.equalsIgnoreCase("") && !confirmpassword.equalsIgnoreCase("")) {
                        ValidateInputs vi = new ValidateInputs();
                        if (vi.isPassword(newpassword) && vi.isPassword(confirmpassword)) {
                            int result = sellerDAO.updatePassword(seller, newpassword, confirmpassword);
                            if (result == 1) {
                                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                                modelAndView = new ModelAndView("seller-success", "message", "Your information has been successfully updated");
                            } else {
                                modelAndView = new ModelAndView("seller-error", "message", "Sorry " + "Cannot be updated at this point of time");
                            }
                        } else {
                            modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                        }
                    } else {
                        modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                    }
                } else {
                    modelAndView = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                }
            } else if (action.equalsIgnoreCase("viewSeller")) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("seller") != null) {
                    Seller seller = (Seller) session.getAttribute("seller");
                    if (seller != null) {
                        SellerDAO sellerDAO = new SellerDAO();
                        Seller s1 = sellerDAO.getSellerById(seller.getId());
                        if (s1 != null) {
                            session.removeAttribute("seller");
                            session.setAttribute("seller", s1);
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                            modelAndView = new ModelAndView("seller-details");
                        } else {
                            modelAndView = new ModelAndView("seller-error", "message", "Not able to view User");
                        }
                    } else {
                        modelAndView = new ModelAndView("seller-error", "message", "Not able to view User");
                    }
                } else {
                    modelAndView = new ModelAndView("seller-error", "message", "Sorry the page is currently unavailable please login after some time");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return modelAndView;
    }
}
