package swinbank.client.atm;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import swinbank.server.ejb.account.AccountRemote;
import swinbank.server.ejb.login.LoginRemote;
import swinbank.server.ejb.transaction.TransactionRemote;
import swinbank.server.entity.Transactions;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Daniel
 * @version 1.0
 * Modified version - by Vipin [27th April, 2010]
 * @version 1.1
 */
public class AtmClient {

    private Scanner in = new Scanner(System.in);
    private int userId, accId;
    @EJB
    private static LoginRemote login;
    @EJB
    private static AccountRemote account;
    @EJB
    private static TransactionRemote trans;

    public static void main(String[] args) {

        AtmClient atm = new AtmClient();

        boolean loginSuccess = atm.login();

        if (loginSuccess) {
            System.out.println("\nLogin Successful !");
            atm.promptAccountId();
            atm.selectOption();
        } else {
            System.out.println("\nLogin failed please try again .....");
            main(args);
        }

    }

    public void promptAccountId() {

        System.out.println("\nPlease enter your Account ID");
        try
        {
        accId = Integer.valueOf(in.nextLine());
        }
        catch(NumberFormatException e)
        {
        accId = -1;
        }
    }

    public void selectOption() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n************************");
            System.out.println("What do you want to do:");
            System.out.println("************************");
            System.out.println("\n0. Log Out");
            System.out.println("1. Get Balance");
            System.out.println("2. Transaction History");
            System.out.println("3. Deposit");
            System.out.println("4. Withdrawal");
            System.out.println("5. Money Transfer");

            

            try {
                option = Integer.parseInt(in.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter valid option.");
                selectOption();
                return;
            }

            switch (option) {
                case 0:
                    System.out.println("\nThank You! Come Again");
                    break;
                case 1:
                    getBalance();
                    break;
                case 2:
                    listTransactionHistory();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    transferFunds();
                    break;
                default:
                    System.out.println("\nPlease enter valid option.");
                    selectOption();
                    return;
            }
        }
    }

    public boolean login() {

        System.out.println("\n******************************");
        System.out.println("Welcome to the SwinBank ATM");
        System.out.println("******************************\n");

        System.out.println("\nPlease enter your user identification number: ");
        userId = Integer.valueOf(in.nextLine());

        System.out.println("Please enter your password: ");
        String pass = in.nextLine();

        try {
            return login.login(userId, pass, ClientType.ATM);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void getBalance() {

        double bal;

        try {
            bal = account.getBalance(accId, userId, ClientType.ATM);

            System.out.println("\nCurrent Balance: $" + bal);

        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());
        }
    }

    public void listTransactionHistory() {

        System.out.println("\nPlease choose the period:");
        System.out.println("\n0. Today");
        System.out.println("\n1. This Month");
        System.out.println("\n2. This Year");
        System.out.println("\n3. All");

        int option = Integer.valueOf(in.nextLine());

        Date startDate, endDate;
        startDate = new Date();
        endDate = new Date();

        try {
            switch (option) {
                case 0:
                    startDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate());
                    break;
                case 1:
                    startDate = new Date(endDate.getYear(), endDate.getMonth() - 1, endDate.getDate());
                    break;
                case 2:
                    startDate = new Date(endDate.getYear() - 1, endDate.getMonth(), endDate.getDate());
                    break;
                case 3:
                    startDate = new Date(endDate.getYear() - 100, endDate.getMonth(), endDate.getDate());
                    break;
                default:
                    return;
            }

            List<Transactions> transactions = account.getTransactions(accId, userId, ClientType.ATM, startDate, endDate);

            for (Transactions t : transactions) {
                System.out.println(new Date(t.getDate()).toString() + "\t" + t.getDescription() + "\t" + t.getAmount());
            }

        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());
        }

        System.out.println("\n End of History");
    }

    public void deposit() {

        System.out.println("\n How much will you deposit?");

        double amount = Double.valueOf(in.nextLine());

        try {
            trans.deposit(accId, ClientType.ATM, amount, "Swinbank ATM Deposit");
        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());
        }

        System.out.println("\n Deposit Successful");
    }

    public void withdraw() {

        System.out.println("\n How much will you withdraw?");

        double amount = Double.valueOf(in.nextLine());

        try {
            trans.withdrawal(userId, accId, ClientType.ATM, amount, "Swinbank ATM Withdrawal");
        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());
        }

        System.out.println("\n Withdraw Successful");
    }

    public void transferFunds() {

        System.out.println("\n Enter the account ID you want to transfer to");

        int toAcc = Integer.valueOf(in.nextLine());

        System.out.println("\n Enter how much you want to transfer");

        double amount = Double.valueOf(in.nextLine());

        try {
            trans.moneyTransfer(userId, toAcc, accId, ClientType.ATM, amount, "Swinbank ATM Transfer to Account #" + toAcc);
        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());
        }

        System.out.println("\n Transfer Successful");
    }
}
