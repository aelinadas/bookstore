/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;

import java.util.ArrayList;
import java.util.List;
import neu.edu.pojo.User;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;
/**
 *
 * @author aelinadas
 */
public class UserDAO {
    
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    
    public List<User> getUsers() {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        List<User> consumers = new ArrayList<User>();
        try {
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from User");
            consumers = q.list();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally {
            connectionDAO.close();
        }
        return consumers;
    }
    public User getUserById(int userId) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        User user = null;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from User where id= :id");
            query.setInteger("id", userId);
            user = (User) query.uniqueResult();
            connectionDAO.commit();
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return user;
    }
    public int addNewUser(String fname, String lname, String email, String password){
        int result = 0;
        ConnectionDAO connectionDAO = new ConnectionDAO();
        try{
            User user = new User();
            user.setFname(fname);
            user.setLname(lname);
            user.setEmail(email);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            connectionDAO.beginTransaction();
            connectionDAO.getSession().save(user);
            connectionDAO.commit();
            result = 1;
            
        }catch (HibernateException e) {
            logger.error(e);
            e.printStackTrace();
            connectionDAO.rollbackTransaction();
        } finally {
            connectionDAO.close();
        }
        return result;
    }
    public int updateUser(User user, String fname, String lname){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int id = user.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from User where id= :id");
            q.setInteger("id", id);
            user = (User) q.uniqueResult();
            if (user != null) {
                user.setFname(fname);
                user.setLname(lname);
                connectionDAO.getSession().update(user);
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
    
    public int updatePassword(User user, String newpassword, String confirmpassword){
        ConnectionDAO connectionDAO = new ConnectionDAO();
        int result = 0;
        int id = user.getId();
        try{
            connectionDAO.beginTransaction();
            Query q = connectionDAO.getSession().createQuery("from User where id= :id");
            q.setInteger("id", id);
            user = (User) q.uniqueResult();
            if (user != null) {
                user.setPassword(BCrypt.hashpw(newpassword, BCrypt.gensalt()));
                connectionDAO.getSession().update(user);
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
    
    public User authenticateUser(String email, String password) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        User user = null;
        Boolean valid = false;
        try {
            connectionDAO.beginTransaction();
            Query query = null;
            query = connectionDAO.getSession().createQuery("from User where email = :id");
            query.setString("id", email);
            user = (User) query.uniqueResult();
            connectionDAO.commit();
            if (user != null) {
                valid = checkPassword(password, user.getPassword());
                if (!valid) {
                    user = null;
                }
            }
        } catch (HibernateException e) {
            logger.error(e);
            connectionDAO.rollbackTransaction();
            System.out.println(e.getMessage());
        } finally {
            connectionDAO.close();
        }
        return user;
    }
    
}
