/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Vipin
 */
@Entity
@Table(name = "FINANCIALS")
@NamedQueries({@NamedQuery(name = "Financials.findAll", query = "SELECT f FROM Financials f"),
@NamedQuery(name = "Financials.findByStatementid", query = "SELECT f FROM Financials f WHERE f.statementid = :statementid"),
@NamedQuery(name = "Financials.findByDate", query = "SELECT f FROM Financials f WHERE f.date = :date"),
@NamedQuery(name = "Financials.findByTime", query = "SELECT f FROM Financials f WHERE f.time = :time"),
@NamedQuery(name = "Financials.findByAccountid", query = "SELECT f FROM Financials f WHERE f.accountid = :accountid"),
@NamedQuery(name = "Financials.findByBalance", query = "SELECT f FROM Financials f WHERE f.balance = :balance")})
public class Financials implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "STATEMENTID")
    private Integer statementid;
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "TIME")
    @Temporal(TemporalType.TIME)
    private Date time;
    @Column(name = "ACCOUNTID")
    private String accountid;
    @Column(name = "BALANCE")
    private Double balance;

    public Financials() {
    }

    public Financials(Integer statementid) {
        this.statementid = statementid;
    }

    public Integer getStatementid() {
        return statementid;
    }

    public void setStatementid(Integer statementid) {
        this.statementid = statementid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
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
        hash += (statementid != null ? statementid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Financials)) {
            return false;
        }
        Financials other = (Financials) object;
        if ((this.statementid == null && other.statementid != null) || (this.statementid != null && !this.statementid.equals(other.statementid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Financials[statementid=" + statementid + "]";
    }

}
