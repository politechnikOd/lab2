package Util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NewWindow {
    private static Parent root;
    private static Stage stage;

    public static void setNewWindow(String fxml, ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/FXML/" + fxml).toURI().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public static void logout(AnchorPane pane){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход из системы");
        alert.setHeaderText("Подтверждение выхода из системы");
        alert.setContentText("Выйти из системы?");

        if(alert.showAndWait().get() == ButtonType.OK){
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.close();
        }


    }
}
