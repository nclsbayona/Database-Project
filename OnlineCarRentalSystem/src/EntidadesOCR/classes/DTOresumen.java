/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesOCR.classes;

import java.util.Collection;
import EntidadesOCR.Linea;

/**
 *
 * @author Administrator
 */
public class DTOresumen {
    private String mensaje;
    private Collection<Linea> lineas;
    private double total_renta;
    private int saldo_ingresados;
    private int cantidad_vueltos;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Collection<Linea> getLineas() {
        return lineas;
    }

    public void setLineas(Collection<Linea> lineas) {
        this.lineas = lineas;
    }

    public double getTotal_renta() {
        return total_renta;
    }

    public void setTotal_renta(double total_renta) {
        this.total_renta = total_renta;
    }

    public int getSaldo_ingresados() {
        return saldo_ingresados;
    }

    public void setSaldo_ingresados(int saldo_ingresados) {
        this.saldo_ingresados = saldo_ingresados;
    }

    public int getCantidad_vueltos() {
        return cantidad_vueltos;
    }

    public void setCantidad_vueltos(int cantidad_vueltos) {
        this.cantidad_vueltos = cantidad_vueltos;
    }

    @Override
    public String toString() {
        String ret;
        if (this.mensaje!=null)
            ret=this.mensaje+'!';
        else
            ret="DTOresumen{" + "lineas=" + lineas + ", total_renta=" + total_renta + ", saldo_ingresados=" + saldo_ingresados + ", cantidad_vueltos=" + cantidad_vueltos + '}';
        return ret;
    }

    public DTOresumen(Collection<Linea> lineas, double total_renta, int saldo_ingresados, int cantidad_vueltos) {
        this.lineas = lineas;
        this.total_renta = total_renta;
        this.saldo_ingresados = saldo_ingresados;
        this.cantidad_vueltos = cantidad_vueltos;
    }

    public DTOresumen(String mensaje) {
        this.mensaje = mensaje;
    }
    
}
