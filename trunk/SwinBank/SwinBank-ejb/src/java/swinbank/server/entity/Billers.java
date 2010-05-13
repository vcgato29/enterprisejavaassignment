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
@Table(name = "BILLERS")
@NamedQueries({@NamedQuery(name = "Billers.findAll", query = "SELECT b FROM Billers b"), @NamedQuery(name = "Billers.findByBillerid", query = "SELECT b FROM Billers b WHERE b.billerid = :billerid"), @NamedQuery(name = "Billers.findByName", query = "SELECT b FROM Billers b WHERE b.name = :name"), @NamedQuery(name = "Billers.findByAccountid", query = "SELECT b FROM Billers b WHERE b.accountid = :accountid")})
public class Billers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BILLERID")
    private Integer billerid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ACCOUNTID")
    private String accountid;

    public Billers() {
    }

    public Billers(Integer billerid) {
        this.billerid = billerid;
    }

    public Integer getBillerid() {
        return billerid;
    }

    public void setBillerid(Integer billerid) {
        this.billerid = billerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billerid != null ? billerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billers)) {
            return false;
        }
        Billers other = (Billers) object;
        if ((this.billerid == null && other.billerid != null) || (this.billerid != null && !this.billerid.equals(other.billerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Billers[billerid=" + billerid + "]";
    }

}
