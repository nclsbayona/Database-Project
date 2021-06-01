/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author Administrator
 */
@Embeddable
public class RentaxbilletePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "RENTAID")
    private int rentaid;
    @Basic(optional = false)
    @Column(name = "DENOMINACIONBILLETEID")
    private int denominacionbilleteid;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    public RentaxbilletePK() {
    }

    public RentaxbilletePK(int rentaid, int denominacionbilleteid) {
        this.rentaid = rentaid;
        this.denominacionbilleteid = denominacionbilleteid;
    }

    public int getRentaid() {
        return rentaid;
    }

    public void setRentaid(int rentaid) {
        this.rentaid = rentaid;
    }

    public int getDenominacionbilleteid() {
        return denominacionbilleteid;
    }

    public void setDenominacionbilleteid(int denominacionbilleteid) {
        this.denominacionbilleteid = denominacionbilleteid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) rentaid;
        hash += (int) denominacionbilleteid;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentaxbilletePK)) {
            return false;
        }
        RentaxbilletePK other = (RentaxbilletePK) object;
        if (this.rentaid != other.rentaid) {
            return false;
        }
        if (this.denominacionbilleteid != other.denominacionbilleteid) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rentaid=" + rentaid + ", denominacionbilleteid=" + denominacionbilleteid + ", id=" + id;
    }
    
}
