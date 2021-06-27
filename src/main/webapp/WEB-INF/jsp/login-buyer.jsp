<%-- 
    Document   : login
    Created on : May 27, 2020, 3:06:21 PM
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
        <title>Buyer Login</title>
    </head>
    <body>
        <h1 align="center">Buyer Login</h1>
        <form action="login.htm" method="POST">
            <label for="email">Email:</label>
            <input id="email" type="email" name="email" placeholder="Email" required>
            <br/>
            <br/>
            <label for="password">Password:</label>
            <input id="password" type="password" name="password" placeholder="Password" required>
            <br/>
            <br/>
            <input type="checkbox" onclick="myFunction()">Show Password
            <br/>
            <br/>
            <input type="submit" value="Login">
            <input type="hidden" value="loginbuyer" name="action" />
        </form>
        <script>
            function myFunction() {
                var x = document.getElementById("password");
                if (x.type === "password") {
                  x.type = "text";
                } else {
                  x.type = "password";
                }
            }
        </script>
        <br><a href="index.htm">Home</a>
    </body>
</html>
