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
        <title>Update Book</title>
    </head>
    <body>
        <h1 align="center">Update Book</h1>
        <c:choose>
            <c:when test="${empty books}">
                <h2 align="center">Something Went wrong!!</h2>
            </c:when>
            <c:otherwise>
                <form action="stock.htm" method="POST" onsubmit="return validateForm();">
                    <label for="isbn">ISBN:</label>
                    <input id="isbn" type="text" name="name" placeholder="ISBN" value="${books.ISBN}" pattern="^(97(8|9))?\\d{9}(\\d|X)$" required readonly>
                    <br/>
                    <br/>
                    <label for="title">Title:</label>
                    <input id="title" type="text" name="title" placeholder="Title" value="${books.title}" pattern="[a-zA-Z0-9]+([\s][a-zA-Z0-9]+)*" required>
                    <br/>
                    <br/>
                    <label for="authors">Authors:</label>
                    <input id="authors" type="text" name="authors" placeholder="Authors" value="${books.authors}" pattern="[a-zA-Z\\s+]+(,[a-zA-Z\\s+]+)*" required>
                    <br/>
                    <br/>
                    <label for="publicationDate">Publication Date</label>
                    <input id="publicationDate" type="date" name="publicationDate" value="${books.publicationDate}" placeholder="Publication Date" max="2020-06-06" required>
                    <br/>
                    <br/>
                    <label for="quantity">Quantity:</label>
                    <input id="quantity" type="number" name="quantity" value="${books.quantity}" placeholder="Quantity" min="1" required>
                    <br/>
                    <br/>
                    <label for="price">Price:</label>
                    <input id="price" type="number" name="price" placeholder="Price" value="${books.price}" min="0.01" max="9999.99" step="0.01" required>
                    <br/>
                    <br/>
                    <label for="seller">Seller:</label>
                    <input id="seller" type="text" name="seller" value="${sessionScope.seller.fname}" placeholder="Seller Name" pattern="[a-zA-Z]+([\s][a-zA-Z]+)*" required readonly>
                    <br/>
                    <br/>
                    <input type="hidden" value="updateBook" name="action">
                    <input name="id" value="${books.id}" type="hidden">
                    <p id="submission"><input type="submit" value="Update Book"></p>
                    <br/>
                    <br/>
                </form>

                <form action="image.htm" method="post" enctype="multipart/form-data">
                    <label>Upload Images</label>
                    <input type="file" name="image" accept="image/*" required>
                    <br/>
                    <br/>
                    <input name="bookID" type="hidden" value="${books.id}">
                    <input name="isbn" type="hidden" value="${books.ISBN}">
                    <input name="action" value="addMoreImg" type="hidden">
                    <p id="submission"><input type="submit" value="Add Image"></p>
                </form>
            <center><h3>Book Images</h3></center>
            <div class="row">
                <c:forEach items="${requestScope.imgMap}" var="entry">
                    <div class="col-md-4">
                        <div class="thumbnail">
                            <img src="<c:out value="${entry.value}"/>" alt="book" style="width:100%" id="imageid">
                            <div class="caption">
                                <a class="home" href="stock.htm?action=deleteImage&booksId=${books.id}&imageKey=<c:out value="${entry.key}"/>">Delete</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
    <a class="home" href="${pageContext.request.contextPath}/seller.htm">Seller Home</a>
</body>
</html>
<script>
    for(var i =0; i<document.images.length; i++){
        var x = document.images[i]
        x.src = x.src.replace('https', 'http');
    }
</script>