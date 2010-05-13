/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinbankdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Vipin
 * @version 1.0
 * 
 */
public class DB {

    public static Connection getConnection() throws SQLException, IOException {
        System.setProperty("jdbc.drivers",
                "org.apache.derby.jdbc.ClientDriver");
        String url = "jdbc:derby://localhost/sun-appserv-samples;create=true";
        String username = "APP";
        String password = "APP";
        return DriverManager.getConnection(url, username, password);
    }

    public void createTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();

            //check for the presence of tables, and if they exist already, will delete them first.
            DatabaseMetaData dbd = cnnct.getMetaData();

            if (dbd.getTables(null, null, "BILLERS", null).next()) {
                stmnt.execute("DROP TABLE BILLERS");
            }

            if (dbd.getTables(null, null, "FINANCIALS", null).next()) {
                stmnt.execute("DROP TABLE FINANCIALS");
            }
            if (dbd.getTables(null, null, "TRANSACTIONS", null).next()) {
                stmnt.execute("DROP TABLE TRANSACTIONS");
            }
            if (dbd.getTables(null, null, "ACCOUNTS", null).next()) {
                stmnt.execute("DROP TABLE ACCOUNTS");
            }

            if (dbd.getTables(null, null, "LOGIN", null).next()) {
                stmnt.execute("DROP TABLE LOGIN");
            }


            //creates and populates the Login Table with Sample data.
            stmnt.execute("CREATE TABLE LOGIN (CustId CHAR(6) CONSTRAINT PK_LOGIN PRIMARY KEY, Password CHAR(6))");
            stmnt.executeUpdate("INSERT INTO LOGIN(CustId, Password) VALUES" + "('0001', 'aaaa')");
            stmnt.executeUpdate("INSERT INTO LOGIN(CustId, Password) VALUES" + "('0002', 'bbbb')");
            stmnt.executeUpdate("INSERT INTO LOGIN(CustId, Password) VALUES" + "('0003', 'cccc')");
            stmnt.executeUpdate("INSERT INTO LOGIN(CustId, Password) VALUES" + "('0004', 'dddd')");


            //creates and populates the Accounts Table with Sample data.
            stmnt.execute("CREATE TABLE ACCOUNTS (AccountId INT GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_ACCOUNTS PRIMARY KEY, CustId CHAR(6), AccountType CHAR, Balance DOUBLE)");
            stmnt.executeUpdate("INSERT INTO ACCOUNTS(CustId, AccountType, Balance) VALUES" + "('0001', 'B', 200.00)");
            stmnt.executeUpdate("INSERT INTO ACCOUNTS(CustId, AccountType, Balance) VALUES" + "('0002', 'P', 400.00)");
            stmnt.executeUpdate("INSERT INTO ACCOUNTS(CustId, AccountType, Balance) VALUES" + "('0003', 'B', 600.00)");
            stmnt.executeUpdate("INSERT INTO ACCOUNTS(CustId, AccountType, Balance) VALUES" + "('0004', 'P', 800.00)");
            stmnt.executeUpdate("INSERT INTO ACCOUNTS(CustId, AccountType, Balance) VALUES" + "('0005', 'P', 1000.00)");


            //creates and populates the Transactions Table with Sample data.
            stmnt.execute("CREATE TABLE TRANSACTIONS (TransId INT GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_TRANSACTIONS PRIMARY KEY, TransType CHAR, Date DATE, Time TIME, FromAccountId CHAR(14), Amount DOUBLE, RecAccountId CHAR(14), Description CHAR(128))");
            stmnt.executeUpdate("INSERT INTO TRANSACTIONS(TransType, Date, Time, FromAccountId, Amount, RecAccountId, Description) VALUES" + "('D', '2010-04-06', '12:30:25', NULL, 100.00, '1', '' )");
            stmnt.executeUpdate("INSERT INTO TRANSACTIONS(TransType, Date, Time, FromAccountId, Amount, RecAccountId, Description) VALUES" + "('D', '2010-04-15', '16:30:25', NULL, 200.00, '2', '' )");
            stmnt.executeUpdate("INSERT INTO TRANSACTIONS(TransType, Date, Time, FromAccountId, Amount, RecAccountId, Description) VALUES" + "('T', '2010-04-20', '13:30:25', '3', 100.00, '4', '' )");
            stmnt.executeUpdate("INSERT INTO TRANSACTIONS(TransType, Date, Time, FromAccountId, Amount, RecAccountId, Description) VALUES" + "('B', '2010-04-21', '14:30:25', '1', 150.00, '1', '' )");
            stmnt.executeUpdate("INSERT INTO TRANSACTIONS(TransType, Date, Time, FromAccountId, Amount, RecAccountId, Description) VALUES" + "('W', '2010-04-25', '09:30:25', '1', 100.00, NULL, '' )");


            //creates and populates the Financials Table with Sample data.
            stmnt.execute("CREATE TABLE FINANCIALS (StatementId INT GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_FINANCIALS PRIMARY KEY, Date DATE, Time TIME, AccountId CHAR(14), Balance DOUBLE)");
//            stmnt.executeUpdate("INSERT INTO TABLE FINANCIALS(Date, Time, AccountId, Balance) VALUES" + "('1', , , ,)");


            //creates and populates the Billers Table with Sample data.
            stmnt.execute("CREATE TABLE BILLERS (BillerId INT GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_BILLERS PRIMARY KEY, Name CHAR(40), AccountId CHAR(14))");
//            stmnt.execute("INSERT INTO TABLE BILLERS VALUES(BillerId, Name, AccountId) VALUES" + "( , , )");

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
    }
}
