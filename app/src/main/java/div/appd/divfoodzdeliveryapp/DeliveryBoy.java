package div.appd.divfoodzdeliveryapp;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DeliveryBoy implements Serializable {
    private String name;
    private String city;
    private String state;
    private String contact;
    private Boolean eligible;
    private ArrayList<String> orderIds;
    private String deliveryBoyId;

    public DeliveryBoy(String name, String city, String state, String contact,  Boolean eligible, ArrayList<String> orderIds, String deliveryBoyId){
        this.name = name;
        this.city = city;
        this.state = state;
        this.contact = contact;
        this.eligible = eligible;
        this.orderIds = orderIds;
        this.deliveryBoyId = deliveryBoyId;

    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getContact() {
        return contact;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public ArrayList<String> getOrderIds() {
        return orderIds;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }
}
