/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NegocioOCR;

import EntidadesOCR.Carro;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Linea;
import EntidadesOCR.Parametro;
import EntidadesOCR.Renta;
import EntidadesOCR.Rentaxbillete;
import EntidadesOCR.classes.DTO;
import EntidadesOCR.classes.DTOresumen;
import IntegracionDatos.CarroJpaController;
import IntegracionDatos.DenominacionbilleteJpaController;
import IntegracionDatos.LineaJpaController;
import IntegracionDatos.ParametroJpaController;
import IntegracionDatos.RentaJpaController;
import IntegracionDatos.RentaxbilleteJpaController;
import IntegracionDatos.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
public class FacadeOCR {

    private CarroJpaController carroControl = new CarroJpaController();
    private DenominacionbilleteJpaController denominacion_billeteControl = new DenominacionbilleteJpaController();
    private LineaJpaController lineaControl = new LineaJpaController();
    private ParametroJpaController parametroControl = new ParametroJpaController();
    private RentaJpaController rentaControl = new RentaJpaController();

    public CarroJpaController getCarroControl() {
        return carroControl;
    }

    public DenominacionbilleteJpaController getDenominacion_billeteControl() {
        return denominacion_billeteControl;
    }

    public LineaJpaController getLineaControl() {
        return lineaControl;
    }

    public ParametroJpaController getParametroControl() {
        return parametroControl;
    }

    public RentaJpaController getRentaControl() {
        return rentaControl;
    }

    public RentaxbilleteJpaController getRentaXbilleteControl() {
        return rentaXbilleteControl;
    }
    private RentaxbilleteJpaController rentaXbilleteControl = new RentaxbilleteJpaController();

    public List<Carro> consultarCarros() {
        EntityManager em = this.carroControl.getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM CARRO", Carro.class);
        return query.getResultList();
    }

    public List<Carro> consultarCarrosDisponibles() {
        List<Carro> carros = this.consultarCarros();
        List<Carro> carros_disponibles = new ArrayList<>();
        for (Carro c : carros) {
            if (c.getUnidadesdisponibles() == 0) {
                carros_disponibles.add(c);
            }
        }
        return carros_disponibles;
    }

    public DTOresumen construirRespuestaRenta(Renta the_renta) {
        DTOresumen dtoresumen;
        double saldo_total_renta = 0;
        for (Iterator<Linea> it = this.rentaControl.getLineaCollection(the_renta.getId()).iterator(); it.hasNext();) {
            Linea linea = it.next();
            saldo_total_renta += (linea.getCantidad() * this.carroControl.consultarPrecioCarro(linea.getCarroid().getId()));
        }
        double saldo_ingresados = 0;
        for (Rentaxbillete linea : the_renta.getRentaxbilleteCollection()) {
            saldo_ingresados = saldo_ingresados + (linea.getDenominacionbillete().getValor() * linea.getCantidad());
        }
        double vueltos = saldo_ingresados - saldo_total_renta;
        Pair<Integer, Integer> the_pair = this.parametroControl.returnParametroInfo(the_renta.getId());
        int cantidad_rentados = 0;
        for (Linea linea : this.rentaControl.getLineaCollection(the_renta.getId())) {
            cantidad_rentados += linea.getCantidad();
        }
        saldo_total_renta = saldo_total_renta * ((((100 - (the_pair.getValue() * ((int) (cantidad_rentados / the_pair.getKey()))))) / 100));
        dtoresumen = new DTOresumen(the_renta.getLineaCollection(), saldo_total_renta, saldo_ingresados, vueltos);
        return dtoresumen;
    }

    public List<Denominacionbillete> consultarTiposBillete() {
        DenominacionbilleteJpaController dbjpa = new DenominacionbilleteJpaController();
        List<Denominacionbillete> tipos = dbjpa.findDenominacionbilleteEntities();
        return tipos;
    }

    public Integer consultarCantidadCarros() {
        Integer cant;
        EntityManager em = this.carroControl.getEntityManager();
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM CARRO");
        cant = (Integer) query.getSingleResult();
        return cant;
    }

    public Carro consultarCarro(Integer id) {
        Carro cant;
        EntityManager em = this.carroControl.getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM CARRO WHERE ID=?", Carro.class);
        query.setParameter(1, id);
        cant = (Carro) query.getSingleResult();
        return cant;
    }

    public Integer consultarCantidadCarrosDisponibles() {
        Integer cant;
        EntityManager em = this.carroControl.getEntityManager();
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM CARRO WHERE UNIDADESDISPONIBLES>0");
        cant = (Integer) query.getSingleResult();
        return cant;
    }

