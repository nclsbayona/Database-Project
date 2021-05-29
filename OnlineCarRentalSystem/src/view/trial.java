/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import NegocioOCR.FacadeOCR;
import EntidadesOCR.Carro;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Renta;
import EntidadesOCR.classes.DTO;

/**
 *
 * @author Administrator
 */
public class trial {
    public static void main(String[] args) {
        FacadeOCR focr=new FacadeOCR();
        DTO<Renta> dto=new DTO<>(new Renta());
        Renta renta;
        for (Carro c:focr.consultarCarros())
                System.out.println(c);
        System.out.println();
        for (Denominacionbillete db:focr.consultarTiposBillete())
                System.out.println(db);
        System.out.println(focr.consultarCantidadCarros()+" -- "+focr.consultarCantidadCarrosDisponibles());
        System.out.println(focr.crearRenta(dto));
        renta=dto.getObj();
        System.out.println(renta);
    }
}
