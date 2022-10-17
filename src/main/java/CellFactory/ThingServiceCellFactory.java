package CellFactory;

import Model.ThingserviceEntity;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ThingServiceCellFactory implements Callback<ListView<ThingserviceEntity>, ListCell<ThingserviceEntity>> {
    @Override
    public ListCell<ThingserviceEntity> call(ListView<ThingserviceEntity> listview)
    {
        return new ThingServiceCell();
    }

}
