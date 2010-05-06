/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinbank.server.ejb.transaction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.SwinDatabase;
import swinbank.server.policy.SwinDatabase.UserAccount;
import swinbank.server.policy.TransactionType;

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
            throw new AccessDeniedException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount <= 0) {
            throw new AccessDeniedException("\nAmmount must be greater than zero!");
        }

        UserAccount acc = SwinDatabase.getAccount(accountId);
        //check if the account exisis
        if (acc == null) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //deposit money
        SwinDatabase.Deposit(accountId, amount);
        //add to the transaction history (type, date, from acc, to acc, amount, description)
        SwinDatabase.AddTransaction(TransactionType.Deposit, new Date(), null, accountId, amount, description);
    }

    public void withdrawal(String custId, String accountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check if its a IB
        if (clientType == ClientType.IB) {
            throw new AccessDeniedException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount <= 0) {
            throw new AccessDeniedException("\nAmmount must be greater than zero!");
        }

        UserAccount acc = SwinDatabase.getAccount(accountId);
        //check if the account exisis
        if (acc == null) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check if the amount is greater than the balance
        if (amount > acc.balance) {
            throw new AccessDeniedException("\nNot enough funds!");
        }

        //check that the account is owned by the customer
        if (acc.userId.equals(custId)) {
            SwinDatabase.Withdrawal(accountId, amount);
            SwinDatabase.AddTransaction(TransactionType.Withdrawal, new Date(), accountId, null, amount, description);
        }

        //the TM withdrawal
        if (clientType == ClientType.TM) {
            SwinDatabase.Withdrawal(accountId, amount);
            SwinDatabase.AddTransaction(TransactionType.Withdrawal, new Date(), accountId, null, amount, description);
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    public void moneyTransfer(String custId, String toAccountId, String fromAccountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check amount is positive
        if (amount <= 0) {
            throw new AccessDeniedException("\nAmmount must be greater than zero!");
        }

        UserAccount acc = SwinDatabase.getAccount(toAccountId);
        //check if the to account exisis
        if (acc == null) {
            throw new AccessDeniedException("\nTo Account does not Exist!");
        }

        acc = SwinDatabase.getAccount(fromAccountId);
        //check if the from account exisis
        if (acc == null) {
            throw new AccessDeniedException("\nFrom Account does not Exist!");
        }

        //check if the amount is greater than the balance of the from account
        if (amount > acc.balance) {
            throw new AccessDeniedException("\nNot enough funds!");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //own account only
            List<String> accounts = SwinDatabase.GetAccounts(custId);

            boolean to = false;
            boolean from = false;

            for (String account : accounts) {
                if (account.equals(toAccountId)) {
                    to = true;
                }
                if (account.equals(fromAccountId)) {
                    from = true;
                }
            }
            //Customer has the two accounts
            if (to && from) {
                SwinDatabase.Withdrawal(fromAccountId, amount);
                SwinDatabase.Deposit(toAccountId, amount);
                SwinDatabase.AddTransaction(TransactionType.MoneyTransfer, new Date(), fromAccountId, toAccountId, amount, description);
            }
            else
            {
                throw new AccessDeniedException("\nYou do not own one of the Accounts!");
            }
        } else {
            //any account
            SwinDatabase.Withdrawal(fromAccountId, amount);
            SwinDatabase.Deposit(toAccountId, amount);
            SwinDatabase.AddTransaction(TransactionType.MoneyTransfer, new Date(), fromAccountId, toAccountId, amount, description);
        }
    }
}
