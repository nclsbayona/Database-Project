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
public class LineaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "RENTAID")
    private int rentaid;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;

    public LineaPK() {
    }

    public LineaPK(int rentaid) {
        this.rentaid = rentaid;
    }

    public int getRentaid() {
        return rentaid;
    }

    public void setRentaid(int rentaid) {
        this.rentaid = rentaid;
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
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LineaPK)) {
            return false;
        }
        LineaPK other = (LineaPK) object;
        if (this.rentaid != other.rentaid) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rentaid=" + rentaid + ", id=" + id ;
    }

}
