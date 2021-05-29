/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NegocioOCR;
import EntidadesOCR.Carro;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Parametro;
import EntidadesOCR.Renta;
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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 *
 * @author Administrator
 */
public class FacadeOCR {
    
    private CarroJpaController carroControl=new CarroJpaController();
    private DenominacionbilleteJpaController denominacion_billeteControl=new DenominacionbilleteJpaController();
    private LineaJpaController lineaControl=new LineaJpaController();
    private ParametroJpaController parametroControl=new ParametroJpaController();
    private RentaJpaController rentaControl=new RentaJpaController();

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
    private RentaxbilleteJpaController rentaXbilleteControl=new RentaxbilleteJpaController();
    
    public List<Carro> consultarCarros(){
        List<Carro> carros=this.carroControl.findCarroEntities();
        return carros;
    }
    
    public List<Carro> consultarCarrosDisponibles(){
        List<Carro> carros=this.consultarCarros();
        List<Carro> carros_disponibles=new ArrayList<>();
        for (Carro c:carros)
            if (c.getUnidadesdisponibles()==0)
                carros_disponibles.add(c);
        return carros_disponibles;
    }
    
    public List<Denominacionbillete> consultarTiposBillete(){
        DenominacionbilleteJpaController dbjpa=new DenominacionbilleteJpaController();
        List<Denominacionbillete> tipos=dbjpa.findDenominacionbilleteEntities();
        return tipos;
    }
    
    public Integer consultarCantidadCarros(){
        Integer cant;
        EntityManager em=this.carroControl.getEntityManager();
        Query query=em.createNativeQuery("SELECT COUNT(*) FROM CARRO");
        System.out.print(query);
        cant=(Integer)query.getSingleResult();
        return cant;
    }
    
    public Integer consultarCantidadCarrosDisponibles(){
        Integer cant;
        EntityManager em=this.carroControl.getEntityManager();
        Query query=em.createNativeQuery("SELECT COUNT(*) FROM CARRO WHERE UNIDADESDISPONIBLES>0");
        cant=(Integer)query.getSingleResult();
        return cant;
    }
    
    public DTOresumen crearRenta(DTO<Renta> objetoRenta){
        DTOresumen dtoresumen;
        if (this.consultarCantidadCarros()==0)
            dtoresumen=new DTOresumen("No existen carros en el sistema!");
        else if (this.consultarCantidadCarrosDisponibles()==0)
            dtoresumen=new DTOresumen("No existen carros disponibles!");
        else{
            int parametroID=1;
            Query query=(this.parametroControl.getEntityManager().createNativeQuery("SELECT * FROM PARAMETRO WHERE ID=?", Parametro.class));
            int numero_renta=((int)(this.rentaControl.getEntityManager().createNativeQuery("SELECT COUNT(*) FROM RENTA").getSingleResult()))+1;
            query.setParameter(1, parametroID);
            Parametro parametro=((Parametro)(query.getSingleResult()));
            Renta renta=new Renta(numero_renta, new Date(), parametro);
            this.rentaControl.create(renta);
            objetoRenta.setObj(renta);
            dtoresumen=new DTOresumen(null, 0, 0, 0);
        }
        return dtoresumen;
    }
}
