package CellFactory;

import Model.SuppliermaterialEntity;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SupplerMaterialCellFactory implements Callback<ListView<SuppliermaterialEntity>, ListCell<SuppliermaterialEntity>>{
    @Override
    public ListCell<SuppliermaterialEntity> call(ListView<SuppliermaterialEntity> suppliermaterialEntityListView) {
        return new SupplierMaterialCell();
    }
}
