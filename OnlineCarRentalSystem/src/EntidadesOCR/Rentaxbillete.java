/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "RENTAXBILLETE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rentaxbillete.findAll", query = "SELECT r FROM Rentaxbillete r"),
    @NamedQuery(name = "Rentaxbillete.findByCantidad", query = "SELECT r FROM Rentaxbillete r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "Rentaxbillete.findByRentaid", query = "SELECT r FROM Rentaxbillete r WHERE r.rentaxbilletePK.rentaid = :rentaid"),
    @NamedQuery(name = "Rentaxbillete.findByDenominacionbilleteid", query = "SELECT r FROM Rentaxbillete r WHERE r.rentaxbilletePK.denominacionbilleteid = :denominacionbilleteid"),
    @NamedQuery(name = "Rentaxbillete.findById", query = "SELECT r FROM Rentaxbillete r WHERE r.rentaxbilletePK.id = :id")})
public class Rentaxbillete implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RentaxbilletePK rentaxbilletePK;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    @JoinColumn(name = "DENOMINACIONBILLETEID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Denominacionbillete denominacionbillete;
    @JoinColumn(name = "RENTAID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Renta renta;

    public Rentaxbillete() {
    }

    public Rentaxbillete(RentaxbilletePK rentaxbilletePK) {
        this.rentaxbilletePK = rentaxbilletePK;
    }

    public Rentaxbillete(RentaxbilletePK rentaxbilletePK, int cantidad) {
        this.rentaxbilletePK = rentaxbilletePK;
        this.cantidad = cantidad;
    }

    public Rentaxbillete(int rentaid, int denominacionbilleteid) {
        this.rentaxbilletePK = new RentaxbilletePK(rentaid, denominacionbilleteid);
    }
    
    public Rentaxbillete(int rentaid, int denominacionbilleteid, int cantidad) {
        this.rentaxbilletePK = new RentaxbilletePK(rentaid, denominacionbilleteid);
        this.cantidad=cantidad;
    }

    public int getId(){
        return rentaxbilletePK.getId();
    }
    
    public int getRentaid() {
        return rentaxbilletePK.getRentaid();
    }

    public int getDenominacionbilleteid() {
        return rentaxbilletePK.getDenominacionbilleteid();
    }
    
    public void setRentaxbilletePK(RentaxbilletePK rentaxbilletePK) {
        this.rentaxbilletePK = rentaxbilletePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Denominacionbillete getDenominacionbillete() {
        return denominacionbillete;
    }

    public void setDenominacionbillete(Denominacionbillete denominacionbillete) {
        this.denominacionbillete = denominacionbillete;
    }

    public Renta getRenta() {
        return renta;
    }
    
    public RentaxbilletePK getRentaxbilletePK() {
        return rentaxbilletePK;
    }

    public void setRenta(Renta renta) {
        this.renta = renta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentaxbilletePK != null ? rentaxbilletePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rentaxbillete)) {
            return false;
        }
        Rentaxbillete other = (Rentaxbillete) object;
        if ((this.rentaxbilletePK == null && other.rentaxbilletePK != null) || (this.rentaxbilletePK != null && !this.rentaxbilletePK.equals(other.rentaxbilletePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rentaxbillete{"+ rentaxbilletePK + ", cantidad=" + cantidad + ", denominacionbillete=" + denominacionbillete + ", renta=" + renta + '}';
    }
    
}
