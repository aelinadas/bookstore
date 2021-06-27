<%-- 
    Document   : signup
    Created on : May 27, 2020, 3:08:14 PM
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
        <title>Sign Up</title>
    </head>
    <body>
        <h1 align="center">Sign Up</h1>
        <form action="home.htm" method="POST" id="selectForm" onsubmit="return validateForm();">
            <label for="fname">First Name:</label>
            <input id="name" type="text" name="fname" pattern="[a-zA-Z]+([\s][a-zA-Z]+)*" placeholder="FirstName" required>
            <br/>
            <br/>
            <label for="lname">Last Name:</label>
            <input id="username" type="text" name="lname" pattern="[a-zA-Z]+([\s][a-zA-Z]+)*" placeholder="LastName" required>
            <br/>
            <br/>
            <label for="email">Email:</label>
            <input id="email" type="email" name="email" pattern="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,63}$" placeholder="Email" required>
            <br/>
            <br/>
            <label for="password">Password:</label>
            <input id="password" type="password" name="password" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*]).{8,20}$" placeholder="Password" required>
            <br/>
            <br/>
            <label for="confirmpassword">Confirm Password:</label>
            <input id="confirmpassword" type="password" name="confirmpassword" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*]).{8,20}$" placeholder="Confirm Password" required>
            <br/>
            <br/>
            <input type="radio" class="input-hidden" name="org" value="buyersignup.htm" required/>Buyer
            <input type="radio" class="input-hidden" name="org" value="sellersignup.htm"/>Seller
            <input type="radio" class="input-hidden" name="org" value="bothsignup.htm"/>Both
            <br/>
            <br/>
            <input type="hidden" value="signup" name="action" />
            <input type="submit" value="Sign Up">
        </form>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script>
            var radios = document.getElementsByClassName('input-hidden');
            for (var i = 0; i < radios.length; i++) {
                radios[i].addEventListener('change', function (e) {
                    document.getElementById('selectForm').setAttribute('action', e.target.value);
                }, false);
            }
        </script>
        <script>
            var password = document.getElementById("password")
                    , confirm_password = document.getElementById("confirmpassword");

            function validatePassword() {
                if (password.value != confirm_password.value) {
                    confirm_password.setCustomValidity("Passwords Don't Match");
                } else {
                    confirm_password.setCustomValidity('');
                }
            }

            password.onchange = validatePassword;
            confirm_password.onkeyup = validatePassword;
        </script>
        <br><a href="index.htm">Home</a>
    </body>
</html>
