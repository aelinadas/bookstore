<%-- 
    Document   : update-password-seller
    Created on : Jun 3, 2020, 3:25:32 PM
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
            <c:when test="${empty sessionScope.seller}">
                <h2 align="center">Something Went wrong!!</h2>
            </c:when>
        <c:otherwise>
        <form action="seller.htm" method="POST" onsubmit="return validateForm();">
            <label for="newpassword">New Password:</label>
            <input id="newpassword" type="password" name="newpassword" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*]).{8,20}$" placeholder="New Password">
            <br/>
            <br/>
            <label for="confirmpassword">Confirm Password:</label>
            <input id="confirmpassword" type="password" name="confirmpassword" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*]).{8,20}$" placeholder="Confirm Password">
            <br/>
            <br/> 
            <input type="hidden" value="updatePwd" name="action"/>
            <input type="submit" value="Update Changes">
            <br/>
            <br/><a class="home" href="${pageContext.request.contextPath}/seller.htm">Back</a>
        </form>
        </c:otherwise>
        </c:choose>
        <br/>
        <br/>
        <form action="home.htm" method="POST">
            <input type="hidden" value="logout" name="action" /> 
            <input type="submit" value="Logout" name="" />
        </form>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script>
            var email = document.querySelector('input[name="email"]');
            var conditionChecked = true;

            email.addEventListener("change", function () {
                if (email.value) {
                    $.ajax({
                        url: 'update.htm',
                        type: "POST",
                        data: {
                            action: "validateConsumer",
                            email : email.value
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data.exists === true) {
                                conditionChecked = false;
                            } else {
                                conditionChecked = true;
                            }
                        },
                        error: function (error) {
                            console.log('Error ${error}');
                        }
                    });
                }
            });

            function validateForm() {
                if (conditionChecked) {
                    return true;
                } else {
                    alert(email.value + ' already exits!!!');
                    return false;
                }
            }
        </script>
        <script>
            var password = document.getElementById("newpassword")
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
    </body>
</html>
