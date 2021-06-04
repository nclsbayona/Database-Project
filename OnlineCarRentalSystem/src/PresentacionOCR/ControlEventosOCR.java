/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentacionOCR;

import EntidadesOCR.Carro;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Linea;
import EntidadesOCR.Renta;
import EntidadesOCR.Rentaxbillete;
import EntidadesOCR.classes.DTO;
import EntidadesOCR.classes.DTOresumen;
import NegocioOCR.FacadeOCR;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static view.trial.llenarDropdownCarros;

/**
 *
 * @author Administrator
 */
public class ControlEventosOCR {
    
    private static Renta rentaActual;
    private static FacadeOCR facadeocr = new FacadeOCR();
    
    @FXML
    private Button nuevaRenta;
    @FXML
    private ComboBox<Carro> showCars;
    @FXML
    private ComboBox<Denominacionbillete> denominacion;
    @FXML
    private Button addLine;
    @FXML
    private TextField cantidadLine;
    @FXML
    private ListView<Linea> listLine;
    @FXML
    private Button deleteLine;
    @FXML
    private TextField cantidadDenominaciones;
    @FXML
    private Button agregar_billete;
    @FXML
    private Label saldo_ingresado;
    @FXML
    private TextField rentaid_texto;
    @FXML
    private Button buscarRenta;
    @FXML
    private ListView<DTO<Carro>> listacumulados;
    
    @FXML
    private Button consultaracumulados_boton;
    
    @FXML
    void consultarAcumulados(ActionEvent event) {
        listacumulados.setItems(null);
        List<DTO<Carro>> ldto = facadeocr.consultarAcumulados();
        listacumulados.setItems(FXCollections.observableList(ldto));
        System.out.println(ldto);
        System.out.println(ldto.size());
    }
    
    @FXML
    void addLine(ActionEvent event) {
        DTO<Linea> l = new DTO<Linea>();
        Carro c = this.showCars.getValue();
        Linea the_line = new Linea(c, rentaActual.getId(), Integer.parseInt(cantidadLine.getText()));
        the_line.setRenta(rentaActual);
        l.setObj(the_line);
        facadeocr.agregarLinea(l, Integer.parseInt(cantidadLine.getText()));
        System.out.println("El dto es: " + l + " la coleccion de lineas es: " + rentaActual.getLineaCollection());
        this.showLine();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.showCars.setItems(null);
        this.showCars.setItems(FXCollections.observableList(facadeocr.consultarCarros()));
        System.out.println(facadeocr.consultarCarros());
        this.showCar();
    }
    
    @FXML
    void deleteLine(ActionEvent event) {
        this.facadeocr.eliminarLinea(new DTO<Linea>(this.listLine.getSelectionModel().getSelectedItem()));
        this.showLine();
        this.showCar();
    }
    
    @FXML
    void agregarBillete(ActionEvent event) {
        DTO<Rentaxbillete> l = new DTO<>();
        Carro c = this.showCars.getValue();
        Rentaxbillete the_line = new Rentaxbillete(rentaActual.getId(), this.denominacion.getSelectionModel().getSelectedItem().getId(), Integer.parseInt(cantidadDenominaciones.getText()));
        the_line.setRenta(rentaActual);
        the_line.setDenominacionbillete(facadeocr.getDenominacion_billeteControl().getEntityManager().find(Denominacionbillete.class, this.denominacion.getSelectionModel().getSelectedItem().getId()));
        l.setObj(the_line);
        DTOresumen dtoresumen = facadeocr.agregarBillete(l);
        this.showLine();
        this.showCar();
        this.saldo_ingresado.setText(String.valueOf(dtoresumen.getSaldo_ingresados()));
    }
    
    @FXML
    private Button terminar_renta;
    
    @FXML
    private Label vueltos_texto;
    
    @FXML
    void terminarRenta(ActionEvent event) {
        DTOresumen dtoresumen = facadeocr.terminarRenta(rentaActual);
        if (dtoresumen.getMensaje() != null) {
            this.vueltos_texto.setText(String.valueOf(dtoresumen.getMensaje()));
        } else {
            this.vueltos_texto.setText(String.valueOf(dtoresumen.getCantidad_vueltos()));
        }
        rentaActual = null;
    }
    
    @FXML
    void nuevaRenta(ActionEvent event) {
        DTO<Renta> dto = new DTO<Renta>();
        DTOresumen dtoresumen = facadeocr.crearRenta(dto);
        rentaActual = dto.getObj();
        this.showLine();
        this.showCar();
        this.showDenominacion();
    }
    
    private void showCar() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.showCars.setItems(null);
        this.showCars.setItems(FXCollections.observableList(facadeocr.consultarCarros()));
        System.out.println(facadeocr.consultarCarros());
    }
    
    private void showDenominacion() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.denominacion.setItems(null);
        this.denominacion.setItems(FXCollections.observableList(facadeocr.consultarTiposBillete()));
        System.out.println(facadeocr.consultarTiposBillete());
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert nuevaRenta != null : "fx:id=\"nuevaRenta\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert showCars != null : "fx:id=\"showCars\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert denominacion != null : "fx:id=\"denominacion\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert addLine != null : "fx:id=\"addLine\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert cantidadLine != null : "fx:id=\"cantidadLine\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert listLine != null : "fx:id=\"listLine\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert deleteLine != null : "fx:id=\"deleteLine\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
    }
    
    private void showLine() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.listLine.setItems(null);
        this.listLine.setItems(FXCollections.observableList(rentaActual.getLineaCollection()));
        System.out.println(rentaActual.getLineaCollection());
    }
    
    @FXML
    void buscarUnaRenta(ActionEvent event) {
        DTO<Renta> dtorenta = new DTO<>();
        DTOresumen dtoresumen = facadeocr.consultarRenta(dtorenta, Integer.valueOf(this.rentaid_texto.getText()));
        this.rentaid_texto.setText(dtorenta.getObj().toString());
    }
}
