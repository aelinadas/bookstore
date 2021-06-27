<%-- 
    Document   : forgot-password
    Created on : Jul 24, 2020, 1:00:55 AM
    Author     : aelinadas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password</title>
    </head>
    <body>
        <h1>Forgot Password</h1>
        <form action="login.htm" method="POST">
            <label for="email">Email:</label>
            <input id="email" type="email" name="email" placeholder="Email" required>
            <br/>
            <br/>
            <input type="submit" value="Submit">
            <input type="hidden" value="generateToken" name="action" />
        </form>
    </body>
</html>
