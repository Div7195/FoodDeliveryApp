package div.appd.divfoodzdeliveryapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    private String name;
    private String address;
    private String city;
    private String state;
    private String contact;
    private String email;
    private Boolean eligible;
    private ArrayList<String> orderIds;
    private String customerId;

    public Customer(String name, String address, String city, String state, String email, String contact, Boolean eligible, ArrayList<String> orderIds, String customerId){
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.contact = contact;
        this.email = email;
        this.eligible = eligible;
        this.orderIds = orderIds;
        this.customerId = customerId;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
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

    public String getEmail() {
        return email;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public ArrayList<String> getOrderIds() {
        return orderIds;
    }

    public String getCustomerId() {
        return customerId;
    }
}
