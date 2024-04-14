package div.appd.divfoodzdeliveryapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Dishtype implements Serializable {
    private String typeName;
    private String imageUrl;
    private ArrayList<String> dishIds;
    private String dishTypeId;

    public Dishtype(String typeName, String imageUrl, ArrayList<String> dishIds, String dishTypeId){
        this.typeName = typeName;
        this.imageUrl = imageUrl;
        this.dishIds = dishIds;
        this.dishTypeId = dishTypeId;
    }
    public String getTypeName(){
        return typeName;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public ArrayList<String> getDishIds() {
        return dishIds;
    }
    public String getDishTypeId() {
        return dishTypeId;
    }
}
