package Session;


public final class ThingId {
    private static ThingId instance;

    private int idThing;

    public ThingId(int idThing) {
        this.idThing = idThing;
    }

    public static ThingId getInstance(int idThing) {
        if (instance == null) {
            instance = new ThingId(idThing);
        }
        return instance;
    }

    public static ThingId getInstance() {
        return instance;
    }

    public int getThingId() {
        return idThing;
    }

    public void cleanThingId() {
        idThing = 0;
    }


}
