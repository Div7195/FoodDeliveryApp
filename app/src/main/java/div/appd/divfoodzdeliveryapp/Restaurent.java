package div.appd.divfoodzdeliveryapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurent implements Serializable {
    private String name;
    private String address;
    private String cuisines;
    private String city;
    private String state;
    private String imageUrl;
    private String contact;
    private ArrayList<String> categories;
    private ArrayList<String> dishIds;
    private String restaurentId;
    private Boolean eligible;

    public  Restaurent(String name, String address, String cuisines, String city, String state, String imageUrl, String contact, ArrayList<String> categories, ArrayList<String> dishIds, String restaurentId, Boolean eligible ){
        this.name = name;
        this.address = address;
        this.cuisines = cuisines;
        this.city = city;
        this.state = state;
        this.imageUrl = imageUrl;
        this.contact = contact;
        this.categories = categories;
        this.dishIds = dishIds;
        this.restaurentId = restaurentId;
        this.eligible = eligible;
    }
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getCuisines(){return cuisines;}
    public String getCity(){return city;}
    public String getState(){return state;}
    public String getImageUrl(){return imageUrl;}
    public String getContact(){return contact;}
    public ArrayList<String> getCategories(){return categories;}
    public ArrayList <String>getDishIds(){return dishIds;}
    public String getRestaurentId(){return restaurentId;}
    public Boolean getEligible(){return eligible;}

}
