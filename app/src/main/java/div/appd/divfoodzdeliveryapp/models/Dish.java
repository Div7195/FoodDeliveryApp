package div.appd.divfoodzdeliveryapp.models;

import java.io.Serializable;

public class Dish implements Serializable {
    private String title;
    private String dishTag;
    private String price;
    private String perQuantity;
    private String imageUrl;
    private Double rating;
    private Integer timesOrdered;
    private String restaurentId;
    private String category;
    private Boolean inStock;
    private String restaurentName;
    private String vegOrNonveg;
    private String dishId;
    public  Dish(String title, String dishTag, String price, String perQuantity, String imageUrl, Double rating, Integer timesOrdered, String restaurentId, String category, Boolean inStock, String restaurentName, String vegOrNonveg, String dishId){
        this.title = title;
        this.dishTag = dishTag;
        this.price = price;
        this.perQuantity = perQuantity;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.timesOrdered = timesOrdered;
        this.restaurentId = restaurentId;
        this.category = category;
        this.inStock = inStock;
        this.restaurentName = restaurentName;
        this.vegOrNonveg = vegOrNonveg;
        this.dishId = dishId;
    }
    public String getTitle(){
        return title;
    }
    public String getDishTag(){
        return dishTag;
    }
    public String getPrice(){
        return price;
    }
    public String getPerQuantity(){
        return perQuantity;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public Double getRating(){
        return rating;
    }
    public Integer getTimesOrdered(){
        return timesOrdered;
    }
    public String getRestaurentId(){
        return restaurentId;
    }
    public String getCategory(){
        return category;
    }
    public Boolean getInStock(){
        return inStock;
    }
    public String getRestaurentName(){
        return restaurentName;
    }
    public String getVegOrNonveg(){
        return vegOrNonveg;
    }
    public String getDishId(){return dishId;}
}
