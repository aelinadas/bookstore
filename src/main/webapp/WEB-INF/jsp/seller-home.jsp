<%-- 
    Document   : seller-home
    Created on : Jun 3, 2020, 2:52:54 AM
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
        <title>Seller Home</title>
    </head>
    <body>
        <h1 align="center">Welcome Seller ${sessionScope.seller.fname}</h1>
        <form action="seller.htm" method="POST">
            <input type="submit" value="View Profile" name="" >
            <input type="hidden" value="viewSeller" name="action" />   
        </form>
        <br>
        <br>
        <form action="seller.htm" method="POST">
            <input type="submit" value="Update Information" name="" >
            <input type="hidden" value="updateSeller" name="action" />   
        </form>
        <br>
        <br>
        <form action="seller.htm" method="POST">
            <input type="hidden" value="updatePassword" name="action" /> 
            <input type="submit" value="Update Password" name="" >
        </form>
        <br>
        <br>
        <form action="stock.htm" method="POST">
            <input type="submit" value="Add Book" name="" >
            <input type="hidden" value="addBook" name="action" />
        </form>
        <br>
        <br>
        <form action="stock.htm" method="POST">
            <input type="submit" value="View Books" name="" >
            <input type="hidden" value="viewbook" name="action" />
        </form>
        <br>
        <br>
        <form action="home.htm" method="POST">
            <input type="hidden" value="logout" name="action" /> 
            <input type="submit" value="Logout" name="" >
        </form>
    </body>
</html>