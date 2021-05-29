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
import EntidadesOCR.classes.DTO;
import EntidadesOCR.classes.DTOresumen;
import NegocioOCR.FacadeOCR;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class ControlEventosOCR{
    
    private Renta rentaActual;
    private FacadeOCR facadeocr=new FacadeOCR();
    
    @FXML // fx:id="mainWindow"
    private AnchorPane mainWindow;
    
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

    @FXML
    void aniadir_carro(ActionEvent event) {
        Carro c=this.carros_list_view.getSelectionModel().getSelectedItem();
        if (c==null)
          this.createNewStageError("Seleccione un carro, no sea imbecil");
        else if (c.getUnidadesdisponibles()==0)
            this.createNewStageError("No hay unidades disponibles, por favor lea");
        else{
            Linea l=new Linea(c, this.rentaActual.getId(), 1);
            DTO <Linea> dtol=new DTO<>(l);
            this.facadeocr.agregarLinea(dtol);
        }
        System.out.println(this.rentaActual);
        this.updateCarList();
    }

    @FXML
    void aniadir_billete(ActionEvent event) {
        Denominacionbillete db=this.tipo_billete_list_view.getSelectionModel().getSelectedItem();
        this.updateBilleteDenominacionList();
    }

    @FXML
    void crear_renta(ActionEvent event) {
        DTO<Renta> dtorenta=new DTO<>();
        DTOresumen dtoresumen=this.facadeocr.crearRenta(dtorenta);
        if (dtoresumen.getMensaje()!=null)
            this.createNewStageError(dtoresumen.getMensaje());
        this.rentaActual=dtorenta.getObj();
        this.renta_actual_txt.setText(this.rentaActual.toString());
        this.updateCarList();
        this.updateBilleteDenominacionList();
    }
    
    private void updateCarList(){
        this.carros_list_view.setItems(FXCollections.observableList(this.facadeocr.consultarCarros()));
    }
    
    private void updateBilleteDenominacionList(){
        this.tipo_billete_list_view.setItems(FXCollections.observableList(this.facadeocr.consultarTiposBillete()));
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
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainWindow != null : "fx:id=\"mainWindow\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert crear_renta_btn != null : "fx:id=\"crear_renta_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert renta_actual_txt != null : "fx:id=\"renta_actual_txt\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert carros_list_view != null : "fx:id=\"carros_list_view\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert tipo_billete_list_view != null : "fx:id=\"tipo_billete_list_view\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert aniadir_carro_btn != null : "fx:id=\"aniadir_carro_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
        assert aniadir_tipo_billete_btn != null : "fx:id=\"aniadir_tipo_billete_btn\" was not injected: check your FXML file 'PantallaPrincipal.fxml'.";
    }
}
