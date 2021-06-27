<%-- 
    Document   : update-book
    Created on : Jun 2, 2020, 6:09:20 PM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
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
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <title>ADD Book</title>
    </head>
    <body>
        <h1 align="center">Add Book</h1>
        <c:choose>
            <c:when test="${empty books}">
                <h2 align="center">No Book Available</h2>
            </c:when>
            <c:otherwise>
                <form action="book.htm" method="POST" onsubmit="return validateForm();">
                    <label for="isbn">ISBN:</label>
                    <input id="isbn" type="text" name="name" placeholder="ISBN" value="${books.ISBN}" pattern="^(97(8|9))?\\d{9}(\\d|X)$" required readonly>
                    <br/>
                    <br/>
                    <label for="title">Title:</label>
                    <input id="title" type="text" name="title" placeholder="Title" value="${books.title}" pattern="[a-zA-Z0-9]+([\s][a-zA-Z0-9]+)*" required readonly>
                    <br/>
                    <br/>
                    <label for="authors">Authors:</label>
                    <input id="authors" type="text" name="authors" placeholder="Authors" value="${books.authors}" pattern="[a-zA-Z\\s+]+(,[a-zA-Z\\s+]+)*" required readonly>
                    <br/>
                    <br/>
                    <label for="publicationDate">Publication Date</label>
                    <input id="publicationDate" type="date" name="publicationDate" value="${books.publicationDate}" placeholder="Publication Date" max="2020-06-06" required readonly>
                    <br/>
                    <br/>
                    <label for="quantity">Quantity:</label>
                    <input id="quantity" type="number" name="quantity" value="${books.quantity}" placeholder="Quantity" min="1" required readonly>
                    <br/>
                    <br/>
                    <label for="price">Price:</label>
                    <input id="price" type="number" name="price" placeholder="Price" value="${books.price}" min="0.01" max="9999.99" step="0.01" required>
                    <br/>
                    <br/>
                    <label for="requiredQuantity">Order Quantity:</label>
                    <input type="number" name="requiredQuantity" value="1" placeholder="Number Of Books" min="1" max="${books.quantity}" required>
                    <br/>
                    <br/>
                    <input type="hidden" value="addToCart" name="action">
                    <input name="id" value="${books.id}" type="hidden">
                    <p id="submission"><input type="submit" value="Add to Cart"></p>
                </form>
                <center><h3>Book Images</h3></center>
                <div class="row">
                    <c:forEach items="${requestScope.imagesList}" var="imageLocation">
                        <div class="col-md-4">
                            <div class="thumbnail">
                                <div class="column">
                                    <img id="imageid" src="<c:out value="${imageLocation}"/>" alt="book" style="width:100%">
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <form id="form_login" action="book.htm" method="post">
                    <input type="hidden" name="action" value="shopBag">
                    <p id="submission"><input type="submit" value="VIEW My Cart"></p>
                </form>
            </c:otherwise>
        </c:choose>
        <a class="home" href="${pageContext.request.contextPath}/buyer.htm">Buyer Home</a>
    </body>
</html>
<script>
    for(var i =0; i<document.images.length; i++){
        var x = document.images[i]
        x.src = x.src.replace('https', 'http');
    }
</script>
