package Session;


public final class OrderId {
    private static OrderId instance;

    private int idOrder;

    public OrderId(int idOrder) {
        this.idOrder = idOrder;
    }

    public static OrderId getInstance(int idOrder) {
        if (instance == null) {
            instance = new OrderId(idOrder);
        }
        return instance;
    }

    public static OrderId getInstance() {
        return instance;
    }

    public int getOrderId() {
        return idOrder;
    }

    public void cleanOrderId() {
        idOrder = 0;
    }


}
