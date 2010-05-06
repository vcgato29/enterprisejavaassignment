/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author Mark
 */
public class UserBean implements Serializable {
    private String username;
    private double balance;
    private String accountId;

    public static final String PROP_USERNAME_PROPERTY = "username";
    public static final String PROP_BALANCE_PROPERTY = "balance";
    public static final String PROP_ACCOUNT_PROPERTY = "account";

    private PropertyChangeSupport propertySuport;

    public UserBean()
    {
        propertySuport = new PropertyChangeSupport(this);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        String oldAccount = this.accountId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        String oldVal = this.username;
        this.username = userName;
        propertySuport.firePropertyChange(PROP_USERNAME_PROPERTY, oldVal, username);
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
