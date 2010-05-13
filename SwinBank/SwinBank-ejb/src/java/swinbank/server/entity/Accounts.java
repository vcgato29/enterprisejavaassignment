/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Vipin
 */
@Entity
@Table(name = "ACCOUNTS")
@NamedQueries({@NamedQuery(name = "Accounts.findAll", query = "SELECT a FROM Accounts a"), @NamedQuery(name = "Accounts.findByAccountid", query = "SELECT a FROM Accounts a WHERE a.accountid = :accountid"), @NamedQuery(name = "Accounts.findByCustid", query = "SELECT a FROM Accounts a WHERE a.custid = :custid"), @NamedQuery(name = "Accounts.findByAccounttype", query = "SELECT a FROM Accounts a WHERE a.accounttype = :accounttype"), @NamedQuery(name = "Accounts.findByBalance", query = "SELECT a FROM Accounts a WHERE a.balance = :balance")})
public class Accounts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACCOUNTID")
    private Integer accountid;
    @Column(name = "CUSTID")
    private String custid;
    @Column(name = "ACCOUNTTYPE")
    private Character accounttype;
    @Column(name = "BALANCE")
    private Double balance;

    public Accounts() {
    }

    public Accounts(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public Character getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(Character accounttype) {
        this.accounttype = accounttype;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountid != null ? accountid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accounts)) {
            return false;
        }
        Accounts other = (Accounts) object;
        if ((this.accountid == null && other.accountid != null) || (this.accountid != null && !this.accountid.equals(other.accountid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Accounts[accountid=" + accountid + "]";
    }

}
