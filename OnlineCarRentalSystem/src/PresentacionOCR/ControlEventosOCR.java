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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class ControlEventosOCR {

    private Renta rentaActual;
    private FacadeOCR facadeocr = new FacadeOCR();

    @FXML // fx:id="mainWindow"
    private AnchorPane mainWindow;

    @FXML // fx:id="dtoresumen_pane"
    private GridPane dtoresumen_pane; // Value injected by FXMLLoader

    @FXML // fx:id="crear_renta_btn"
    private Button crear_renta_btn; // Value injected by FXMLLoader

    @FXML // fx:id="renta_actual_txt"
    private Text renta_actual_txt; // Value injected by FXMLLoader

    @FXML // fx:id="aniadir_carro_btn"
    private Button aniadir_carro_btn; // Value injected by FXMLLoader

    @FXML // fx:id="aniadir_tipo_billete_btn"
    private Button aniadir_tipo_billete_btn; // Value injected by FXMLLoader

    @FXML // fx:id="carros_list_view"
    private ListView<Carro> carros_list_view; // Value injected by FXMLLoader

    @FXML // fx:id="tipo_billete_list_view"
    private ListView<Denominacionbillete> tipo_billete_list_view; // Value injected by FXMLLoader

    @FXML // fx:id="linea_collection_list_view"
    private ListView<Linea> linea_collection_list_view; // Value injected by FXMLLoader

    @FXML // fx:id="total_de_la_renta_txt"
    private Text total_de_la_renta_txt; // Value injected by FXMLLoader

    @FXML // fx:id="saldo_ingresados_txt"
    private Text saldo_ingresados_txt; // Value injected by FXMLLoader

    @FXML // fx:id="cantidad_vueltos_txt"
    private Text cantidad_vueltos_txt; // Value injected by FXMLLoader

    @FXML // fx:id="close_resumen_btn"
    private Button close_resumen_btn; // Value injected by FXMLLoader

    @FXML
    void aniadir_carro(ActionEvent event) {

        Carro c = this.carros_list_view.getSelectionModel().getSelectedItem();
        if (c == null) {
            this.createNewStageError("Seleccione un carro, no sea imbecil");
        } else if (c.getUnidadesdisponibles() == 0) {
            this.createNewStageError("No hay unidades disponibles, por favor lea");
        } else {
            Linea l = new Linea(c, this.rentaActual.getId(), 1);
            DTO<Linea> dtol = new DTO<>(l);
            DTOresumen dtoresumen = this.facadeocr.agregarLinea(dtol);
            this.createNewStageDone("Exito!");
            this.crearDtoResumenReview(dtoresumen);
        }
        this.updateCarList();
    }

    @FXML
    void aniadir_billete(ActionEvent event) {
        Denominacionbillete db = this.tipo_billete_list_view.getSelectionModel().getSelectedItem();
        if (db == null) {
            this.createNewStageError("Seleccione una denominacion de billete, no sea imbecil");
        } else {
            Rentaxbillete rxb = new Rentaxbillete(this.rentaActual.getId(), db.getId());
            DTO<Rentaxbillete> dtorxb = new DTO<>(rxb);
            this.facadeocr.agregarBillete(dtorxb);
        }
        System.out.println(this.rentaActual);
        this.updateBilleteDenominacionList();
    }

    @FXML
    void crear_renta(ActionEvent event) {
        DTO<Renta> dtorenta = new DTO<>();
        DTOresumen dtoresumen = this.facadeocr.crearRenta(dtorenta);
        if (dtoresumen.getMensaje() != null) {
            this.createNewStageError(dtoresumen.getMensaje());
        } else {
            this.crearDtoResumenReview(dtoresumen);
            this.rentaActual = dtorenta.getObj();
            this.updateCarList();
            this.updateBilleteDenominacionList();
        }
    }

    private void updateCarList() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.carros_list_view.setItems(FXCollections.observableList(this.facadeocr.consultarCarros()));
        this.renta_actual_txt.setText(this.rentaActual.toString());
    }

    private void updateBilleteDenominacionList() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlEventosOCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.tipo_billete_list_view.setItems(FXCollections.observableList(this.facadeocr.consultarTiposBillete()));
        this.renta_actual_txt.setText(this.rentaActual.toString());
    }

    private void createNewStageError(String msg) {
        Stage stage = (Stage) this.mainWindow.getScene().getWindow();
        AlertType alert = AlertType.ERROR;
        Alert alerta = new Alert(alert, "");
        alerta.initModality(Modality.APPLICATION_MODAL);
        alerta.initOwner(stage);
        alerta.getDialogPane().setContentText(msg);
        alerta.getDialogPane().setHeaderText("Error!");
        alerta.showAndWait();
    }

    private void createNewStageDone(String msg) {
        Stage stage = (Stage) this.mainWindow.getScene().getWindow();
        AlertType alert = AlertType.INFORMATION;
        Alert alerta = new Alert(alert, "");
        alerta.initModality(Modality.APPLICATION_MODAL);
        alerta.initOwner(stage);
        alerta.getDialogPane().setContentText(msg);
        alerta.getDialogPane().setHeaderText("Done!");
        alerta.showAndWait();
    }

    private void activateDTOPane() {
        this.dtoresumen_pane.setVisible(!(this.dtoresumen_pane.isVisible()));
        this.dtoresumen_pane.setDisable(!(this.dtoresumen_pane.isDisabled()));
    }

    private void crearDtoResumenReview(DTOresumen dtoresumen) {
        if (dtoresumen.getLineas() == null) {
            dtoresumen.setLineas(new ArrayList<>());
        }
        this.linea_collection_list_view.setItems(FXCollections.observableList(dtoresumen.getLineas()));
        this.saldo_ingresados_txt.setText(String.valueOf(dtoresumen.getSaldo_ingresados()));
        this.total_de_la_renta_txt.setText(String.valueOf(dtoresumen.getTotal_renta()));
        this.cantidad_vueltos_txt.setText(String.valueOf(dtoresumen.getCantidad_vueltos()));
        this.activateDTOPane();
    }

    @FXML
    void close_resumen(ActionEvent event) {
        this.activateDTOPane();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainWindow != null : "fx:id=\"mainWindow\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert crear_renta_btn != null : "fx:id=\"crear_renta_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert renta_actual_txt != null : "fx:id=\"renta_actual_txt\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert carros_list_view != null : "fx:id=\"carros_list_view\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert tipo_billete_list_view != null : "fx:id=\"tipo_billete_list_view\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert aniadir_carro_btn != null : "fx:id=\"aniadir_carro_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert aniadir_tipo_billete_btn != null : "fx:id=\"aniadir_tipo_billete_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert linea_collection_list_view != null : "fx:id=\"linea_collection_list_view\" was not injected: check your FXML file 'DTOResumenScreen.fxml'.";
        assert total_de_la_renta_txt != null : "fx:id=\"total_de_la_renta_txt\" was not injected: check your FXML file 'DTOResumenScreen.fxml'.";
        assert saldo_ingresados_txt != null : "fx:id=\"saldo_ingresados_txt\" was not injected: check your FXML file 'DTOResumenScreen.fxml'.";
        assert cantidad_vueltos_txt != null : "fx:id=\"cantidad_vueltos_txt\" was not injected: check your FXML file 'DTOResumenScreen.fxml'.";
        assert dtoresumen_pane != null : "fx:id=\"dtoresumen_pane\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert close_resumen_btn != null : "fx:id=\"close_resumen_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
    }
}
