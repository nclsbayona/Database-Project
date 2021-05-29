/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentacionOCR;

import EntidadesOCR.Renta;
import EntidadesOCR.classes.DTO;
import EntidadesOCR.classes.DTOresumen;
import NegocioOCR.FacadeOCR;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML // fx:id="label"
    private Label label; // Value injected by FXMLLoader

    @FXML // fx:id="renta_actual_txt"
    private Text renta_actual_txt; // Value injected by FXMLLoader

    @FXML
    void crear_renta(ActionEvent event) {
        DTO<Renta> dtorenta=new DTO<>();
        DTOresumen dtoresumen=this.facadeocr.crearRenta(dtorenta);
        if (dtoresumen.getMensaje()!=null)
            this.createNewStageError(dtoresumen.getMensaje());
        this.rentaActual=dtorenta.getObj();
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
}
