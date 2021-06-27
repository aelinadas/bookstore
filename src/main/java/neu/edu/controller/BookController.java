/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import neu.edu.applicationstats.Statistic;
import neu.edu.dao.BookDAO;
import neu.edu.dao.InventoryDAO;
import neu.edu.pojo.Book;
import neu.edu.pojo.BooksInCart;
import neu.edu.pojo.Buyer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author aelinadas
 */
public class BookController extends AbstractController {

    public BookController() {
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
        String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        Buyer buyer = (Buyer) request.getSession(false).getAttribute("buyer");
        if (action.equalsIgnoreCase("shopBooks") && buyer != null) {
            BookDAO bookDAO = new BookDAO();
            ArrayList<Book> cartItems = bookDAO.getBooksByBuyer(buyer);
            if (cartItems != null) {
                Collections.sort(cartItems, new Comparator<Book>() {
                    public int compare(Book a, Book b) {
                        int bQuantity = b.getQuantity();
                        int aQuantity = a.getQuantity();
                        return aQuantity - bQuantity;
                    }
                });
                statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                modelAndView = new ModelAndView("add-to-cart", "cartItems", cartItems);
            }
        }
        if (action.equalsIgnoreCase("moreInfo") && buyer != null) {
            String bookId = request.getParameter("id") != null ? request.getParameter("id") : "";
            if (!bookId.equalsIgnoreCase("")) {
                BookDAO bdao = new BookDAO();
                Book book = bdao.getBookBasedOnID(Integer.parseInt(bookId));
                if (book != null) {
                    ArrayList<String> imagesList = bdao.getImageUrlsWithISBN(book.getISBN());
                    request.setAttribute("imagesList", imagesList);
                    statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                    statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);
                    statistic.getInstance().incrementCounter("BookCounter "+book.getTitle());

                    modelAndView = new ModelAndView("decide-book", "books", book);
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Book does not exist");
                }

            }
        }
        if (action.equalsIgnoreCase("shopBag")) {
            InventoryDAO inventoryDAO = new InventoryDAO();
            ArrayList<BooksInCart> currentList = inventoryDAO.getUnOrderedBooks(buyer);
            BookDAO bdao = new BookDAO();
            ArrayList<Book> checkList = bdao.getAllBooks();
            String cartUpdateMsg = "";
            ArrayList<BooksInCart> cartList = new ArrayList<BooksInCart>();
            for (BooksInCart booksInCart : currentList) {
                boolean bookNotAvailable = true;
                for (Book book : checkList) {
                    if (booksInCart.getBookID() == book.getId()) {
                        bookNotAvailable = false;
                        break;
                    }
                }
                if (!bookNotAvailable) {
                    cartList.add(booksInCart);
                } else {
                    cartUpdateMsg = cartUpdateMsg + " " + booksInCart.getTitle() + " is no more available for purchase";
                    inventoryDAO.deleteBook(booksInCart.getBookID());
                }
            }
            request.setAttribute("msg", cartUpdateMsg);
            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

            modelAndView = new ModelAndView("view-cart", "cartList", cartList);
        }

        if (action.equalsIgnoreCase("addToCart")) {
            String id = request.getParameter("id") != null ? request.getParameter("id") : "";
            String requiredQuantity = request.getParameter("requiredQuantity") != null ? request.getParameter("requiredQuantity") : "";
            if (!id.equalsIgnoreCase("") && !requiredQuantity.equalsIgnoreCase("")) {
                BookDAO bookDAO = new BookDAO();
                Book book = bookDAO.getBookBasedOnID(Integer.parseInt(id));
                BooksInCart booksInCart = new BooksInCart();
                booksInCart.setBuyerID(buyer.getId());
                booksInCart.setBookID(book.getId());
                booksInCart.setTitle(book.getTitle());
                booksInCart.setPrice(book.getPrice());
                booksInCart.setSeller(book.getSeller());
                booksInCart.setQuantity(Integer.parseInt(requiredQuantity));
                booksInCart.setSellerID(book.getSeller().getId());
                BookDAO bdao = new BookDAO();
                int result = bdao.addToCart(booksInCart);
                if (result == 1) {
                    ArrayList<Book> requestList = bdao.getBooksByBuyer(buyer);
                    statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                    statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                    modelAndView = new ModelAndView("add-to-cart", "cartItems", requestList);
                } else {
                    modelAndView = new ModelAndView("buyer-error", "message", "Book you requested for does not exist anymore");
                }
            } else {
                modelAndView = new ModelAndView("buyer-error", "message", "Book you requested for does not exist");
            }
        }
        return modelAndView;
    }

}
