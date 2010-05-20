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

/**
 *
 * @author Vipin
 */
@Entity
@Table(name = "TRANSACTIONS")
@NamedQueries({@NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
@NamedQuery(name = "Transactions.findByTransid", query = "SELECT t FROM Transactions t WHERE t.transid = :transid"),
@NamedQuery(name = "Transactions.findByTranstype", query = "SELECT t FROM Transactions t WHERE t.transtype = :transtype"),
@NamedQuery(name = "Transactions.findByDate", query = "SELECT t FROM Transactions t WHERE t.date = :date"),
@NamedQuery(name = "Transactions.findByFromaccountid", query = "SELECT t FROM Transactions t WHERE t.fromaccountid = :fromaccountid"),
@NamedQuery(name = "Transactions.findByAmount", query = "SELECT t FROM Transactions t WHERE t.amount = :amount"),
@NamedQuery(name = "Transactions.findByRecaccountid", query = "SELECT t FROM Transactions t WHERE t.recaccountid = :recaccountid"),
@NamedQuery(name = "Transactions.findByDescription", query = "SELECT t FROM Transactions t WHERE t.description = :description"),
@NamedQuery(name = "Transactions.findTransactionList", query = "SELECT t FROM Transactions t WHERE (t.fromaccountid = :account OR t.recaccountid = :account) AND t.date <= :todate AND t.date >= :fromdate")})
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TRANSID")
    private Integer transid;
    @Column(name = "TRANSTYPE")
    private Character transtype;
    @Column(name = "DATE")
    private long date;
    @Column(name = "FROMACCOUNTID")
    private String fromaccountid;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "RECACCOUNTID")
    private String recaccountid;
    @Column(name = "DESCRIPTION")
    private String description;

    public Transactions() {
    }

    public Transactions(Integer transid) {
        this.transid = transid;
    }

    public Integer getTransid() {
        return transid;
    }

    public void setTransid(Integer transid) {
        this.transid = transid;
    }

    public Character getTranstype() {
        return transtype;
    }

    public void setTranstype(Character transtype) {
        this.transtype = transtype;
    }

    public long getDate() {
        return date;
    }
    
    public Date getDateAsDate() {
        return new Date(date);
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getFromaccountid() {
        return fromaccountid;
    }

    public void setFromaccountid(String fromaccountid) {
        this.fromaccountid = fromaccountid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRecaccountid() {
        return recaccountid;
    }

    public void setRecaccountid(String recaccountid) {
        this.recaccountid = recaccountid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transid != null ? transid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.transid == null && other.transid != null) || (this.transid != null && !this.transid.equals(other.transid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Transactions[transid=" + transid + "]";
    }

}
