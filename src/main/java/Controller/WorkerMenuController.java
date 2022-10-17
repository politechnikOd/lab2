package Controller;

import CellFactory.ThingServiceCellFactory;
import Model.*;
import Session.UserSession;
import Util.GetListForChoiceBox;
import Util.NewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class WorkerMenuController implements Initializable {

    @FXML
    private Label workerLabel;

    @FXML
    private ListView<ThingserviceEntity> servicesListView;

    @FXML
    private Label infoLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label materialLabel;

    @FXML
    private Label damageLabel;

    @FXML
    private Circle coloredCircle;

    @FXML
    private ChoiceBox<String> materialChoiceBox;

    @FXML
    private Spinner<Double> quantitySpinner;

    @FXML
    private AnchorPane scenePane;

    private ThingserviceEntity currentService;


    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        workerLabel.setText(UserSession.getInstance().getWorker().getF());
        materialChoiceBox.getItems().addAll(GetListForChoiceBox.getMaterialsList());
        servicesListView.getItems().addAll(getThingServiceList());
        servicesListView.setCellFactory(new ThingServiceCellFactory());

        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 99999);
        valueFactory.setValue(0.0);
        quantitySpinner.setValueFactory(valueFactory);

        servicesListView.getSelectionModel().selectedItemProperty().addListener((observableValue, thingserviceEntity, t1) -> {
            currentService = servicesListView.getSelectionModel().getSelectedItem();

            infoLabel.setText(getInfo(currentService));
            nameLabel.setText(getName(currentService));
            typeLabel.setText(getType(currentService));
            materialLabel.setText(getMaterial(currentService));
            damageLabel.setText(getDamage(currentService));
            coloredCircle.setVisible(true);
            coloredCircle.setFill(getColor(currentService));
            coloredCircle.setStroke(getColor(currentService));
        });
    }

    public void completeService(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение добавления услуги");
        alert.setContentText("Добавить услугу?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            setExcecuted(getChosenMaterialEntity(getChosenMaterial()), quantitySpinner.getValue());
            addCompletedServiceLog();
            NewWindow.setNewWindow("workerMenu.fxml", event);
        }

    }

    public void logout(ActionEvent event){
        addWorkerLogoutLog();
        NewWindow.logout(scenePane);
    }

    private void setExcecuted(MaterialEntity material, Double quantity) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            UsedmaterialEntity usedMaterial = new UsedmaterialEntity();
            usedMaterial.setMaterialByIdMaterial(material);
            usedMaterial.setOrderrByIdOrder(getOrder(em, currentService).getSingleResult());
            usedMaterial.setWorkerByIdWorker(UserSession.getInstance().getWorker());
            usedMaterial.setQuantity(quantity);
            em.persist(usedMaterial);

            Query q = em.createQuery("UPDATE MaterialEntity m SET m.quantity=:quantity WHERE m.idMaterial =:idMaterial");
            q.setParameter("quantity", em.createQuery("SELECT m.quantity FROM MaterialEntity m WHERE m.idMaterial=:idMaterial", Double.class).
                    setParameter("idMaterial", material.getIdMaterial()).getSingleResult() - quantity).setParameter("idMaterial", material.getIdMaterial());
            q.executeUpdate();

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            Query q2 = em.createQuery("UPDATE WorkerthingserviceEntity w SET w.executdate=:executDate WHERE w.workerByIdWorker.idWorker=:idWorker AND w.thingserviceByIdThingService.idThingService=:idThingService");
            q2.setParameter("executDate", sqlDate).setParameter("idWorker", UserSession.getInstance().getWorker().getIdWorker()).setParameter("idThingService", currentService.getIdThingService());
            q2.executeUpdate();

            et.commit();

            if (isOrderCompleted(em)) {
                et.begin();
                Query q3 = em.createQuery("UPDATE OrderrEntity o SET o.execdate=:executDate WHERE o.idOrder=:idOrder").
                        setParameter("idOrder", em.createQuery("SELECT o FROM ThingserviceEntity ts " +
                                        "INNER JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                                        "LEFT JOIN OrderrEntity o ON t.orderrByIdOrder.idOrder = o.idOrder " +
                                        "WHERE ts.idThingService =:idThingService", OrderrEntity.class).
                                setParameter("idThingService", currentService.getIdThingService()).getSingleResult().getIdOrder()).setParameter("executDate", sqlDate);
                q3.executeUpdate();
                et.commit();
            }
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    private void addCompletedServiceLog() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            LogEntity log = new LogEntity();
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            log.setActivity("Выполнил(а) " + currentService.getServiceByIdService().getName() +
                    " для предмета " + currentService.getThingByIdThing().getName() +
                    " из заказа с id " + currentService.getThingByIdThing().getOrderrByIdOrder().getIdOrder() +
                    " использовав материал " + getChosenMaterial() +
                    " в количестве " + quantitySpinner.getValue());
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

    private void addWorkerLogoutLog() {
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

    private boolean isOrderCompleted(EntityManager em) {
        Query q = em.createQuery("SELECT wts FROM WorkerthingserviceEntity wts " +
                        "INNER JOIN ThingserviceEntity ts ON wts.thingserviceByIdThingService.idThingService = ts.idThingService " +
                        "LEFT JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                        "LEFT JOIN OrderrEntity o ON t.orderrByIdOrder.idOrder = o.idOrder " +
                        "WHERE o.idOrder=:idOrder AND wts.executdate IS NULL").
                setParameter("idOrder", getOrder(em, currentService).getSingleResult().getIdOrder());
        return q.getResultList() == null;
    }

    private String getInfo(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String result = getOrder(em, thingService).getSingleResult().getF() + " " + getOrder(em, thingService).getSingleResult().getI() + " " + getOrder(em, thingService).getSingleResult().getTel() + " " + getOrder(em, thingService).getSingleResult().getOrderdate();
        em.close();
        return result;
    }

    private TypedQuery<OrderrEntity> getOrder(EntityManager em, ThingserviceEntity thingService) {
        return em.createQuery("SELECT o FROM ThingserviceEntity ts " +
                        "INNER JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                        "LEFT JOIN OrderrEntity o ON t.orderrByIdOrder.idOrder = o.idOrder " +
                        "WHERE ts.idThingService=:idThingService", OrderrEntity.class).
                setParameter("idThingService", thingService.getIdThingService());

    }

    private String getName(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String result = getQuery(em, thingService).getSingleResult().getName();
        em.close();
        return result;
    }

    private String getType(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String result = getQuery(em, thingService).getSingleResult().getType();
        em.close();
        return result;
    }

    private String getMaterial(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String result = getQuery(em, thingService).getSingleResult().getMaterial();
        em.close();
        return result;
    }

    private String getDamage(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String result = getQuery(em, thingService).getSingleResult().getDamage();
        em.close();
        return result;
    }

    private Color getColor(ThingserviceEntity thingService) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Color result = Color.valueOf(getQuery(em, thingService).getSingleResult().getColor());
        em.close();
        return result;
    }

    private TypedQuery<ThingEntity> getQuery(EntityManager em, ThingserviceEntity thingService) {
        return em.createQuery("SELECT t FROM ThingserviceEntity ts " +
                "INNER JOIN ThingEntity t ON ts.thingByIdThing.idThing = t.idThing " +
                "WHERE ts.idThingService =:idThingService", ThingEntity.class).setParameter("idThingService", thingService.getIdThingService());
    }

    private ArrayList<ThingserviceEntity> getThingServiceList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<ThingserviceEntity> tq = em.createQuery("SELECT ts FROM ThingserviceEntity ts INNER JOIN WorkerthingserviceEntity wts ON ts.idThingService = wts.thingserviceByIdThingService.idThingService WHERE wts.workerByIdWorker.idWorker=:idWorker AND wts.executdate IS NULL", ThingserviceEntity.class);
        tq.setParameter("idWorker", UserSession.getInstance().getWorker().getIdWorker());
        ArrayList<ThingserviceEntity> result = new ArrayList<>(tq.getResultList());
        em.close();
        return result;
    }

    private MaterialEntity getChosenMaterialEntity(String material) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<MaterialEntity> tq = em.createQuery("SELECT m FROM MaterialEntity m WHERE m.name=:name", MaterialEntity.class);
        MaterialEntity result = tq.setParameter("name", material).getSingleResult();
        em.close();
        return result;
    }

    private String getChosenMaterial() {
        return materialChoiceBox.getValue().substring(0, materialChoiceBox.getValue().indexOf(" "));
    }

}
