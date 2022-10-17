package Controller;

import Model.OrderrEntity;
import Session.OrderSession;
import Util.NewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


public class AddOrderController {
    @FXML
    private TextField fTextField;

    @FXML
    private TextField iTextField;

    @FXML
    private TextField oTextField;

    @FXML
    private TextField telTextField;

    @FXML
    private TextField addressTextField;

    private String f;
    private String i;
    private String o;
    private String tel;
    private String address;


    public void addNewOrder(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение продолжения оформления заказа");
        alert.setContentText("Продолжить оформление заказа?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            f = fTextField.getText();
            i = iTextField.getText();
            o = oTextField.getText();
            tel = telTextField.getText();
            address = addressTextField.getText();
            saveOrder(f, i, o, tel, address);
            NewWindow.setNewWindow("addThingMenu.fxml", event);
        }
    }

    public void cancel(ActionEvent event) throws IOException{
        //OrderSession.getInstance().cleanOrderSession();
        NewWindow.setNewWindow("managerMenu.fxml", event);
    }

    private void saveOrder(String f, String i, String o, String tel, String address){
        OrderrEntity order = new OrderrEntity();
        order.setF(f);
        order.setI(i);
        order.setO(o);
        order.setTel(tel);
        order.setAddress(address);
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        order.setOrderdate(sqlDate);
        OrderSession.getInstance(order);
    }


}
