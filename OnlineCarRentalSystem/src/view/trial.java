/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import NegocioOCR.FacadeOCR;
import EntidadesOCR.Carro;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Linea;
import EntidadesOCR.Renta;
import EntidadesOCR.Rentaxbillete;
import EntidadesOCR.classes.DTO;
import EntidadesOCR.classes.DTOresumen;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class trial {

    private static FacadeOCR focr = new FacadeOCR();
    private static Renta renta_actual;

    public static void llenarDropdownCarros() {
        //Los imprime en vez de llenar algo
        for (Carro c : focr.consultarCarros()) {
            System.out.println(c);
        }
        System.out.println();
    }

    public static void llenarDropdownBilletes() {
        for (Denominacionbillete db : focr.consultarTiposBillete()) {
            System.out.println(db);
        }
        System.out.println();
    }

    public static int cantidadCarros() {
        return focr.consultarCantidadCarros();
    }

    public static int cantidadCarrosDisponibles() {
        return focr.consultarCantidadCarrosDisponibles();
    }

    public static DTOresumen crearRenta() {
        DTO<Renta> dto = new DTO<Renta>();
        DTOresumen dtoresumen = focr.crearRenta(dto);
        renta_actual = dto.getObj();
        return dtoresumen;
    }

    public static boolean imprimirMenu() {
        if (renta_actual != null) {
            System.out.println("La renta actual es " + renta_actual.toString());
            System.out.println(renta_actual.getLineaCollection());
            System.out.println("2. Aniadir carro");
            System.out.println("3. Eliminar linea");
            System.out.println("4. Introducir billete");
        } else {
            System.out.println("1. Crear renta");
        }
        Scanner sc = new Scanner(System.in);
        String entrada = sc.next();
        Integer entrada1, entrada2, entrada3;
        entrada1 = Integer.valueOf(entrada);
        switch (entrada1) {
            case 1: {
                System.out.println(crearRenta());
                break;
            }
            case 2: {
                DTO<Linea> l = new DTO<>();
                System.out.println("La lista de carros:");
                llenarDropdownCarros();
                System.out.print("Seleccione carro: ");
                entrada = sc.next();
                entrada2 = Integer.valueOf(entrada);
                System.out.print("Seleccione cantidad: ");
                entrada = sc.next();
                entrada3 = Integer.valueOf(entrada);
                Linea the_line = new Linea(focr.consultarCarro(entrada2), renta_actual.getId(), entrada3);
                the_line.setRenta(renta_actual);
                l.setObj(the_line);
                System.out.println(focr.agregarLinea(l, entrada3));
                break;
            }
            case 3: {
                DTO<Linea> l = new DTO<>();
                System.out.println("La lista de lineas:");
                for (Linea c : renta_actual.getLineaCollection()) {
                    System.out.println(c);
                }
                System.out.println();
                System.out.print("Seleccione linea: ");
                entrada = sc.next();
                entrada2 = Integer.valueOf(entrada);
                Linea the_line = renta_actual.getLineaCollection().get(entrada2 - 1);
                l.setObj(the_line);
                System.out.println(focr.eliminarLinea(l));
                break;
            }
            case 4: {
                DTO<Rentaxbillete> l = new DTO<>();
                System.out.println("La lista de denominaciones:");
                llenarDropdownBilletes();
                System.out.print("Seleccione denominacion: ");
                entrada = sc.next();
                entrada2 = Integer.valueOf(entrada);
                System.out.print("Seleccione cantidad: ");
                entrada = sc.next();
                entrada3 = Integer.valueOf(entrada);
                System.out.println("El valor de entrada2 es: " + entrada2);
                Rentaxbillete the_line = new Rentaxbillete(renta_actual.getId(), entrada2, entrada3);
                the_line.setRenta(renta_actual);
                the_line.setDenominacionbillete(focr.getDenominacion_billeteControl().getEntityManager().find(Denominacionbillete.class, entrada2));
                l.setObj(the_line);
                System.out.println("El valor del ID es: " + the_line.getDenominacionbilleteid());
                System.out.println(focr.agregarBillete(l));
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        boolean that = true;
        while (that) {
            that = imprimirMenu();
        }
    }
}
