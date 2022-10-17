package Controller;

import CellFactory.SupplerMaterialCellFactory;
import Model.*;
import Session.UserSession;
import Util.GetListForChoiceBox;
import Util.NewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaterialsMenuController implements Initializable {
    @FXML
    private ChoiceBox<String> supplierChoiceBox;

    @FXML
    private ChoiceBox<String> materialChoiceBox;

    @FXML
    private Spinner<Double> priceSpinner;

    @FXML
    private Spinner<Double> quantitySpinner;

    @FXML
    private ListView<SuppliermaterialEntity> suppliesListView;

    @FXML
    private TableView materialsTableView;


    private ObservableList<MaterialEntity> materials;

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        materialChoiceBox.getItems().addAll(GetListForChoiceBox.getMaterialsList());
        supplierChoiceBox.getItems().addAll(GetListForChoiceBox.getSuppliersList());

        SpinnerValueFactory<Double> valueFactory1 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 99999);
        valueFactory1.setValue(0.0);

        SpinnerValueFactory<Double> valueFactory2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 99999);
        valueFactory2.setValue(0.0);

        quantitySpinner.setValueFactory(valueFactory1);
        priceSpinner.setValueFactory(valueFactory2);

        suppliesListView.getItems().addAll(getSupplierMaterialList());
        suppliesListView.setCellFactory(new SupplerMaterialCellFactory());

        updateTable();
        setupTable();
    }

    public void addMaterialOrder(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение добавления поставки");
        alert.setContentText("Добавить поставку?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            setSupply(getChosenSupplierEntity(supplierChoiceBox.getValue()), getChosenMaterialEntity(getChosenMaterial()), quantitySpinner.getValue(), priceSpinner.getValue());
            addSupplyLog();
            NewWindow.setNewWindow("materialsMenu.fxml", event);
        }

    }

    public void back(ActionEvent event) throws IOException{
        ENTITY_MANAGER_FACTORY.close();
        NewWindow.setNewWindow("managerMenu.fxml", event);
    }

    private void setSupply(SupplierEntity supplier, MaterialEntity material, Double quantity, Double price) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            SuppliermaterialEntity supplierMaterial = new SuppliermaterialEntity();
            supplierMaterial.setSupplierByIdSupplier(supplier);
            supplierMaterial.setMaterialByIdMaterial(material);
            supplierMaterial.setQuantity(quantity);
            supplierMaterial.setPrice(price);
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            supplierMaterial.setSupplydate(sqlDate);

            em.persist(supplierMaterial);

            Query q = em.createQuery("UPDATE MaterialEntity m SET m.quantity=:quantity WHERE m.idMaterial =:idMaterial");
            q.setParameter("quantity", em.createQuery("SELECT m.quantity FROM MaterialEntity m WHERE m.idMaterial=:idMaterial", Double.class).
                    setParameter("idMaterial", material.getIdMaterial()).getSingleResult() + quantity).setParameter("idMaterial", material.getIdMaterial());
            q.executeUpdate();

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

    private void updateTable(){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT m FROM MaterialEntity m");
        List results = q.getResultList();
        if(materials == null){
            materials = FXCollections.observableArrayList(results);
        }
        else{
            materials.clear();
            materials.addAll(results);
        }
        em.close();
    }

    private void setupTable(){
        TableColumn<MaterialEntity, String> nameTableColumn = new TableColumn<>();
        nameTableColumn.setText("Название");
        nameTableColumn.setMinWidth(100);
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MaterialEntity, Double> quantityTableColumn = new TableColumn<>();
        quantityTableColumn.setText("Количество");
        quantityTableColumn.setMinWidth(100);
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<MaterialEntity, String> materialTableColumn = new TableColumn<>();
        materialTableColumn.setText("Категория");
        materialTableColumn.setMinWidth(100);
        materialTableColumn.setCellValueFactory(m -> {
            if (m.getValue() != null) {
                return new SimpleStringProperty(m.getValue().getCategoryByIdCategory().getName());
            } else {
                return new SimpleStringProperty("<no name>");
            }
        });
        materialsTableView.getColumns() .addAll(nameTableColumn, quantityTableColumn, materialTableColumn);
        materialsTableView.setItems(materials);
    }

    private void addSupplyLog() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            LogEntity log = new LogEntity();
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            log.setActivity("Принял поставку на материал " + getChosenMaterial() + " от поставщика " + supplierChoiceBox.getValue() + " в количестве " + quantitySpinner.getValue() + " за " + priceSpinner.getValue());
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

    private ArrayList<SuppliermaterialEntity> getSupplierMaterialList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<SuppliermaterialEntity> tq = em.createQuery("SELECT sm FROM SuppliermaterialEntity sm", SuppliermaterialEntity.class);
        ArrayList<SuppliermaterialEntity> result = new ArrayList<>(tq.getResultList());
        em.close();
        return result;
    }

    private SupplierEntity getChosenSupplierEntity(String material) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<SupplierEntity> tq = em.createQuery("SELECT s FROM SupplierEntity s WHERE s.name=:name", SupplierEntity.class);
        SupplierEntity result = tq.setParameter("name", material).getSingleResult();
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
