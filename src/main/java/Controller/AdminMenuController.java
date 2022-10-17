package Controller;

import Model.StudentEntity;
import Session.UserSession;
import Util.NewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    private TableView<StudentEntity> tableView;

    @FXML
    private AnchorPane scenePane;

    @FXML
    private Label adminLabel;

    private ObservableList workers;

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminLabel.setText(UserSession.getInstance().getStudent().getStudentsurname());
        updateTable();
        setupTable();

    }

    public void addUser(ActionEvent event) throws IOException {
        NewWindow.setNewWindow("addUserMenu.fxml", event);
    }

    public void checkLogs(ActionEvent event) throws IOException {
        NewWindow.setNewWindow("checkLogsMenu.fxml", event);
    }

    public void logout(ActionEvent event){
        NewWindow.logout(scenePane);
    }

    private void updateTable() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT w FROM StudentEntity w WHERE w.usertype <> 1 ");
        List results = q.getResultList();
        if (workers == null) {
            workers = FXCollections.observableArrayList(results);
        } else {
            workers.clear();
            workers.addAll(results);
        }
        em.close();
    }

    private void setupTable() {
        TableColumn<StudentEntity, String> fTableColumn = new TableColumn<>();
        fTableColumn.setText("Фамилия");
        fTableColumn.setMinWidth(80);
        fTableColumn.setCellValueFactory(new PropertyValueFactory<>("f"));

        TableColumn<StudentEntity, String> iTableColumn = new TableColumn<>();
        iTableColumn.setText("Имя");
        iTableColumn.setMinWidth(93);
        iTableColumn.setCellValueFactory(new PropertyValueFactory<>("i"));

        TableColumn<StudentEntity, String> oTableColumn = new TableColumn<>();
        oTableColumn.setText("Отчество");
        oTableColumn.setMinWidth(90);
        oTableColumn.setCellValueFactory(new PropertyValueFactory<>("o"));

        TableColumn<StudentEntity, String> roleTableColumn = new TableColumn<>();
        roleTableColumn.setText("Статус");
        roleTableColumn.setMinWidth(70);
        roleTableColumn.setCellValueFactory(w -> {
            if (w.getValue() != null) {
                String result = null;
                if (w.getValue().getUsertype() == 2) {
                    result = "Менеджер";
                } else if (w.getValue().getUsertype() == 3){
                    result = "Работник";
                }
                return new SimpleStringProperty(result);
            } else {
                return new SimpleStringProperty("<no name>");
            }
        });

        TableColumn<StudentEntity, Double> salaryTableColumn = new TableColumn<>();
        salaryTableColumn.setText("Оценка");
        salaryTableColumn.setMinWidth(40);
        salaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));



        tableView.getColumns().addAll(fTableColumn, iTableColumn, oTableColumn, roleTableColumn, salaryTableColumn);
        tableView.setItems(workers);


    }

}