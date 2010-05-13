/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinbank.server.ejb.transaction;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.InvalidAccountException;
import swinbank.server.policy.InvalidAmmountException;
import swinbank.server.policy.InvalidClientException;
import swinbank.server.policy.InvalidFundsException;
import swinbank.server.policy.SwinDatabase;
import swinbank.server.policy.SwinDatabase.UserAccount;

/**
 *
 * @author James
 */
@Stateless
@Remote(TransactionRemote.class)
public class Transaction {

    public void deposit(String accountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {

        //check if its a IB
        if (clientType == ClientType.IB) {
            throw new InvalidClientException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        //check if the account exisis
        if (!accountExists(accountId)) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //deposit money from a TM or ATM into any account
        SwinDatabase.deposit(accountId, amount, description);
    }

    public void withdrawal(String custId, String accountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check if its a IB
        if (clientType == ClientType.IB) {
            throw new InvalidClientException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        //check if the account exisis
        if (!accountExists(accountId)) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check if the amount is greater than the balance
        if (!accountHasMoney(amount, accountId)) {
            throw new AccessDeniedException("\nNot enough funds!");
        }

        if (clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (ownAccount(accountId, custId)) {
                //withdrawal money from customers own account at ATM
                SwinDatabase.withdrawal(accountId, amount, description);
            } else {
                throw new AccessDeniedException("\nYou do not own the Accounts!");
            }
        } else {//TM
            //withdrawal money from TM
            SwinDatabase.withdrawal(accountId, amount, description);
        }

    }

    public void moneyTransfer(String custId, String toAccountId, String fromAccountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check amount is positive
        if (amount <= 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        //check if the to account exisis
        if (!accountExists(toAccountId)) {
            throw new AccessDeniedException("\nTo Account does not Exist!");
        }

        //check if the from account exisis
        if (!accountExists(fromAccountId)) {
            throw new AccessDeniedException("\nFrom Account does not Exist!");
        }

        //check if the amount is greater than the balance of the from account
        if (!accountHasMoney(amount, fromAccountId)) {
            throw new InvalidFundsException("\nNot enough funds!");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the two accounts
            if (ownAccount(toAccountId, custId) && ownAccount(fromAccountId, custId)) {
                SwinDatabase.moneyTransfer(fromAccountId, toAccountId, amount, description);
            } else {
                throw new AccessDeniedException("\nYou do not own one of the Accounts!");
            }
        } else {//TM
            SwinDatabase.moneyTransfer(fromAccountId, toAccountId, amount, description);
        }
    }

    private void billPayment(String custId, String accountId, String billerId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check if its not a IB
        if (clientType == ClientType.TM || clientType == ClientType.ATM) {
            throw new InvalidClientException("\nYou do not have access to pay a bill!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        //get the account id of the biller
        String billerAccountId = SwinDatabase.biller(billerId).accountId();
        //check if the  biller exisis
        if (!accountExists(billerAccountId)) {
            throw new AccessDeniedException("\nBiller does not Exist!");
        }

        //check biller is a Biller account
        if (SwinDatabase.account(billerAccountId).type != AccountType.Biller) {
            throw new AccessDeniedException("\nBiller account is not a bill account!");
        }

        //check if the  account exisis
        if (!accountExists(accountId)) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }
        //check account is a Standard account
        if (SwinDatabase.account(accountId).type != AccountType.Standard) {
            throw new InvalidAccountException("\nAccount account is not a standard account!");
        }

        //check if the amount is greater than the balance of the from account
        if (!accountHasMoney(amount, accountId)) {
            throw new InvalidFundsException("\nNot enough funds!");
        }

        //pay bill
        SwinDatabase.billPayment(accountId, billerAccountId, description);

    }

    private boolean ownAccount(String accountId, String custId) {
        UserAccount acc = SwinDatabase.getAccount(accountId);
        if (acc.userId.equals(custId)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean accountExists(String accountId) {
        UserAccount acc = SwinDatabase.getAccount(accountId);
        //check if the account exisis
        if (acc == null) {
            return false;
        }
        return true;
    }

    private boolean accountHasMoney(Double amount, String accountId) {
        UserAccount acc = SwinDatabase.getAccount(accountId);

        //check if the amount is greater than the balance
        if (amount > acc.balance) {
            return false;
        }
        return true;
    }
}
