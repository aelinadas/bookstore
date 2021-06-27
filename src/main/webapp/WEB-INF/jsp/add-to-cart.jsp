<%-- 
    Document   : add-to-cart
    Created on : Jun 4, 2020, 5:11:30 AM
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
                padding: 10px 40px;
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
        <title>Available Books</title>
    </head>
    <body>
        <h1 align="center">Available Books</h1>
        <c:choose>
            <c:when test="${empty cartItems}">
                <h2 align="center">Currently, no books are available. Sorry</h2>
            </c:when>
            <c:otherwise>
                <form action="book.htm" method="POST">
                    <table id="uicolor">
                        <tr>
                            <th>ISBN</th>
                            <th>Published Date</th>
                            <th>Title</th>
                            <th>Authors</th>
                            <th>Quantity Available</th>
                            <th>Seller</th>
                            <th>Price</th>
                        </tr>
                      <c:forEach var="cartItem" items="${cartItems}">
                            <c:if test="${cartItem.quantity>0}">
                                <tr id="${cartItem.id}">
                                    <td><c:out value="${cartItem.ISBN}"/></td>
                                    <td><c:out value="${cartItem.publicationDate}"/></td>
                                    <td><c:out value="${cartItem.title}"/></td>
                                    <td><c:out value="${cartItem.authors}"/></td>
                                    <td><c:out value="${cartItem.quantity}"/></td>
                                    <td><c:out value="${cartItem.seller.fname}"/></td>
                                    <td><c:out value="${cartItem.price}"/></td>
                                    <td><a href="book.htm?action=moreInfo&id=${cartItem.id}">DETAILS</a>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>
                    <br>
<!--                    <input type="hidden" name="action" value="myCart">
                    <input type="hidden" name="productIds" value="">
                    <input type="submit" value="My Cart">-->
                </form>
            </c:otherwise>
        </c:choose>
        <br>
        <a class="home" href="${pageContext.request.contextPath}/buyer.htm">Home</a>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
                    var orderItems = [];
                    var ordered = false;

                    $('input[type=submit]').on('click', function () {
                        if (orderItems.length > 0) {
                            $("input[name='productIds']").val(orderItems);
                            ordered = true;
                        } else {
                            ordered = false;
                        }

                    });

                    function orderedSomething() {
                        if (ordered) {
                            return true;
                        } else {
                            alert("Your cart is empty");
                            return false;
                        }
                    }

                    $('p').click(function () {
                        $(this).closest('tr').find("input").each(function () {
                            var productId = $(this).closest('tr').attr('id');
                            var quantity = this.value;
                            orderItems.push(productId);
                            orderItems.push(quantity);
                            $(this).closest('tr').hide();
                        });
                    });
    </script>
</html>
