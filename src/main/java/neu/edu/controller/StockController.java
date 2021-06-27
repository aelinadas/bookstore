/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import neu.edu.applicationstats.Statistic;
import neu.edu.dao.BookDAO;
import neu.edu.pojo.Book;
import neu.edu.pojo.Seller;
import neu.edu.utility.ValidateInputs;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class StockController extends AbstractController {

    private static final Logger logger = Logger.getLogger(StockController.class);

    public StockController() {
    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Seller seller = (Seller) request.getSession(false).getAttribute("seller");
        Long beginTime = System.currentTimeMillis();
        ModelAndView mv = null;
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        try {
            if (action == null || action == "") {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                
                mv = new ModelAndView("seller-home");
            } else if (action.equalsIgnoreCase("addBook")) {
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                
                mv = new ModelAndView("add-book");
            } else if (action.equalsIgnoreCase("viewbook")) {
                BookDAO bookDAO = new BookDAO();
                ArrayList<Book> books = bookDAO.getAllBookList(seller);
                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                
                mv = new ModelAndView("view-all-books", "books", books);
            } else if (action.equals("delete") && seller != null) {
                BookDAO bookDAO = new BookDAO();
                String bookId = request.getParameter("id") != null ? request.getParameter("id") : "";
                if (!bookId.equalsIgnoreCase("")) {
                    int result = bookDAO.deleteBook(Integer.parseInt(bookId));
                    if (result == 1) {
                        ArrayList<Book> books = bookDAO.getAllBookList(seller);
                        statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("view-all-books", "books", books);
                    } else {
                        mv = new ModelAndView("seller-error", "message", "Book you requested for does not exist");
                    }
                } else {
                    mv = new ModelAndView("seller-error", "message", "Book can't be deleted");
                }
            } else if (action.equalsIgnoreCase("update")) {
                String bookId = request.getParameter("id") != null ? request.getParameter("id") : "";
                if (!bookId.equalsIgnoreCase("")) {
                    BookDAO bookDAO = new BookDAO();
                    Book books = bookDAO.getBookBasedOnID(Integer.parseInt(bookId));
                    if (books != null) {
                        Map<String, String> imgMap = bookDAO.getImageUrlsWithIDISBN(books.getId(), books.getISBN());
                        request.setAttribute("imgMap", imgMap);
                        statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("update-book", "books", books);
                    } else {
                        mv = new ModelAndView("seller-error", "message", "Book you requested for does not exist");
                    }
                }
            } else if (action.equalsIgnoreCase("updateBook") && seller != null) {
                String id = request.getParameter("id") != null ? request.getParameter("id") : "";
                String title = request.getParameter("title") != null ? request.getParameter("title") : "";
                String authors = request.getParameter("authors") != null ? request.getParameter("authors") : "";
                String publicationDate = request.getParameter("publicationDate") != null ? request.getParameter("publicationDate") : "";
                String quantity = request.getParameter("quantity") != null ? request.getParameter("quantity") : "";
                String price = request.getParameter("price") != null ? request.getParameter("price") : "";

                if (!id.equalsIgnoreCase("") && !title.equalsIgnoreCase("") && !authors.equalsIgnoreCase("") && !publicationDate.equalsIgnoreCase("")
                        && !quantity.equalsIgnoreCase("") && !price.equalsIgnoreCase("")) {
                    ValidateInputs vi = new ValidateInputs();
                    if (vi.isName(title) && vi.isAuthors(authors) && vi.isDate(publicationDate) && vi.isQuantity(quantity) && vi.isDecimal(price)) {
                        BookDAO bookDAO = new BookDAO();
                        int result = bookDAO.updateBook(Integer.parseInt(id), title, authors, Date.valueOf(publicationDate), Integer.parseInt(quantity), Double.parseDouble(price), seller);
                        if (result == 1) {
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                            mv = new ModelAndView("seller-success", "message", "Your product has been successfully updated");
                        } else {
                            mv = new ModelAndView("seller-error", "message", "Product cannot be updated at this point of time");
                        }
                    } else {
                        mv = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                    }
                } else {
                    mv = new ModelAndView("seller-error", "message", "Cannot be updated at this point of time");
                }
            } else if (action.equals("deleteImage") && seller != null) {
                String imageKey = request.getParameter("imageKey") != null ? request.getParameter("imageKey") : "";
                String bookId = request.getParameter("booksId") != null ? request.getParameter("booksId") : "";
                if (!imageKey.equalsIgnoreCase("") && !bookId.equalsIgnoreCase("")) {
                    System.out.println("Inside");
                    BookDAO bdao = new BookDAO();
                    int result = bdao.deleteImageInBucket(Integer.parseInt(bookId), imageKey);
                    if (result == 1) {
                        statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("seller-success", "message", "Book Image deleted.");
                    } else {
                        mv = new ModelAndView("seller-error", "message", "Book not deleted");
                    }

                } else {
                    mv = new ModelAndView("seller-error", "message", "Book not deleted");
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println(e.getMessage());
        }
        return mv;
    }
}
