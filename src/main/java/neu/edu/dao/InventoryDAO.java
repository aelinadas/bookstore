/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import neu.edu.pojo.Book;
import neu.edu.pojo.BooksInCart;
import neu.edu.pojo.Buyer;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 *
 * @author aelinadas
 */
public class InventoryDAO {
    
    public ArrayList<Book> getBooks(Buyer buyer) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<Book> bookList = new ArrayList<Book>();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("FROM Book");
            ArrayList<Book> tempList = (ArrayList<Book>) query.list();
            connectionDAO.commit();
            for (Book book : tempList) {
                if (!book.getSeller().getEmail().equalsIgnoreCase(buyer.getEmail())) {
                    bookList.add(book);
                }
            }
            Collections.sort(bookList, new Comparator<Book>() {
                public int compare(Book b1, Book b2) {
                    int q1 = b1.getQuantity();
                    int q2 = b2.getQuantity();
                    return q1 - q2;
                }

            });
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return bookList;
    }

    public ArrayList<BooksInCart> getUnOrderedBooks(Buyer buyer) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<BooksInCart> bookList = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("FROM BooksInCart WHERE buyerID= :id");
            query.setInteger("id", buyer.getId());
            bookList = (ArrayList<BooksInCart>) query.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return bookList;
    }

    public int deleteBook(int id) {
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from BooksInCart where bookID = :id");
            query.setInteger("id", id);
            BooksInCart booksInCart = (BooksInCart) query.uniqueResult();
            if (booksInCart != null) {
                connectionDAO.getSession().delete(booksInCart);
                connectionDAO.commit();
                result = 1;
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            e.printStackTrace();

        } finally {
            connectionDAO.getSession().flush();
            connectionDAO.getSession().clear();
            connectionDAO.close();
        }
        return result;
    }
    
}
