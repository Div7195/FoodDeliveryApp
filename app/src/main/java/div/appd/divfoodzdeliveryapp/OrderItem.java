package div.appd.divfoodzdeliveryapp;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String orderId;
    private String status;
    private String partnerName;
    private String date;

    public OrderItem(String orderId, String status, String partnerName, String date){
        this.orderId = orderId;
        this.status = status;
        this.partnerName = partnerName;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getDate() {
        return date;
    }
}
