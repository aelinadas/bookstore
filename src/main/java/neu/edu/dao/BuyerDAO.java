/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;
import java.util.ArrayList;
import java.util.List;
import neu.edu.pojo.Buyer;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author aelinadas
 */
public class BuyerDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    
    public List<Buyer> getBuyers() {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        List<Buyer> buyers = new ArrayList<Buyer>();
        try {
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Buyer");
            buyers = q.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally {
            connectionDAO.close();
        }
        return buyers;
    }
    
    public Buyer getBuyerById(int buyerId) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        Buyer buyer = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Buyer where id= :id");
            query.setInteger("id", buyerId);
            buyer = (Buyer) query.uniqueResult();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return buyer;
    }
    
    public int addNewBuyer(String fname, String lname, String email, String password){
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try{
            Buyer buyer = new Buyer();
            buyer.setFname(fname);
            buyer.setLname(lname);
            buyer.setEmail(email);
            buyer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            connectionDAO.beginTransaction();
            connectionDAO.getSession().save(buyer);
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
    
    public int updateBuyer(Buyer buyer, String fname, String lname){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int buyerId = buyer.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Buyer where id= :id");
            q.setInteger("id", buyerId);
            buyer = (Buyer) q.uniqueResult();
            if (buyer != null) {
                buyer.setFname(fname);
                buyer.setLname(lname);
                connectionDAO.getSession().update(buyer);
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
    
    public int updatePassword(Buyer buyer, String newpassword, String confirmpassword){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int buyerId = buyer.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from Buyer where id= :id");
            q.setInteger("id", buyerId);
            buyer = (Buyer) q.uniqueResult();
            if (buyer != null) {
                buyer.setPassword(BCrypt.hashpw(newpassword, BCrypt.gensalt()));
                connectionDAO.getSession().update(buyer);
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
    
    private boolean checkPassword(String plainPassword, String hashedPassword) {
        boolean valid = false;
        if (BCrypt.checkpw(plainPassword, hashedPassword)) {
            valid = true;
        }
        return valid;
    }
    
    public Buyer authenticateBuyer(String email, String password) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        Buyer buyer = null;
        Boolean valid = false;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from Buyer where email = :id");
            query.setString("id", email);
            buyer = (Buyer) query.uniqueResult();
            connectionDAO.commit();
            if (buyer != null) {
                valid = checkPassword(password, buyer.getPassword());
                if (!valid) {
                    buyer = null;
                }
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return buyer;
    }
    
}
