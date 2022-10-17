package Controller;

import Model.ThingEntity;
import Session.ThingSession;
import Util.NewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Optional;

public class AddThingController {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField materialTextField;

    @FXML
    private TextField furnitureTextField;

    @FXML
    private TextField damageTextField;

    @FXML
    private ColorPicker colorPicker;


    private String name;
    private String type;
    private String material;
    private String furniture;
    private String damage;
    private String color;

    public void addNewThing(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение продолжения оформления заказа");
        alert.setContentText("Продолжить оформление заказа?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            name = nameTextField.getText();
            type = typeTextField.getText();
            material = materialTextField.getText();
            furniture = furnitureTextField.getText();
            damage = damageTextField.getText();
            color = colorPicker.getValue().toString();
            saveThing(name, type, material, furniture, damage, color);
            NewWindow.setNewWindow("setServiceAndWorkerMenu.fxml", event);
        }
    }

    private void saveThing(String name, String type, String material, String furniture, String damage, String color) {
        ThingEntity thing = new ThingEntity();
        thing.setName(name);
        thing.setType(type);
        thing.setMaterial(material);
        thing.setFurniture(furniture);
        thing.setDamage(damage);
        thing.setColor(color);
        ThingSession.getInstance(thing);
    }


}
