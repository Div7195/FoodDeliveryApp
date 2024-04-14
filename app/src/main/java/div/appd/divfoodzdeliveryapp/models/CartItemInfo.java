package div.appd.divfoodzdeliveryapp.models;

import java.io.Serializable;

public class CartItemInfo implements Serializable {
    private String dishId;
    private String restaurentId;
    private String dishName;
    private Integer quanity;
    private Double price;
    private Double singleItemPrice;
    private String vegNonVeg;

    public CartItemInfo(String dishId, String restaurentId, String dishName, Integer quanity, Double price,Double singleItemPrice, String vegNonVeg){
        this.dishId = dishId;
        this.restaurentId = restaurentId;
        this.dishName = dishName;
        this.quanity = quanity;
        this.price = price;
        this.singleItemPrice = singleItemPrice;
        this.vegNonVeg = vegNonVeg;
    }

    public String getDishId() {
        return dishId;
    }

    public String getRestaurentId() {
        return restaurentId;
    }

    public String getDishName() {
        return dishName;
    }

    public Integer getQuanity() {
        return quanity;
    }

    public Double getPrice() {
        return price;
    }
    public Double getSingleItemPrice(){return singleItemPrice;}

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setRestaurentId(String restaurentId) {
        this.restaurentId = restaurentId;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuanity(Integer quanity) {
        this.quanity = quanity;
    }
    public void setSingleItemPrice(Double singleItemPrice){this.singleItemPrice = singleItemPrice;}

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }
}
