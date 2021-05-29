/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "DENOMINACIONBILLETE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Denominacionbillete.findAll", query = "SELECT d FROM Denominacionbillete d"),
    @NamedQuery(name = "Denominacionbillete.findByValor", query = "SELECT d FROM Denominacionbillete d WHERE d.valor = :valor"),
    @NamedQuery(name = "Denominacionbillete.findById", query = "SELECT d FROM Denominacionbillete d WHERE d.id = :id")})
public class Denominacionbillete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private int valor;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "denominacionbillete")
    private Collection<Rentaxbillete> rentaxbilleteCollection;

    public Denominacionbillete() {
    }

    public Denominacionbillete(Integer id) {
        this.id = id;
    }

    public Denominacionbillete(Integer id, int valor) {
        this.id = id;
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<Rentaxbillete> getRentaxbilleteCollection() {
        return rentaxbilleteCollection;
    }

    public void setRentaxbilleteCollection(Collection<Rentaxbillete> rentaxbilleteCollection) {
        this.rentaxbilleteCollection = rentaxbilleteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Denominacionbillete)) {
            return false;
        }
        Denominacionbillete other = (Denominacionbillete) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Denominacionbillete{" + "valor=" + valor + ", id=" + id + ", rentaxbilleteCollection=" + rentaxbilleteCollection + '}';
    }

}
