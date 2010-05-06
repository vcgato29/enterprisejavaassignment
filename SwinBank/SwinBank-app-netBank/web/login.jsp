<%-- 
    Document   : login
    Created on : 28/04/2010, 1:15:27 AM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - Swinbank</title>
    </head>
    <body>
        <h1>Login:</h1>
        <form action="login" method="post">
        <p>
        Username: <input type="text" name="userid" size="25">
        </p>
        <p>
        Password: <input type="password" name="password" size="10">
        </p>
        <input type="submit" value="Submit">
        <input type="reset" value="Reset">
        </form>
    </body>
</html>
