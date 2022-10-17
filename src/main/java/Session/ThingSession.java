package Session;

import Model.ThingEntity;


public final class ThingSession {
    private static ThingSession instance;

    private ThingEntity thing;

    public ThingSession(ThingEntity thing) {
        this.thing = thing;
    }

    public static ThingSession getInstance(ThingEntity thing) {
        instance = new ThingSession(thing);
        return instance;
    }

    public static ThingSession getInstance() {
        return instance;
    }

    public ThingEntity getThing() {
        return thing;
    }

    public void cleanThingSession() {
        thing = null;
    }


}
