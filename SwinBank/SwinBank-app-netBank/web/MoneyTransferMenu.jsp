<%-- 
    Document   : MoneyTransferMenu
    Created on : 22/05/2010, 8:43:29 PM
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
                    To Account: <input type="text" name="toAccount" size="25"/>
                </p>
                <p>
                    From Account: <input type="text" name="fromAccount" size="25"/>
                </p>
                <p>
                    Ammount: $<input type="text" name="ammount"/>
                </p>
                <input type="submit" value="Submit">
            </form>
    </body>
</html>
