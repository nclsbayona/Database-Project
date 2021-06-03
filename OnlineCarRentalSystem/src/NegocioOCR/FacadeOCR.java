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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        List<Carro> carros = this.carroControl.findCarroEntities();
        return carros;
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

    public List<Denominacionbillete> consultarTiposBillete() {
        DenominacionbilleteJpaController dbjpa = new DenominacionbilleteJpaController();
        List<Denominacionbillete> tipos = dbjpa.findDenominacionbilleteEntities();
        return tipos;
    }

    public Integer consultarCantidadCarros() {
        Integer cant;
        EntityManager em = this.carroControl.getEntityManager();
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM CARRO");
        System.out.print(query);
        cant = (Integer) query.getSingleResult();
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
            dtoresumen = new DTOresumen(null, 0, 0, 0);
        }
        return dtoresumen;
    }

    public DTOresumen agregarLinea(DTO<Linea> l) {
        DTOresumen dtoresumen = null;
        if (!this.carroControl.carExists(l.getObj().getCarroid().getId())) {
            dtoresumen = new DTOresumen("No existe este carro en el catalogo");
        } else if (!this.carroControl.carAvailable(l.getObj().getCarroid().getId())) {
            dtoresumen = new DTOresumen("No existen unidades de este carro disponibles en el catalogo");
        } else {
            Query query;
            query = (this.rentaControl.getEntityManager().createNativeQuery("SELECT * FROM RENTA WHERE ID=?", Renta.class));
            query.setParameter(1, (l.getObj().getRentaid()));
            Renta the_renta = (Renta) (query.getSingleResult());
            //Aqui se mira si existe una renta ya del mismo carro y eso
            Query query2;
            query2 = (this.lineaControl.getEntityManager().createNativeQuery("SELECT * FROM LINEA WHERE CARROID=? AND RENTAID=?", Linea.class));
            query2.setParameter(1, (l.getObj().getCarroid().getId()));
            query2.setParameter(2, l.getObj().getRentaid());
            Query query3=(this.parametroControl.getEntityManager().createNativeQuery("SELECT P.CANTIDAD_CARROS FROM PARAMETRO P, RENTA R WHERE R.ID=? AND R.PARAMETROID=P.ID"));
            query3.setParameter(1, l.getObj().getRentaid());
            int parametro_cars_number=(int)(query3.getSingleResult());
            Query query4=(this.parametroControl.getEntityManager().createNativeQuery("SELECT P.PORCENTAJE FROM PARAMETRO P, RENTA R WHERE R.ID=? AND R.PARAMETROID=P.ID"));
            query4.setParameter(1, l.getObj().getRentaid());
            int porcentaje_descuento_cars=(int)(query4.getSingleResult());
            int cantidad_rentados;
            Linea the_line = null;
            try {
                the_line = (Linea) (query2.getSingleResult());
            } catch (NoResultException ne) {
            }
            if (the_line != null) {
                try {
                    this.lineaControl.updateLine(the_line, the_line.getCantidad() + l.getObj().getCantidad());
                    double total_renta = 0;
                    double total_ingresado = 0;
                    double saldo_vueltos;
                    cantidad_rentados=the_renta.getLineaCollection().size();
                    the_renta = (Renta) (query.getSingleResult());
                    for (Iterator<Linea> it = the_renta.getLineaCollection().iterator(); it.hasNext();) {
                        Linea line = it.next();
                        int tmp=(line.getCantidad() * this.carroControl.consultarPrecioCarro(line.getCarroid().getId()))*((cantidad_rentados%parametro_cars_number)*);
                        total_renta = total_renta + tmp;
                    }
                    for (Iterator<Rentaxbillete> it = the_renta.getRentaxbilleteCollection().iterator(); it.hasNext();) {
                        Rentaxbillete billeteIngresado = it.next();
                        total_ingresado += (billeteIngresado.getCantidad() * billeteIngresado.getDenominacionbillete().getValor());
                    }
                    saldo_vueltos = total_ingresado - total_renta;
                    dtoresumen = new DTOresumen(the_renta.getLineaCollection(), total_renta, total_ingresado, saldo_vueltos);
                    System.out.println(this.consultarCarros());
                } catch (Exception ex) {
                    Logger.getLogger(FacadeOCR.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    double total_renta = 0;
                    double total_ingresado = 0;
                    double saldo_vueltos;
                    Linea linea = l.getObj();
                    linea.setRenta(the_renta);
                    this.lineaControl.create(linea);
                    l.setObj(linea);
                    the_renta.add(linea);
                    cantidad_rentados=the_renta.getLineaCollection().size();
                    for (Iterator<Linea> it = the_renta.getLineaCollection().iterator(); it.hasNext();) {
                        Linea line = it.next();
                        total_renta = total_renta + (line.getCantidad() * line.getCarroid().getPrecio());
                    }
                    for (Iterator<Rentaxbillete> it = the_renta.getRentaxbilleteCollection().iterator(); it.hasNext();) {
                        Rentaxbillete billeteIngresado = it.next();
                        total_ingresado += (billeteIngresado.getCantidad() * billeteIngresado.getDenominacionbillete().getValor());
                    }
                    saldo_vueltos = total_ingresado - total_renta;
                    dtoresumen = new DTOresumen(the_renta.getLineaCollection(), total_renta, total_ingresado, saldo_vueltos);
                } catch (Exception e) {
                    dtoresumen = new DTOresumen("Ha ocurrido un error, por favor intente nuevamente");
                }
            }
        }
        return dtoresumen;
    }

    public void agregarBillete(DTO<Rentaxbillete> dtorxb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