    public DTOresumen crearRenta(DTO<Renta> objetoRenta) {
        DTOresumen dtoresumen;
        if (this.consultarCantidadCarros() == 0) {
            dtoresumen = new DTOresumen("No existen carros en el sistema!");
        } else if (this.consultarCantidadCarrosDisponibles() == 0) {
            dtoresumen = new DTOresumen("No existen carros disponibles!");
        } else {
            int parametroID = 1;
            Query query = (this.parametroControl.getEntityManager().createNativeQuery("SELECT * FROM PARAMETRO WHERE ID=?", Parametro.class));
            int numero_renta = ((int) (this.rentaControl.getEntityManager().createNativeQuery("SELECT COUNT(*) FROM RENTA").getSingleResult())) + 1;
            query.setParameter(1, parametroID);
            Parametro parametro = ((Parametro) (query.getSingleResult()));
            Renta renta = new Renta(numero_renta, new Date(), parametro);
            this.rentaControl.create(renta);
            objetoRenta.setObj(renta);
            dtoresumen = this.construirRespuestaRenta(renta);
        }
        return dtoresumen;
    }

    public DTOresumen agregarLinea(DTO<Linea> l, Integer cantidad) {
        DTOresumen dtoresumen = null;
        if (!this.carroControl.carExists(l.getObj().getCarroid().getId())) {
            dtoresumen = new DTOresumen("No existe este carro en el catalogo");
        } else if (!this.carroControl.carAvailable(l.getObj().getCarroid().getId(), cantidad)) {
            dtoresumen = new DTOresumen("No existen unidades de este carro disponibles en el catalogo");
        } else {
            Linea the_line = this.rentaControl.getLineaFromRenta(l.getObj().getRentaid(), l.getObj().getCarroid().getId());
            Renta the_renta = l.getObj().getRenta();
            if (the_line != null) {
                the_renta.remove(the_line);
                this.lineaControl.updateAmountLine(the_line, l.getObj().getCantidad() + the_line.getCantidad());
                the_renta.add(the_line);
            } else {
                the_line = l.getObj();
                try {
                    this.lineaControl.create(the_line);
                    the_renta.add(the_line);
                } catch (Exception e) {
                }
            }
            dtoresumen = this.construirRespuestaRenta(the_renta);
        }
        return dtoresumen;
    }

    public DTOresumen agregarBillete(DTO<Rentaxbillete> dtorxb) {
        DTOresumen dtoresumen;
        System.out.println("El simple ID es: " + dtorxb.getObj().getId());
        if (!this.denominacion_billeteControl.denExists(dtorxb.getObj().getDenominacionbilleteid())) {
            dtoresumen = new DTOresumen("No existe la denominacion especificada");
        } else {
            Renta the_renta = dtorxb.getObj().getRenta();
            the_renta.addRentaxB(dtorxb.getObj());
            dtoresumen = this.construirRespuestaRenta(the_renta);
        }
        return dtoresumen;
    }

    public DTOresumen eliminarLinea(DTO<Linea> l) {
        DTOresumen dtoresumen;
        if (l.getObj() == null) {
            dtoresumen = new DTOresumen("La linea esta vacía");
        } else if (!this.lineaControl.buscarLineaDeRenta(l.getObj())) {
            dtoresumen = new DTOresumen("La linea no existe");
        } else {
            Renta the_renta = l.getObj().getRenta();
            try {
                System.out.println(the_renta.remove(l.getObj()));
                this.lineaControl.destroy(l.getObj().getLineaPK());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(FacadeOCR.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            dtoresumen = this.construirRespuestaRenta(the_renta);
        }
        return dtoresumen;
    }

    private double devolverSaldo(double saldo) {
        return saldo;
    }

    public DTOresumen terminarRenta(Renta renta) {
        DTOresumen dtoresumen;
        if (this.construirRespuestaRenta(renta).getCantidad_vueltos() < 0) {
            dtoresumen = new DTOresumen("No se puede finalizar la renta");
        } else {
            dtoresumen = this.construirRespuestaRenta(renta);
            this.devolverSaldo(dtoresumen.getCantidad_vueltos());
        }
        return dtoresumen;
    }

    public DTOresumen consultarRenta(DTO<Renta> dtorenta, Integer entrada3) {
        try {
            dtorenta.setObj(this.rentaControl.findRenta(entrada3));
            return this.construirRespuestaRenta(dtorenta.getObj());
        } catch (Exception e) {
            return null;
        }
    }

    public List<DTO<Carro>> consultarAcumulados() {
        List<DTO<Carro>> al = new ArrayList<>();
        for (Renta renta : this.rentaControl.findRentaEntities()) {
            for (Linea l : renta.getLineaCollection()) {
                al.add(new DTO<>(l.getCarroid()));
            }
        }
        return al;
    }
}
