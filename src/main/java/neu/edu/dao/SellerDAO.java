/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;

import java.util.ArrayList;
import java.util.List;
import neu.edu.pojo.Seller;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author aelinadas
 */
public class SellerDAO {
   private static final Logger logger = Logger.getLogger(BuyerDAO.class);
   
       public List<Seller> getSellers() {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        List<Seller> sellers = new ArrayList<Seller>();
        try {
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Seller");
            sellers = q.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally {
            connectionDAO.close();
        }
        return sellers;
    }
    public Seller getSellerById(int sellerId) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        Seller seller = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Seller where id= :id");
            query.setInteger("id", sellerId);
            seller = (Seller) query.uniqueResult();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return seller;
    }
    
    private boolean checkPassword(String plainPassword, String hashedPassword) {
        boolean valid = false;
        if (BCrypt.checkpw(plainPassword, hashedPassword)) {
            valid = true;
        }
        return valid;
    }
    
    public boolean isUnique(String email) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        boolean exist = false;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Seller where email = :id");
            query.setString("id", email);
            ArrayList<Seller> consumerList = (ArrayList<Seller>) query.list();
            connectionDAO.commit();
            if (!consumerList.isEmpty()) {
                exist = true;
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return exist;
    }
    
    public int addNewSeller(String fname, String lname, String email, String password){
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try{
            Seller seller = new Seller();
            seller.setFname(fname);
            seller.setLname(lname);
            seller.setEmail(email);
            seller.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            connectionDAO.beginTransaction();
            connectionDAO.getSession().save(seller);
            connectionDAO.commit();
            result = 1;
        }catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
        } finally {
            connectionDAO.close();
        }
        return result;
    }
    
    public int updateSeller(Seller seller, String fname, String lname){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int id = seller.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Seller where id= :id");
            q.setInteger("id", id);
            seller = (Seller) q.uniqueResult();
            if (seller != null) {
                seller.setFname(fname);
                seller.setLname(lname);
                connectionDAO.getSession().update(seller);
                connectionDAO.commit();
                result = 1;
            }
        } catch(HibernateException e){
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally{
            connectionDAO.close();
        }
        return result;
    }
    
    public int updatePassword(Seller seller, String newpassword, String confirmpassword){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int sellerId = seller.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Seller where id= :id");
            q.setInteger("id", sellerId);
            seller = (Seller) q.uniqueResult();
            if (seller != null) {
                seller.setPassword(BCrypt.hashpw(newpassword, BCrypt.gensalt()));
                connectionDAO.getSession().update(seller);
                connectionDAO.commit();
                result = 1;
            }
        } catch(HibernateException e){
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally{
            connectionDAO.close();
        }
        return result;
    }
    
    public Seller authenticateSeller(String email, String password) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        Seller seller = null;
        Boolean valid = false;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Seller where email = :id");
            query.setString("id", email);
            seller = (Seller) query.uniqueResult();
            connectionDAO.commit();
            if (seller != null) {
                valid = checkPassword(password, seller.getPassword());
                if (!valid) {
                    seller = null;
                }
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return seller;
    }
    
    public boolean validUser(String email) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        boolean valid = true;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Seller where email = :id");
            query.setString("id", email);
            ArrayList<Seller> consumerList = (ArrayList<Seller>) query.list();
            connectionDAO.commit();
            if (consumerList.isEmpty()) {
                valid = false;
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return valid;
    }
}
