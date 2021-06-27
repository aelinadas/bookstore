<%-- 
    Document   : home
    Created on : May 27, 2020, 3:52:06 PM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <style>
            input[type="submit"] {
                color: #fff !important;
                text-transform: uppercase;
                text-decoration: none;
                background: #00b3b3;
                padding: 10px 20px;
                border: none;
            }
            a {
                color: #fff !important;
                text-transform: uppercase;
                text-decoration: none;
                background: #00b3b3;
                padding: 5px 10px;
                border: none;
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book Store</title>
    </head>
    <body>
        <h1 align="center">Book Store</h1>
        <form action="home.htm" method="POST">
            <p>Existing Users? Login as Buyer</p>
            <input type="submit" value="Login as Buyer" name="" >
            <input type="hidden" value="loginBuyer" name="action" />   
        </form>
        <br>
        <form action="home.htm" method="POST">
            <p>Existing Users? Login as Seller</p>
            <input type="submit" value="Login as Seller" name="" >
            <input type="hidden" value="loginSeller" name="action" />   
        </form>
        <br>
        <form action="home.htm" method="POST">
            <p>New Users?</p>
            <input type="submit" value="Sign Up" name="" >
            <input type="hidden" value="signupUser" name="action" />   
        </form>
        <br>
    </body>
</html>
