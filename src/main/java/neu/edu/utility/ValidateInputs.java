/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.utility;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aelinadas
 */
public class ValidateInputs {
    public boolean isName(String name) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+([\\s][a-zA-Z]+)*");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    
    public boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{1,63}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public boolean isPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,20}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    public boolean isISBN(String ISBN) {
        Pattern pattern = Pattern.compile("^(97(8|9))?\\d{9}(\\d|X)$");
        Matcher matcher = pattern.matcher(ISBN);
        return matcher.matches();
    }

    public boolean isDate(String date) {
        Pattern pattern = Pattern.compile("^(19|20)\\d\\d([- /.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
    
    public boolean isAuthors(String author) {
        Pattern pattern = Pattern.compile("[a-zA-Z\\s+]+(,[a-zA-Z\\s+]+)*");
        Matcher matcher = pattern.matcher(author);
        return matcher.matches();
    }

    public boolean isDecimal(String decimalNumber) {
        try {
            double deci = DecimalFormat.getNumberInstance().parse(decimalNumber).doubleValue();
            if (deci >= 0.01 && deci <= 9999.99) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isQuantity(String quantity) {
        try {
            int q = Integer.parseInt(quantity);
            if (q >= 0 && q <= 999) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
}
