<%-- 
    Document   : view-all-books
    Created on : Jun 2, 2020, 6:09:51 PM
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
                width: 75px;
            }
            input[type="button"] {
                color: #fff !important;
                text-transform: uppercase;
                text-decoration: none;
                background: #00b3b3;
                padding: 10px 20px;
                border: none;
                width: 75px;
            }
            .delete{
                background-color:red;
                font-weight:bold;
                color:#ffffff;
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
        <title>View All Books</title>
    </head>
    <body>
        <h1 align="center">View All Books</h1>
        <c:choose>
            <c:when test="${empty books}">
                <h2 align="center">No book available</h2>
            </c:when>
            <c:otherwise>
                <form method="POST" name="form">
                <table id="uicolor">
                    <tr>
                        <th>ISBN</th>
                        <th>Title</th>
                        <th>Authors</th>
                        <th>Publication Date</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Seller</th>
                        <th>Modify</th>
                    </tr>
                    <c:forEach var="book" items="${books}">
                        <tr>
                            <td><c:out value="${book.getISBN()}" /></td>
                            <td><c:out value="${book.getTitle()}" /></td>
                            <td><c:out value="${book.getAuthors()}" /></td>
                            <td><c:out value="${book.getPublicationDate()}" /></td>
                            <td><c:out value="${book.getQuantity()}" /></td>
                            <td><c:out value="${book.getPrice()}" /></td>
                            <td><c:out value="${book.getSeller().getFname()}" /></td>
                            <td>
                              <a href="stock.htm?action=update&id=${book.id}">Update</a><br><br><input type="button" name="delete" value="Delete" onclick="deleteBook(${book.id});">
                            </td>
                        </tr>
                    </c:forEach> 
                </table>
                    </form>
            </c:otherwise>
        </c:choose>
        <br><br><a class="home" href="${pageContext.request.contextPath}/seller.htm">Seller Home</a>
    </body>
    <script language="javascript">
        function deleteBook(id) {
            var resp = confirm('Are you sure you want to delete this book?');
            if (resp) {
                var f = document.form;
                f.method="post";
                f.action = 'stock.htm?action=delete&id=' + id;
                f.submit();
            }
        }
    </script>
</html>

