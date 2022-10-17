package Session;

import Model.OrderrEntity;

public final class OrderSession {
    private static OrderSession instance;

    private OrderrEntity order;

    public OrderSession(OrderrEntity order) {
        this.order = order;
    }

    public static OrderSession getInstance(OrderrEntity order) {
        if (instance == null) {
            instance = new OrderSession(order);
        }
        return instance;
    }

    public static OrderSession getInstance() {
        return instance;
    }

    public OrderrEntity getOrder() {
        return order;
    }

    public void cleanOrderSession() {
        order = null;
    }


}
