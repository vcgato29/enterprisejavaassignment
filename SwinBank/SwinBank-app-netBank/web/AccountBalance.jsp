<%-- 
    Document   : account
    Created on : 28/04/2010, 1:34:45 AM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account - Swinbank</title>
    </head>
    <body>
        <jsp:useBean id="userBean" scope="session" class="web.UserBean"/>
        <h1>Savings Account</h1>
        
        <p>Balance: $${userBean.balance}</p>
        
        Click here to <a href="logout">logout</a>
        <%-- <a href="logout" >logout</a> --%>
    </body>
</html>
