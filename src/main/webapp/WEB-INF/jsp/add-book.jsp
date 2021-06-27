<%-- 
    Document   : add-book
    Created on : Jun 2, 2020, 6:09:13 PM
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
        <title>Add Book</title>
    </head>
    <body>
        <h1 align="center">Add Book</h1>
        <form action="image.htm" method="POST" enctype="multipart/form-data">
            <label for="isbn">ISBN:</label>
            <input id="isbn" type="text" name="isbn" placeholder="ISBN" pattern="^(97(8|9))?\d{9}(\d|X)$" required>
            <br/>
            <br/>
            <label for="title">Title:</label>
            <input id="title" type="text" name="title" placeholder="Title" pattern="[a-zA-Z0-9]+([\s][a-zA-Z0-9]+)*" required>
            <br/>
            <br/>
            <label for="authors">Authors:</label>
            <input id="authors" type="text" name="authors" placeholder="Authors" pattern="[a-zA-Z\\s+]+(,[a-zA-Z\\s+]+)*" required>
            <br/>
            <br/>
            <label for="publicationDate">Publication Date</label>
            <input id="publicationDate" id ="maxdate" type="date" name="publicationDate" placeholder="Publication Date" max="2020-06-06" required>
            <br/>
            <br/>
            <label for="quantity">Quantity:</label>
            <input id="quantity" type="number" name="quantity" placeholder="Quantity" min="0" required>
            <br/>
            <br/>
            <label for="price">Price:</label>
            <input id="price" type="number" name="price" placeholder="Price" min="0.01" max="9999.99" step="0.01" required>
            <br/>
            <br/>
            <label for="image">Upload Image</label>
            <input id="image" type="file" name="image" accept="image/*" multiple required>
            <br/>
            <br/>
            <input type="submit" value="Add Book" name="add">
            <input type="hidden" value="add" name="action" />
            <br/>
            <br/>
            <a class="home" href="${pageContext.request.contextPath}/seller.htm">Seller Home</a>
        </form>   
    </body>
    <script>
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd
        }
        if (mm < 10) {
            mm = '0' + mm
        }

        today = yyyy + '-' + mm + '-' + dd;
        document.getElementById("maxdate").setAttribute("max", today);

    </script>
</html>

