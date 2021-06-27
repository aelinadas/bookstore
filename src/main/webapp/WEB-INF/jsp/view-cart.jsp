<%-- 
    Document   : view-cart
    Created on : Jun 4, 2020, 4:52:54 AM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <style>
            #uicolor {
                border-collapse: collapse;
                width: 100%;
                align-content: center;
            }
            #uicolor td, #customers th {
                border: 1px solid #ddd;
                padding: 8px;
            }
            #uicolor tr:nth-child(even){background-color: #F2F2F2;}
            #uicolor tr:hover {background-color: #ddd;}
            #uicolor th {
                padding-top: 12px;
                padding-bottom: 12px;
                text-align: left;
                background-color: #00b3b3;
                color: white;
            }
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
                width: 20px;
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Cart</title>
    </head>
    <body>
        <h1 align="center">View Cart</h1>
        <h2 align="center">${requestScope.msg}</h2>
        <c:choose>
            <c:when test="${empty cartList}">
                <h2 align="center">No books available in your cart currently.</h2>
                <br>
                <br>
                <a class="home" href="${pageContext.request.contextPath}/buyer.htm">Buyer Home</a>
            </c:when>
            <c:otherwise>
                <form action="order.htm" method="POST">
                    <table id="uicolor">
                        <tr>
                            <th>Title</th>
                            <th>Quantity</th>
                            <th>Price</th>
                        </tr>
                        <c:forEach var="cartItem" items="${cartList}">
                            <tr id="${cartItem.getBookInCartID()}">
                                <td><c:out value="${cartItem.getTitle()}" /></td>
                                <td><c:out value="${cartItem.getQuantity()}" /></td>
                                <td><c:out value="${cartItem.getPrice()}" /></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                    <br>
                    <a href="${pageContext.request.contextPath}/buyer.htm">Buyer Home</a>
                    <br>
                    <br>
                    <a class="home" href="${pageContext.request.contextPath}/home.htm">Logout</a>
                </form>
            </c:otherwise>
        </c:choose>
    </body>
</html>

