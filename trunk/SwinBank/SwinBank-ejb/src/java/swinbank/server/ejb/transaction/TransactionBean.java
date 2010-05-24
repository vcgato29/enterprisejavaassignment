/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinbank.server.ejb.transaction;

import java.util.Date;
import javax.ejb.Remote;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import swinbank.server.entity.Account;
import swinbank.server.entity.Billers;
import swinbank.server.entity.Transactions;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.InvalidAccountException;
import swinbank.server.policy.InvalidAmmountException;
import swinbank.server.policy.InvalidClientException;
import swinbank.server.policy.InvalidFundsException;
import swinbank.server.policy.TransactionType;

/**
 *
 * @author James
 */
@Stateless
@Remote(TransactionRemote.class)
public class TransactionBean {

    @PersistenceContext
    private EntityManager em;

    public void deposit(int accountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {

        //check if its a IB
        if (clientType == ClientType.IB) {
            throw new InvalidClientException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        System.out.println("1");

        Account account = null;
        System.out.println("2");

        //check if the  account exisis
        try {
            account = getAccount(accountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }
        System.out.println("3");
        //deposit money from a TM or ATM into any account
        account.setBalance(account.getBalance() + amount);
        em.persist(account);
        System.out.println("4");
        newTransaction(TransactionType.Deposit, -1, account.getAccountid(), amount, description);
        System.out.println("5");

    }

    public void withdrawal(int custId, int accountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check if its a IB
        if (clientType == ClientType.IB) {
            throw new InvalidClientException("\nClient not alowed to make a Deposit!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        Account account = null;

        //check if the  account exisis
        try {
            account = getAccount(accountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check if the amount is greater than the balance
        if (!accountHasMoney(amount, account)) {
            throw new AccessDeniedException("\nNot enough funds!");
        }

        if (clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (account.getCustid().equals(custId)) {
                //withdrawal money from customers own account at ATM
                account.setBalance(account.getBalance() - amount);
                em.persist(account);
                newTransaction(TransactionType.Withdrawal, account.getAccountid(), -1, amount, description);
            } else {
                throw new AccessDeniedException("\nYou do not own the Accounts!");
            }
        } else {//TM
            //withdrawal money from TM
            account.setBalance(account.getBalance() - amount);
            em.persist(account);
            newTransaction(TransactionType.Withdrawal, account.getAccountid(), -1, amount, description);
        }

    }

    public void moneyTransfer(int custId, int toAccountId, int fromAccountId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check amount is positive
        if (amount <= 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }

        Account toAccount = null;
        Account fromAccount = null;
        //check if the  account exisis
        try {
            toAccount = getAccount(toAccountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nTo Account does not Exist!");
        }

        //check if the from account exisis
        try {
            fromAccount = getAccount(fromAccountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nFrom Account does not Exist!");
        }

        //check if the amount is greater than the balance of the from account
        if (!accountHasMoney(amount, fromAccount)) {
            throw new InvalidFundsException("\nNot enough funds!");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the two accounts
            if (toAccount.getCustid().equals(custId) && fromAccount.getCustid().equals(custId)) {
                toAccount.setBalance(toAccount.getBalance() + amount);
                em.persist(toAccount);
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                em.persist(fromAccount);
                newTransaction(TransactionType.MoneyTransfer, fromAccount.getAccountid(), toAccount.getAccountid(), amount, description);
            } else {
                throw new AccessDeniedException("\nYou do not own one of the Accounts!");
            }
        } else {//TM
            toAccount.setBalance(toAccount.getBalance() + amount);
            em.persist(toAccount);
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            em.persist(fromAccount);
            newTransaction(TransactionType.MoneyTransfer, fromAccount.getAccountid(), toAccount.getAccountid(), amount, description);
        }
    }

    public void billPayment(int accountId, int billerId, ClientType clientType, Double amount, String description) throws AccessDeniedException {
        //check if its not a IB
        if (clientType == ClientType.TM || clientType == ClientType.ATM) {
            throw new InvalidClientException("\nYou do not have access to pay a bill!");
        }

        //check amount is positive
        if (amount < 0) {
            throw new InvalidAmmountException("\nAmmount must be greater than zero!");
        }
        Billers biller = null;

        //check if the  biller exisis
        try {
            biller = getBiller(billerId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nBiller does not Exist!");
        }

        Account billAccount = null;
        //check if the billers account exisis
        try {
            billAccount = getAccount(biller.getAccountid());
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nBiller Account does not Exist!");
        }

        //check biller is a Biller account
        biller.getAccountid();
        if (billAccount.getAccounttype() != 'B') {
            throw new AccessDeniedException("\nBiller account is not a bill account!");
        }

        Account account = null;
        //check if the  account exisis
        try {
            account = getAccount(accountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check account is a Standard account
        if (account.getAccounttype() != 'S') {
            throw new InvalidAccountException("\nAccount account is not a standard account!");
        }

        //check if the amount is greater than the balance of the from account
        if (!accountHasMoney(amount, account)) {
            throw new InvalidFundsException("\nNot enough funds!");
        }

        //pay bill
        billAccount.setBalance(billAccount.getBalance() + amount);
        em.persist(billAccount);
        account.setBalance(account.getBalance() - amount);
        em.persist(account);
        newTransaction(TransactionType.MoneyTransfer, account.getAccountid(), billAccount.getAccountid(), amount, description);

    }


    private boolean accountHasMoney(Double amount, Account account) {

        //check if the amount is greater than the balance
        if (amount > account.getBalance()) {
            return false;
        }
        return true;
    }

    private Billers getBiller(int billerId) {
        Query userByLoginId = em.createNamedQuery("Billers.findByBillerid").setParameter("billerid", billerId);
        Billers biller = (Billers) userByLoginId.getSingleResult();
        return biller;
    }

    private Account getAccount(int accountId) {
        Query acountByIdQuery = em.createNamedQuery("Account.findByAccountid").setParameter("accountid", accountId);
        Account account = (Account) acountByIdQuery.getSingleResult();
        return account;
    }

    private void newTransaction(TransactionType type, int fromAccount, int recAccount, Double amount, String description) {
        Transactions tran = new Transactions();
        tran.setAmount(amount);
        tran.setDate(new Date().getTime());
        tran.setDescription(description);
        tran.setFromaccountid(fromAccount);
        tran.setRecaccountid(recAccount);

        if (type == TransactionType.BillPayment) {
            tran.setTranstype('B');
        }
        if (type == TransactionType.Deposit) {
            tran.setTranstype('D');
        }
        if (type == TransactionType.Withdrawal) {
            tran.setTranstype('W');
        }
        if (type == TransactionType.MoneyTransfer) {
            tran.setTranstype('M');
        }
        em.persist(tran);
    }
}
