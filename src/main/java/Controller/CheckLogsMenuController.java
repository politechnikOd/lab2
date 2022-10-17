package Controller;

import Model.LogEntity;
import Model.WorkerEntity;
import Util.NewWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CheckLogsMenuController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private ChoiceBox<String> choiceBox;


    private ObservableList<LogEntity> logs;

    private Boolean isAlreadyUsed = false;

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(getWorkerList());
        choiceBox.setOnAction(this::getChosenWorker);
    }

    public void back(ActionEvent event) throws IOException {
        ENTITY_MANAGER_FACTORY.close();
        NewWindow.setNewWindow("adminMenu.fxml", event);
    }

    private List<String> getWorkerList(){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT w FROM WorkerEntity w WHERE w.usertype <> 1");
        List<WorkerEntity> workers = q.getResultList();
        List<String> workerNames = new ArrayList<>();
        for (WorkerEntity w: workers) {
            workerNames.add(w.toString());
        }
        em.close();
        return workerNames;
    }

    private void getChosenWorker(ActionEvent event){
        String worker = choiceBox.getValue();
        updateTable(getChosenWorkerF(worker), getChosenWorkerI(worker));
        setupTable();
        isAlreadyUsed = true;
    }

    private void updateTable(String f, String i){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT l FROM LogEntity l " +
                "INNER JOIN WorkerEntity w on l.workerByIdWorker.idWorker = w.idWorker " +
                "WHERE w.f=:f and  w.i=:i").setParameter("f", f).setParameter("i", i);
        List results = q.getResultList();
        if(logs == null){
            logs = FXCollections.observableArrayList(results);
        }
        else{
            logs.clear();
            logs.addAll(results);
        }
        em.close();
    }

    private void setupTable(){
        if(!isAlreadyUsed) {

            TableColumn<LogEntity, Timestamp> dateTableColumn = new TableColumn<>();
            dateTableColumn.setText("Время действия");
            dateTableColumn.setMinWidth(10);
            dateTableColumn.setPrefWidth(140);
            dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

            TableColumn<LogEntity, String> activityTableColumn = new TableColumn<>();
            activityTableColumn.setText("Описание действия");
            activityTableColumn.setMinWidth(10);
            activityTableColumn.setPrefWidth(382);
            activityTableColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));

            tableView.getColumns().addAll(dateTableColumn, activityTableColumn);
        }
        tableView.setItems(logs);
    }

    private String getChosenWorkerF(String fi){
        return fi.substring(0, fi.indexOf(" "));
    }

    private String getChosenWorkerI(String fi){
        return fi.substring(fi.indexOf(" ") + 1);
    }
}
