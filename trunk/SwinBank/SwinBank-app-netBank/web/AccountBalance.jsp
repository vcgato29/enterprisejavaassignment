<%-- 
    Document   : account
    Created on : 28/04/2010, 1:34:45 AM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
    prefix="c" %> 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account - Swinbank</title>
    </head>
    <body>
        <jsp:useBean id="userBean" scope="session" class="web.UserBean"/>
        <h1>Account: ${userBean.accountId}</h1>
        
        <p>Balance: $${userBean.balance}</p>
        
        <h2>Transactions</h2>
        <table>
            <c:forEach var="transactions" items="${userBean.transactions}">
                <tr>
                    <td>${transactions.DateAsDate}</td>
                    <td>${transactions.Fromaccountid}</td>
                    <td>${transactions.Recaccountid}</td>
                    <td>${transactions.Amount}</td>
                    <td>${transactions.Transtype}</td>
                </tr>
            </c:forEach>
        </table>
        
        <form action="transactionHistory">
            <select name="period" size="3" >
                <option>Today</option>
                <option selected="selected">This Month</option>
                <option>This Year</option>
                <option>All</option>
            </select>
            <input type="submit" value="refresh">
        </form>

        Click here to <a href="logout">logout</a>
        <%-- <a href="logout" >logout</a> --%>
    </body>
</html>
