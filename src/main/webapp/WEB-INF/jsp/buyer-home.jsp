<%-- 
    Document   : buyer-home
    Created on : Jun 3, 2020, 2:53:53 AM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    response.setHeader("pragma","no-cache");
    response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
    response.setHeader("Expires", "0");
%>
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
        <title>Buyer Home</title>
    </head>
    <body>
        <h1 align="center">Welcome Buyer ${sessionScope.buyer.fname}</h1>
        <form action="buyer.htm" method="POST">
            <input type="submit" value="View Profile" name="" >
            <input type="hidden" value="viewBuyer" name="action" />   
        </form>
        <br>
        <br>
        <form action="buyer.htm" method="POST">
            <input type="submit" value="Update Information" name="" >
            <input type="hidden" value="updateBuyer" name="action" />   
        </form>
        <br>
        <br>
        <form action="buyer.htm" method="POST">
            <input type="hidden" value="updatePassword" name="action" /> 
            <input type="submit" value="Update Password" name="" >
        </form>
        <br>
        <br>
        <form action="book.htm" method="POST">
            <input type="hidden" value="shopBooks" name="action" /> 
            <input type="submit" value="Shop Books" name="" >
        </form>
        <br>
        <br>
        <form action="book.htm" method="POST">
            <input type="hidden" value="shopBag" name="action" /> 
            <input type="submit" value="My Shopping Bag" name="" >
        </form>
        <br>
        <br>
        <form action="home.htm" method="POST">
            <input type="hidden" value="logout" name="action" /> 
            <input type="submit" value="Logout" name="" >
        </form>
    </body>
</html>
