<%-- 
    Document   : logout
    Created on : 28/04/2010, 1:34:19 AM
    Author     : Mark
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logout - Swinbank</title>
    </head>
    <jsp:useBean id="userBean" scope="session" class="web.UserBean"/>

    <body>
        <h1>
           Thank you for using Swinbank!
        </h1>
        <a href="/login.jsp">Login</a>
    </body>
</html>
