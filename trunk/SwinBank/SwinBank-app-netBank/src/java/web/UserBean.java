/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.List;
import swinbank.server.entity.Transactions;

/**
 *
 * @author Mark
 */
public class UserBean implements Serializable {
    private int username;
    private double balance;
    private int accountId;
    private List<Transactions> transactions;

    public static final String PROP_USERNAME_PROPERTY = "username";
    public static final String PROP_BALANCE_PROPERTY = "balance";
    public static final String PROP_ACCOUNT_PROPERTY = "account";
    public static final String PROP_TRANSACTIONS_PROPERTY = "transactions";

    private PropertyChangeSupport propertySuport;

    public UserBean()
    {
        propertySuport = new PropertyChangeSupport(this);
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        int oldAccount = this.accountId;
        this.accountId = accountId;
        propertySuport.firePropertyChange(PROP_ACCOUNT_PROPERTY, oldAccount, accountId);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        double oldVal = this.balance;
        this.balance = balance;
        propertySuport.firePropertyChange(PROP_BALANCE_PROPERTY, oldVal, balance);

    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int userName) {
        int oldVal = this.username;
        this.username = userName;
        propertySuport.firePropertyChange(PROP_USERNAME_PROPERTY, oldVal, username);
    }

    public List<Transactions> getTransactions()
    {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions)
    {

        List<Transactions> oldVal = this.transactions;
        this.transactions = transactions;
        propertySuport.firePropertyChange(PROP_TRANSACTIONS_PROPERTY, oldVal, transactions);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertySuport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertySuport.removePropertyChangeListener(listener);
    }
}
