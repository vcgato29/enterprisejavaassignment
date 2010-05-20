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
 * @author Mark
 */
@Entity
@Table(name = "ACCOUNTS")
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountid", query = "SELECT a FROM Account a WHERE a.accountid = :accountid"),
    @NamedQuery(name = "Account.findByCustid", query = "SELECT a FROM Account a WHERE a.custid = :custid"),
    @NamedQuery(name = "Account.findByAccounttype", query = "SELECT a FROM Account a WHERE a.accounttype = :accounttype"),
    @NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance"),
    @NamedQuery(name = "Account.findByIsactive", query = "SELECT a FROM Account a WHERE a.isactive = :isactive"),
    @NamedQuery(name = "Account.findByCustidAndIsActive", query = "SELECT a FROM Account a WHERE a.custid = :custid AND a.isactive = :isactive"),
    @NamedQuery(name = "Account.accountsCountActive", query = "SELECT a FROM Account a WHERE a.custid = :custid AND a.isactive = 1")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACCOUNTID")
    private Integer accountid;
    @Column(name = "CUSTID")
    private Integer custid;
    @Column(name = "ACCOUNTTYPE")
    private Character accounttype;
    @Column(name = "BALANCE")
    private Double balance;
    @Column(name = "ISACTIVE")
    private Short isactive;

    public Account() {
    }

    public Account(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
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

    public Short getIsactive() {
        return isactive;
    }

    public void setIsactive(Short isactive) {
        this.isactive = isactive;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountid == null && other.accountid != null) || (this.accountid != null && !this.accountid.equals(other.accountid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Account[accountid=" + accountid + "]";
    }
}
