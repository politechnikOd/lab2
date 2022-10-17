package CellFactory;

import Model.SuppliermaterialEntity;
import javafx.scene.control.ListCell;


public class SupplierMaterialCell extends ListCell<SuppliermaterialEntity> {
    @Override
    public void updateItem(SuppliermaterialEntity supplierMaterial, boolean empty) {

        super.updateItem(supplierMaterial, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (supplierMaterial == null || empty) {
        } else {
            name = (index + 1) + ". Поставщик: " +
                    supplierMaterial.getSupplierByIdSupplier().getName() + "; Материал " +
                    supplierMaterial.getMaterialByIdMaterial().getName() + " из категории " +
                    supplierMaterial.getMaterialByIdMaterial().getCategoryByIdCategory().getName() + "; Дата доставки: " +
                    supplierMaterial.getSupplydate() + "; Количество: " +
                    supplierMaterial.getQuantity() + "; Цена: " +
                    supplierMaterial.getPrice();
        }

        this.setText(name);
        setGraphic(null);
    }

}
