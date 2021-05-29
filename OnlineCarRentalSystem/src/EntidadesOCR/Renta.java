/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "RENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Renta.findAll", query = "SELECT r FROM Renta r"),
    @NamedQuery(name = "Renta.findByNumero", query = "SELECT r FROM Renta r WHERE r.numero = :numero"),
    @NamedQuery(name = "Renta.findByFechahora", query = "SELECT r FROM Renta r WHERE r.fechahora = :fechahora"),
    @NamedQuery(name = "Renta.findById", query = "SELECT r FROM Renta r WHERE r.id = :id")})
public class Renta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private int numero;
    @Basic(optional = false)
    @Column(name = "FECHAHORA")
    @Temporal(TemporalType.DATE)
    private Date fechahora;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "renta")
    private Collection<Rentaxbillete> rentaxbilleteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "renta")
    private Collection<Linea> lineaCollection;
    @JoinColumn(name = "PARAMETROID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Parametro parametroid;

    public Renta() {
    }

    public Renta(Integer id) {
        this.id = id;
    }

    public Renta(int numero, Date fechahora) {
        this.numero = numero;
        this.fechahora = fechahora;
    }

    public Renta(int numero, Date fechahora, Parametro parametroid) {
        this.numero = numero;
        this.fechahora = fechahora;
        this.parametroid = parametroid;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getFechahora() {
        return fechahora;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
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

    @XmlTransient
    public Collection<Linea> getLineaCollection() {
        return lineaCollection;
    }

    public void setLineaCollection(Collection<Linea> lineaCollection) {
        this.lineaCollection = lineaCollection;
    }

    public Parametro getParametroid() {
        return parametroid;
    }

    public void setParametroid(Parametro parametroid) {
        this.parametroid = parametroid;
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
        if (!(object instanceof Renta)) {
            return false;
        }
        Renta other = (Renta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String ret="Renta{" + "numero=" + numero + ", fechahora=" + fechahora + ", id=" + id;
        if (rentaxbilleteCollection!=null&&rentaxbilleteCollection.size()>0){
            ret+= ", rentaxbilleteCollection=";
            ret = rentaxbilleteCollection.stream().map(rentaxb -> (rentaxb.toString()+' '+',')).reduce(ret, String::concat);
        }
        if (lineaCollection!=null&&lineaCollection.size()>0){
            ret+= ", lineaCollection=";
            ret = lineaCollection.stream().map(linea -> (linea.toString()+' '+',')).reduce(ret, String::concat);
        }
        ret=ret+", parametro=" + parametroid.toString()+ '}';
        return ret;
    }
}
