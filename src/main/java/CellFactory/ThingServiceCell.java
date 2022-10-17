package CellFactory;

import Model.ThingserviceEntity;
import javafx.scene.control.ListCell;


public class ThingServiceCell extends ListCell<ThingserviceEntity> {
    @Override
    public void updateItem(ThingserviceEntity thingService, boolean empty) {

        super.updateItem(thingService, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (thingService == null || empty) {
        } else {
            name = (index + 1) + ". " +
                    thingService.getServiceByIdService().getName() + ", " +
                    thingService.getThingByIdThing().getType() + ", " +
                    thingService.getThingByIdThing().getName();
        }

        this.setText(name);
        setGraphic(null);
    }
}

