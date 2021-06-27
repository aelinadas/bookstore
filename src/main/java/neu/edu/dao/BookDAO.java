/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import neu.edu.pojo.Book;
import neu.edu.pojo.BooksInCart;
import neu.edu.pojo.Buyer;
import neu.edu.pojo.Seller;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author aelinadas
 */
public class BookDAO {

    private static final Logger logger = Logger.getLogger(BookDAO.class);
    private AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    String configPath = "/home/ubuntu/applicationConfig.properties";

    public ArrayList<Book> getAllBookList(Seller seller) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<Book> bookList = null;
        try {
            Properties properties = new Properties();
            InputStream inputStream;
            inputStream = new FileInputStream(configPath);
            properties.load(inputStream);
            inputStream.close();
            String dbName = properties.getProperty("dbName");

            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createSQLQuery("Select * from " + dbName + ".book where sellerID = :id").addEntity(Book.class);
            query.setInteger("id", seller.getId());
            bookList = (ArrayList<Book>) query.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            connectionDAO.close();
        }
        return bookList;
    }

    public int addBook(String ISBN, String title, String authors, Date publicationDate, int quantity, double price, Seller seller, String images) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        try {
            if (seller != null) {
                connectionDAO.beginTransaction();
                Book book = new Book();
                book.setISBN(ISBN);
                book.setTitle(title);
                book.setAuthors(authors);
                book.setPublicationDate(publicationDate);
                book.setQuantity(quantity);
                book.setPrice(price);
                long millis = System.currentTimeMillis();
                java.sql.Timestamp dateTime = new java.sql.Timestamp(millis);
                book.setAddDate(dateTime);
                book.setUpdateDate(dateTime);
                book.setSeller(seller);
                book.setImages(images);
                seller.getBookList().add(book);
                result = (int) connectionDAO.getSession().save(book);
                connectionDAO.commit();
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            //System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public Book getBookBasedOnID(int bookid) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        Book book = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where id = :id");
            query.setInteger("id", bookid);
            book = (Book) query.uniqueResult();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return book;
    }

    public int updateBook(int id, String title, String authors, Date publicationDate, int quantity, double price, Seller seller) {
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where id = :id");
            query.setInteger("id", id);
            Book book = (Book) query.uniqueResult();
            if (book == null) {
                return result;
            } else {
                book.setTitle(title);
                book.setAuthors(authors);
                book.setPublicationDate(publicationDate);
                book.setQuantity(quantity);
                book.setPrice(price);
                book.setSeller(seller);
                connectionDAO.getSession().update(book);
                connectionDAO.commit();
                result = 1;
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public int deleteBook(int id) {
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where id = :id");
            query.setInteger("id", id);
            Book book = (Book) query.uniqueResult();
            if (book == null) {
                return result;
            } else {
                connectionDAO.getSession().delete(book);
                connectionDAO.commit();
                deleteImageInBucket(id);
                result = 1;
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public boolean isUnique(String ISBN) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        boolean exist = false;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where ISBN = :id");
            query.setString("id", ISBN);
            ArrayList<Book> bookList = (ArrayList<Book>) query.list();
            connectionDAO.commit();
            if (!bookList.isEmpty()) {
                exist = true;
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return exist;
    }

    public ArrayList<Book> getBooksByBuyer(Buyer buyer) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<Book> bookList = new ArrayList<Book>();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("From Book");
            ArrayList<Book> tl = (ArrayList<Book>) query.list();
            connectionDAO.commit();
            for (Book book : tl) {
                if (!book.getSeller().getEmail().equalsIgnoreCase(buyer.getEmail())) {
                    bookList.add(book);
                }
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return bookList;
    }

    public ArrayList<Book> getAllBooks() {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<Book> bookList = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("From Book");
            bookList = (ArrayList<Book>) query.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return bookList;
    }

    public ArrayList<BooksInCart> getBooksInCart(Buyer buyer) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ArrayList<BooksInCart> booksInCarts = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("From BooksInCart where buyerID= :id");
            query.setInteger("id", buyer.getId());
            booksInCarts = (ArrayList<BooksInCart>) query.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return booksInCarts;
    }

    public int addToCart(ArrayList<BooksInCart> requestList) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        try {
            if (requestList != null) {
                for (BooksInCart booksInCart : requestList) {
                    connectionDAO.beginTransaction();
                    connectionDAO.getSession().save(booksInCart);
                    connectionDAO.commit();
                    result = 1;
                }
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public String getBucketName() {
        String bucketName = "";
        try {
            Properties properties = new Properties();
            InputStream inputStream;
            inputStream = new FileInputStream(configPath);
            properties.load(inputStream);
            inputStream.close();
            bucketName = properties.getProperty("bucketName");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return bucketName;
    }

    public Map<String, String> getImageUrlsWithIDISBN(int bookId, String isbn) {
        Map<String, String> imagesMap = new HashMap<String, String>();
        String bucketName = getBucketName();
        ObjectListing objectListing = s3client.listObjects(bucketName);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            if (os.getKey().contains(isbn + "-" + bookId)) {
                imagesMap.put(os.getKey(), s3client.getUrl(bucketName, os.getKey()).toExternalForm());
            }
        }

        return imagesMap;
    }

    public ArrayList<String> getImageUrlsWithISBN(String isbn) {
        ArrayList<String> imagesList = new ArrayList<String>();
        String bucketName = getBucketName();
        ObjectListing objectListing = s3client.listObjects(bucketName);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            if (os.getKey().contains(isbn + "-")) {
                imagesList.add(s3client.getUrl(bucketName, os.getKey()).toExternalForm());
            }
        }

        return imagesList;
    }

    public int deleteImageInBucket(int bookId, String imageKey) {
        System.out.println("Inside2");
        int result = 0;
        String bucketName = getBucketName();
        ObjectMetadata objectMetadata = s3client.getObjectMetadata(bucketName, imageKey);
        String fileName = objectMetadata.getUserMetaDataOf("x-amz-meta-originalfilename");
        int resultA = deleteImage(bookId, fileName);
        if (resultA == 1) {
            s3client.deleteObject(bucketName, imageKey);
            result = 1;
        }
        return result;
    }

    public int updateImage(int bookId, String bookName) {
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where id = :id");
            query.setInteger("id", bookId);
            Book book = (Book) query.uniqueResult();
            if (book != null) {
                String imageNames = book.getImages();
                if (!imageNames.equalsIgnoreCase("")) {
                    book.setImages(imageNames + "," + bookName);
                } else {
                    book.setImages(imageNames + bookName);
                }
                book.setImages(imageNames);
                long millis = System.currentTimeMillis();
                java.sql.Timestamp dateTime = new java.sql.Timestamp(millis);
                book.setUpdateDate(dateTime);
                connectionDAO.getSession().update(book);
                connectionDAO.commit();
                result = 1;
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            e.printStackTrace();
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public void deleteImageInBucket(int bookId) {

        ArrayList<String> imagesList = new ArrayList<String>();
        String bucketName = getBucketName();
        ObjectListing objectListing = s3client.listObjects(bucketName);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            if (os.getKey().contains("-" + bookId + "-")) {
                s3client.deleteObject(bucketName, os.getKey());
            }
        }
    }

    public int deleteImage(int bookId, String bookName) {
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Book where id = :id");
            query.setInteger("id", bookId);
            Book book = (Book) query.uniqueResult();
            if (book != null) {
                String imageNames = book.getImages();
                if (!imageNames.equalsIgnoreCase("")) {
                    ArrayList<String> oldNamesList = new ArrayList(Arrays.asList(imageNames.split(",")));
                    Iterator<String> itr = oldNamesList.iterator();
                    String newImages = "";
                    while (itr.hasNext()) {
                        String image = itr.next();
                        if (image.equals(bookName)) {
                            itr.remove();
                        }
                    }
                    if (!oldNamesList.isEmpty()) {
                        for (int i = 0; i < oldNamesList.size(); i++) {
                            if (i != 0) {
                                newImages = newImages + "," + oldNamesList.get(i);
                            } else {
                                newImages = newImages + oldNamesList.get(i);

                            }
                        }
                    }
                    book.setImages(newImages);
                }
                long millis = System.currentTimeMillis();
                java.sql.Timestamp dateTime = new java.sql.Timestamp(millis);
                book.setUpdateDate(dateTime);
                connectionDAO.getSession().update(book);
                connectionDAO.commit();
                result = 1;
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            e.printStackTrace();
        } finally {
            connectionDAO.close();
        }
        return result;
    }

    public int addToCart(BooksInCart booksInCart) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        try {
            if (booksInCart != null) {
                connectionDAO.beginTransaction();
                connectionDAO.getSession().save(booksInCart);
                connectionDAO.commit();
                result = 1;
            }
        } catch (HibernateException e) {
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return result;
    }
}
