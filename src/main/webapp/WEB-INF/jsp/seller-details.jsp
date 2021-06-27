<%-- 
    Document   : seller-details
    Created on : Jun 3, 2020, 2:55:00 PM
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
        <title>View Information</title>
    </head>
    <body>
        <h1 align="center">Welcome Seller ${sessionScope.seller.fname}</h1>
        <h2 align="center">Your Profile</h2>
        <label>Your Full Name </label>${sessionScope.seller.fname} ${sessionScope.seller.lname}
        <br/>
        <label>Your UserName </label>${sessionScope.seller.email}
        <br/>
        <br/>
        <br><a href="${pageContext.request.contextPath}/seller.htm">Back</a>
        <br/>
        <br/>
        <form action="home.htm" method="POST">
            <input type="hidden" value="logout" name="action" /> 
            <input type="submit" value="Logout" name="" >
        </form>
    </body>
</html>
