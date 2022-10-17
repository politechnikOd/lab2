package Controller;

import Model.*;
import Session.*;
import Util.NewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SetServiceAndWorkerController implements Initializable {
    @FXML
    private ChoiceBox<String> serviceChoiceBox;

    @FXML
    private ChoiceBox<String> workerChoiceBox;

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceChoiceBox.getItems().addAll(getServiceList());
        workerChoiceBox.getItems().addAll(getWorkerList());
    }

    public void createOrder(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение оформления заказа");
        alert.setContentText("Оформить этот заказ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
            EntityTransaction et = null;
            try {
                et = em.getTransaction();
                et.begin();

                persistWholeOrder(et, em);

                addNewOrderLog(OrderId.getInstance().getOrderId());

                OrderSession.getInstance().cleanOrderSession();
                ThingSession.getInstance().cleanThingSession();
                OrderId.getInstance().cleanOrderId();
                ThingId.getInstance().cleanThingId();

            } catch (Exception e) {
                if (et != null) {
                    et.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
            ENTITY_MANAGER_FACTORY.close();
            NewWindow.setNewWindow("managerMenu.fxml", event);
        }

    }

    public void confirmAndAddThing(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение добавления вещи в заказ");
        alert.setContentText("Добавить вещь в заказ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
            EntityTransaction et = null;
            try {
                et = em.getTransaction();
                et.begin();

                persistWholeOrder(et, em);

                ThingSession.getInstance().cleanThingSession();
                ThingId.getInstance().cleanThingId();

            } catch (Exception e) {
                if (et != null) {
                    et.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
            NewWindow.setNewWindow("addThingMenu.fxml", event);
        }
    }

    public void confirmAndAddService(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение добавления услуги");
        alert.setContentText("Добавить услугу?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
            EntityTransaction et = null;
            try {
                et = em.getTransaction();
                et.begin();

                persistWholeOrder(et, em);



            } catch (Exception e) {
                if (et != null) {
                    et.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
            NewWindow.setNewWindow("setServiceAndWorkerMenu.fxml", event);
        }
    }

    private void persistWholeOrder(EntityTransaction et, EntityManager em) {

        if (OrderSession.getInstance().getOrder() != null) {
            OrderrEntity order = OrderSession.getInstance().getOrder();

            em.persist(order);
            et.commit();

            OrderId.getInstance(order.getIdOrder());
            et.begin();
        }

        if (ThingSession.getInstance().getThing() != null) {
            ThingEntity thing = getThing(ThingSession.getInstance().getThing(), OrderId.getInstance().getOrderId());

            em.persist(thing);
            et.commit();

            ThingId.getInstance(thing.getIdThing());
            et.begin();
        }

        ThingserviceEntity thingService = getThingService(ThingId.getInstance().getThingId());

        em.persist(thingService);
        et.commit();
        et.begin();

        WorkerthingserviceEntity workerThingService = getWorkerThingService(thingService.getIdThingService());

        em.persist(workerThingService);
        et.commit();
        et.begin();

        Query q = em.createQuery("UPDATE OrderrEntity o SET o.price =:price WHERE o.idOrder =:idOrder");
        q.setParameter("price", getPriceSum(em, OrderId.getInstance().getOrderId())).setParameter("idOrder", OrderId.getInstance().getOrderId());
        q.executeUpdate();

        if (OrderSession.getInstance().getOrder() != null) {
            OrderSession.getInstance().cleanOrderSession();
        }
        if (ThingSession.getInstance().getThing() != null) {
            ThingSession.getInstance().cleanThingSession();
        }

        et.commit();
    }


    private List<String> getWorkerList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT w FROM WorkerEntity w WHERE w.usertype = 3");
        List<WorkerEntity> workers = q.getResultList();
        List<String> workerNames = new ArrayList<>();
        for (WorkerEntity w : workers) {
            workerNames.add(w.toString());
        }
        em.close();
        return workerNames;
    }

    private List<String> getServiceList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT s FROM ServiceEntity s");
        List<ServiceEntity> services = q.getResultList();
        List<String> serviceNames = new ArrayList<>();
        for (ServiceEntity s : services) {
            serviceNames.add(s.toString());
        }
        em.close();
        return serviceNames;
    }

    private ThingserviceEntity getThingService(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        ThingserviceEntity thingService = new ThingserviceEntity();

        TypedQuery<ThingEntity> tq1 = em.createQuery("SELECT t FROM ThingEntity t WHERE t.idThing=:id", ThingEntity.class);
        tq1.setParameter("id", id);
        thingService.setThingByIdThing(tq1.getSingleResult());

        TypedQuery<ServiceEntity> tq2 = em.createQuery("SELECT s FROM ServiceEntity s WHERE s.name=:name", ServiceEntity.class);
        tq2.setParameter("name", getChosenService(serviceChoiceBox.getValue()));
        thingService.setServiceByIdService(tq2.getSingleResult());
        em.close();
        return thingService;
    }

    private WorkerthingserviceEntity getWorkerThingService(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        WorkerthingserviceEntity workerThingService = new WorkerthingserviceEntity();

        TypedQuery<ThingserviceEntity> tq = em.createQuery("SELECT ts FROM ThingserviceEntity ts WHERE ts.idThingService=:id", ThingserviceEntity.class);
        tq.setParameter("id", id);
        workerThingService.setThingserviceByIdThingService(tq.getSingleResult());

        Query q = em.createQuery("SELECT w FROM WorkerEntity w WHERE w.f=:f and  w.i=:i");
        q.setParameter("f", getChosenWorkerF(workerChoiceBox.getValue()));
        q.setParameter("i", getChosenWorkerI(workerChoiceBox.getValue()));
        workerThingService.setWorkerByIdWorker((WorkerEntity) q.getSingleResult());
        em.close();
        return workerThingService;
    }

    private ThingEntity getThing(ThingEntity thing, int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        TypedQuery<OrderrEntity> tq = em.createQuery("SELECT o FROM OrderrEntity o WHERE o.idOrder=:id", OrderrEntity.class);
        tq.setParameter("id", id);
        thing.setOrderrByIdOrder(tq.getSingleResult());
        em.close();
        return thing;
    }

    private Double getPriceSum(EntityManager em, int idOrder) {
        Query query = em.createQuery("SELECT SUM(s.price) FROM OrderrEntity o " +
                "INNER JOIN ThingEntity t ON o.idOrder = t.orderrByIdOrder.idOrder " +
                "LEFT JOIN ThingserviceEntity ts ON t.idThing = ts.thingByIdThing.idThing " +
                "LEFT JOIN ServiceEntity s ON ts.serviceByIdService.idService = s.idService WHERE o.idOrder=:idOrder");
        query.setParameter("idOrder", idOrder);
        return (Double) query.getSingleResult();
    }

    private void addNewOrderLog(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            LogEntity log = new LogEntity();
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            log.setActivity("Создал заказ с id"+id);
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

    private String getChosenWorkerF(String fi) {
        return fi.substring(0, fi.indexOf(" "));
    }

    private String getChosenWorkerI(String fi) {
        return fi.substring(fi.indexOf(" ") + 1);
    }

    private String getChosenService(String s) {
        return s.substring(0, s.indexOf(","));
    }


}
