<%-- 
    Document   : accounts
    Created on : 02/05/2010, 1:37:32 AM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Account Selection</title>
    </head>
    <body>
        <div>
            <p>
                Welcome to Swinbank.
                Please enter an account number to get started
            </p>
            <form action="account" method="post">
                <p>
                    Account Number: <input type="text" name="accountId" size="25">
                </p>
                <input type="submit" value="Submit">
            </form>
            <p><strong>You do not have permission to access that account! please enter the account number for an account you own</strong></p>
        </div>
    </body>
</html> 