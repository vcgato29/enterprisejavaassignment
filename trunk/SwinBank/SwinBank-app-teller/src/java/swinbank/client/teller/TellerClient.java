package swinbank.client.teller;

import java.rmi.RemoteException;
import java.util.Scanner;
import javax.ejb.EJB;
import swinbank.server.ejb.account.AccountRemote;
import swinbank.server.ejb.login.LoginRemote;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Vipin
 * @version 1.0
 * Modified version - by Vipin [28th April, 2010]
 * @version 1.1
 */
public class TellerClient {

    private Scanner in = new Scanner(System.in);
    private String userId;
    @EJB
    private static LoginRemote login;
    @EJB
    private static AccountRemote account;

    public static void main(String[] args) {

        TellerClient tm = new TellerClient();

        boolean loginSuccess = tm.login();

        if (loginSuccess) {
            System.out.println("\nLogin Successful !");
            tm.promptUserCommand();
        } else {
            System.out.println("\nLogin failed please try again .....");
            main(args);
        }

        //System.out.println("Thank You! Come Again");
    }

    public void promptUserCommand() {

        while (true) {
            System.out.println("\n************************");
            System.out.println("What do you want to do:");
            System.out.println("************************");
            System.out.println("\n0. Cancel");
            System.out.println("1. GetBalance");

            String choice = in.nextLine();

            if (choice.equals("0")) {
                System.out.println("\nThank You! Come Again");
                return;
            }
            if (choice.equals("1")) {
                this.getBalance();
            } else {
                System.out.println("\nPlease enter valid option.");
            }
        }
    }

    public boolean login() {

        System.out.println("\n******************************");
        System.out.println("Welcome to the SwinBank TM");
        System.out.println("******************************\n");

        System.out.println("\nPlease enter your user identification number: ");
        userId = in.nextLine();

        System.out.println("Please enter your password: ");
        String pass = in.nextLine();

        boolean loginSuccess = false;

        try {
            return loginSuccess = login.login(userId, pass, ClientType.TM);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void getBalance() {

        System.out.println("\nPlease enter Account ID");
        String accId = in.nextLine();

        double bal;


        try {
            bal = account.getBalance(accId, userId, ClientType.TM);

            System.out.println("\nCurrent Balance: $" + bal);

        } catch (RemoteException e) {
            System.out.println(e.getCause().getCause().getMessage());

        }
    }
}

