package Controller;

import Model.*;
import Session.UserSession;
import Util.NewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerMenuController implements Initializable {

    @FXML
    private Label managerLabel;

    @FXML
    private AnchorPane scenePane;

    @FXML
    private TableView<OrderrEntity> ordersTableView;

    @FXML
    private RadioButton completedRadioButton, notRadioButton;

    private ObservableList orders;

    private Boolean isAlreadyUsed = false;

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        managerLabel.setText(UserSession.getInstance().getWorker().getF());

    }

    public void newOrder(ActionEvent event) throws IOException {
        NewWindow.setNewWindow("addOrderMenu.fxml", event);
    }

    public void toMaterials(ActionEvent event) throws IOException {
        NewWindow.setNewWindow("materialsMenu.fxml", event);
    }

    public void setupTable(ActionEvent event) {
        if (completedRadioButton.isSelected()) {
            updateTable(true);
            setupTable();
            isAlreadyUsed = true;
        } else if (notRadioButton.isSelected()) {
            updateTable(false);
            setupTable();
            isAlreadyUsed = true;
        }
    }

    public void logout(ActionEvent event) {
        addManagerLogoutLog();
        NewWindow.logout(scenePane);
    }

    private void addManagerLogoutLog() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            LogEntity log = new LogEntity();
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            log.setActivity("Вышел из системы");
            log.setWorkerByIdWorker(UserSession.getInstance().getWorker());
            em.persist(log);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void updateTable(Boolean isCompleted) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<OrderrEntity> tq;
        if (isCompleted) {
            tq = em.createQuery("SELECT o FROM OrderrEntity o WHERE o.execdate IS NOT NULL ", OrderrEntity.class);
        } else {
            tq = em.createQuery("SELECT o FROM OrderrEntity o WHERE o.execdate IS NULL ", OrderrEntity.class);
        }
        List<OrderrEntity> results = tq.getResultList();
        if (orders == null) {
            orders = FXCollections.observableArrayList(results);
        } else {
            orders.clear();
            orders.addAll(results);
        }
        em.close();
    }

    private void setupTable() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        if (!isAlreadyUsed) {

            TableColumn<OrderrEntity, String> fioTableColumn = new TableColumn<>();
            fioTableColumn.setText("ФИО");
            fioTableColumn.setMinWidth(80);
            fioTableColumn.setCellValueFactory(o -> {
                if (o.getValue() != null) {
                    String result = o.getValue().getF() + " " + o.getValue().getI() + " " + o.getValue().getO();
                    return new SimpleStringProperty(result);
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            });

            TableColumn<OrderrEntity, String> telTableColumn = new TableColumn<>();
            telTableColumn.setText("Телефон");
            telTableColumn.setMinWidth(93);
            telTableColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));

            TableColumn<OrderrEntity, Date> orderDateTableColumn = new TableColumn<>();
            orderDateTableColumn.setText("Дата принятия");
            orderDateTableColumn.setMinWidth(72);
            orderDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderdate"));

            TableColumn<OrderrEntity, Date> endDateTableColumn = new TableColumn<>();
            endDateTableColumn.setText("Дата выполнения");
            endDateTableColumn.setMinWidth(72);
            endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("execdate"));

            TableColumn<OrderrEntity, Double> priceTableColumn = new TableColumn<>();
            priceTableColumn.setText("Стоимость");
            priceTableColumn.setMinWidth(40);
            priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<OrderrEntity, String> servicesTableColumn = new TableColumn<>();
            servicesTableColumn.setText("Услуги");
            servicesTableColumn.setMinWidth(72);
            servicesTableColumn.setCellValueFactory(o -> {
                if (o.getValue() != null) {
                    List<ThingserviceEntity> thingServiceList = getServices(em, o.getValue()).getResultList();
                    StringBuilder sb = new StringBuilder();
                    for (ThingserviceEntity ts : thingServiceList) {
                        sb.append(ts.getServiceByIdService().getName()).append(", ");
                    }
                    return new SimpleStringProperty(sb.toString());
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            });

            TableColumn<OrderrEntity, String> workersTableColumn = new TableColumn<>();
            workersTableColumn.setText("Работники");
            workersTableColumn.setMinWidth(72);
            workersTableColumn.setCellValueFactory(o -> {
                if (o.getValue() != null) {
                    List<WorkerthingserviceEntity> workersList = getWorkers(em, o.getValue()).getResultList();
                    StringBuilder sb = new StringBuilder();
                    for (WorkerthingserviceEntity wts : workersList) {
                        sb.append(wts.getWorkerByIdWorker().getF()).append(", ");
                    }
                    return new SimpleStringProperty(sb.toString());
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            });

            ordersTableView.getColumns().addAll(fioTableColumn, telTableColumn, orderDateTableColumn, endDateTableColumn, priceTableColumn, servicesTableColumn, workersTableColumn);
        }
        ordersTableView.setItems(orders);
    }

    private TypedQuery<ThingserviceEntity> getServices(EntityManager em, OrderrEntity o) {
        return em.createQuery("SELECT ts FROM ThingserviceEntity ts " +
                        "INNER JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                        "LEFT JOIN OrderrEntity o ON t.orderrByIdOrder.idOrder = o.idOrder WHERE o.idOrder=:idOrder", ThingserviceEntity.class).
                setParameter("idOrder", o.getIdOrder());
    }

    private TypedQuery<WorkerthingserviceEntity> getWorkers(EntityManager em, OrderrEntity o) {
        return em.createQuery("SELECT wts FROM WorkerthingserviceEntity wts " +
                        "INNER JOIN ThingserviceEntity ts ON wts.thingserviceByIdThingService.idThingService = ts.idThingService " +
                        "LEFT JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                        "LEFT JOIN OrderrEntity o ON t.orderrByIdOrder.idOrder = o.idOrder WHERE o.idOrder=:idOrder", WorkerthingserviceEntity.class).
                setParameter("idOrder", o.getIdOrder());
    }
}
