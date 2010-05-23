<%-- 
    Document   : error_MoneyTransfer
    Created on : 23/05/2010, 1:08:56 AM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Money Transfer</title>
    </head>
    <body>
        <h1>Move your money!</h1>
        <form action="moneyTransfer" method="post">
                <p>
                    Target Account: <input type="text" name="toAccount" size="25"/>
                </p>
                <p>
                    From Account: <input type="text" name="fromAccount" size="25"/>
                </p>
                <p>
                    Ammount: $<input type="text" name="ammount"/>
                </p>
                <input type="submit" value="Submit">
            </form>
        <p>
            Error: Transfer could not be completed.
        </p>
        <p>
            Make sure that the ammount to transfer is greater than zero.
            Ensure that you have the correct account information.
            The account must exist and you must own each account.
        </p>
    </body>
</html>
