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
@Table(name = "LOGIN")
@NamedQueries({
    @NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l"),
    @NamedQuery(name = "Login.findByCustid", query = "SELECT l FROM Login l WHERE l.custid = :custid"),
    @NamedQuery(name = "Login.findByPassword", query = "SELECT l FROM Login l WHERE l.password = :password"),
    @NamedQuery(name = "Login.findByIsstaff", query = "SELECT l FROM Login l WHERE l.isstaff = :isstaff")})
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CUSTID")
    private Integer custid;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ISSTAFF")
    private Short isstaff;

    public Login() {
    }

    public Login(Integer custid) {
        this.custid = custid;
    }

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
        this.custid = custid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Short getIsstaff() {
        return isstaff;
    }

    public void setIsstaff(Short isstaff) {
        this.isstaff = isstaff;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custid != null ? custid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        if ((this.custid == null && other.custid != null) || (this.custid != null && !this.custid.equals(other.custid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "swinbank.server.entity.Login[custid=" + custid + "]";
    }

}
