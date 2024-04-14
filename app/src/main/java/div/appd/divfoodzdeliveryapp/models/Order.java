package div.appd.divfoodzdeliveryapp.models;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private String status;
    private String deliveryBoyName;

    public Order(String orderId, String status, String deliveryBoyName){
        this.orderId = orderId;
        this.status = status;
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }
}
