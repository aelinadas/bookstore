<%-- 
    Document   : update
    Created on : May 27, 2020, 3:45:00 PM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <title>Update Account Information</title>
    </head>
    <body>
        <h1 align="center">Update Account Information</h1>
        <c:choose>
        <c:when test="${empty sessionScope.buyer}">
            <h2 align="center">Something Went wrong!!</h2>
        </c:when>
        <c:otherwise>
        <form action="buyer.htm" method="POST" onsubmit="return validateForm();">
            <label for="email">User Name:</label>
            <input id="email" type="email" name="email" value="${sessionScope.buyer.email}" pattern="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,63}$" placeholder="Email" readonly>
            <br/>
            <br/>
            <label for="fname">First Name:</label>
            <input id="name" type="text" name="fname" value="${sessionScope.buyer.fname}" pattern="[a-zA-Z]+([\s][a-zA-Z]+)*" placeholder="FirstName" required>
            <br/>
            <br/>
            <label for="lname">Last Name:</label>
            <input id="username" type="text" name="lname" value="${sessionScope.buyer.lname}" pattern="[a-zA-Z]+([\s][a-zA-Z]+)*" placeholder="LastName" required>
            <br/>
            <br/>        
            <input type="hidden" value="update" name="action"/>
            <input type="submit" value="Update Changes">
            <br>
            <br><a class="home" href="${pageContext.request.contextPath}/buyer.htm">Back</a>
            
        </form>
        <br/>
        <br/>
        <form action="home.htm" method="POST">
            <input type="hidden" value="logout" name="action" /> 
            <input type="submit" value="Logout" name="" >
        </form>
        </c:otherwise>
        </c:choose>
    </body>
</html>
