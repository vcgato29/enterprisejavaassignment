<%-- 
    Document   : BillMenu
    Created on : 22/05/2010, 9:02:37 PM
    Author     : Mark
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bills</title>
    </head>
    <body>
        <h1>Pay Bill (not fred)</h1>
        <form action="billPayment" method="post">
                <p>
                    Biller: <input type="text" name="biller" size="25"/>
                </p>
                <p>
                    Amount: <input type="text" name="amount" size="25"/>
                </p>
                <p>
                    From Account: $<input type="text" name="fromAccount"/>
                </p>
                <p>
                    Description: <input type="text" name="description"/>
                </p>

                <input type="submit" value="Submit">
            </form>
    </body>
</html>
