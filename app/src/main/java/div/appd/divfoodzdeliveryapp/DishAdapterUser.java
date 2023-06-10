package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;

import com.example.fooddelivery.R;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DishAdapterUser extends ArrayAdapter<Dish> {
    Integer gg;
    Double d = 0.0;
    Integer q = 0;
    String resId = "";
    String mapKey = "map";
    HashMap<String, CartItemInfo> hashMapSameDish = new HashMap<String, CartItemInfo>();
    public DishAdapterUser(Context context, ArrayList<Dish> dishes){
        super(context, 0 , dishes );
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Dish dish = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurent_dish_item, parent, false);
        }


        LinearLayout layout = (LinearLayout) parent.getTag();
        LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
        LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
//        ***********************


//        **************************************
        TextView cartTotalPriceView = (TextView) layout.findViewById(R.id.priceInCart);
        TextView cartTotalItemsView = (TextView) layout.findViewById(R.id.itemInCart);
        ViewFlipper viewFlipper = (ViewFlipper)convertView.findViewById(R.id.add_flipper);
        TextView restaurentNameView = (TextView) convertView.findViewById(R.id.restaurant_name_user);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.veg_icon_user);
        TextView dishTitleView = (TextView) convertView.findViewById(R.id.food_name_user);
        TextView foodPriceView = (TextView) convertView.findViewById(R.id.food_price_user);
        ImageView foodImgView = (ImageView) convertView.findViewById(R.id.food_image_user);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarForDishInUser);
        MaterialRatingBar materialRatingBar = (MaterialRatingBar) convertView.findViewById(R.id.food_rating);
        Button addButtonView = (Button) convertView.findViewById(R.id.add_button_user);
        ImageButton imageButtonSubView= (ImageButton) convertView.findViewById(R.id.subtract_button);
        ImageButton imageButtonAddView = (ImageButton) convertView.findViewById(R.id.increment_item_button);
        TextView quantityView = (TextView) convertView.findViewById(R.id.quantity_text);
        restaurentNameView.setText(dish.getRestaurentName());
        restaurentNameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_arrow_forward_24,0);
        if(dish.getImageUrl()!=null) {
            Picasso.get().load(dish.getImageUrl()).into(foodImgView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else{
            foodImgView.setBackgroundResource(R.drawable.default_food);
            progressBar.setVisibility(View.GONE);
        }
        dishTitleView.setText(dish.getTitle());
        if(dish.getPrice() != null) {
            foodPriceView.setText(dish.getPrice());
        }
        if(dish.getVegOrNonveg() != null) {
            if (dish.getVegOrNonveg().equals("VEG")) {
                imgView.setImageResource(R.drawable.veg);
            } else {
                imgView.setImageResource(R.drawable.nonveg);
            }
        }
        if(dish.getRating() != null){
            float f = dish.getRating().floatValue();
            f = 4.25F;
            materialRatingBar.setRating(f);
        }


        restaurentNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), UserRestoActivity.class);
                if(dish.getRestaurentId() == null){
                    Log.d("debugging", "id is null");
                }else {
                    saveMap(hashMapSameDish);
                    mIntent.putExtra("restaurentId", dish.getRestaurentId());
                    getContext().startActivity(mIntent);
                }
            }
        });




        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resId.equals("") || resId.equals(dish.getRestaurentId())){
                        viewFlipper.showNext();
                        if(layout2 != null) {
                            if(hashMapSameDish.size() == 0) {
                                ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                                int h = layout1.getHeight() - 116;
                                int w = params1.width;
                                params1.height = h;
                                params1.width = w;
                                layout1.setLayoutParams(params1);
                                layout2.setVisibility(View.VISIBLE);
                            }
                            hashMapSameDish.put(dish.getDishId(), new CartItemInfo(dish.getDishId(), dish.getRestaurentId(), dish.getTitle(), 0, 0.0, dish.getVegOrNonveg()));
                            resId = dish.getRestaurentId();
                            CartItemInfo cartItemInfo = hashMapSameDish.get(dish.getDishId());
                            Integer quantity = cartItemInfo.getQuanity() + 1;
                            Double price = cartItemInfo.getPrice() + Double.valueOf(dish.getPrice());
                            cartItemInfo.setQuanity(quantity);
                            cartItemInfo.setPrice(price);
                            d = d + Double.valueOf(dish.getPrice());
                            q = q + 1;
                            cartTotalPriceView.setText(String.valueOf(d));
                            cartTotalItemsView.setText(String.valueOf(q));
                            quantityView.setText(String.valueOf(cartItemInfo.getQuanity()));
                        }else{
                            Toast.makeText(getContext(), "fsadf", Toast.LENGTH_SHORT).show();
                        }
                }else{
                    Toast.makeText(getContext(), "Select same restaurent", Toast.LENGTH_SHORT).show();
                }



            }
        });

        imageButtonSubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartItemInfo cartItemInfo = hashMapSameDish.get(dish.getDishId());
                Integer quantity = cartItemInfo.getQuanity() - 1;
                Double price = cartItemInfo.getPrice() - Double.valueOf(dish.getPrice());
                cartItemInfo.setQuanity(quantity);
                cartItemInfo.setPrice(price);
                d = d - Double.valueOf(dish.getPrice());
                q = q - 1;
                cartTotalPriceView.setText(String.valueOf(d));
                cartTotalItemsView.setText(String.valueOf(q));
                quantityView.setText(String.valueOf(cartItemInfo.getQuanity()));
                if(cartItemInfo.getQuanity() == 0){
                    viewFlipper.showPrevious();
                    hashMapSameDish.remove(dish.getDishId());
                    if(hashMapSameDish.size() == 0){
                        resId = "";
                        ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                        int h = layout1.getHeight() + layout2.getHeight();
                        int w = params1.width;
                        layout2.setVisibility(View.GONE);
                        params1.height = h;
                        params1.width = w;
                        layout1.setLayoutParams(params1);
                    }
                }
            }
        });
        imageButtonAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CartItemInfo cartItemInfo = hashMapSameDish.get(dish.getDishId());
                    Integer quantity = cartItemInfo.getQuanity() + 1;
                    Double price = cartItemInfo.getPrice() + Double.valueOf(dish.getPrice());
                    cartItemInfo.setQuanity(quantity);
                    cartItemInfo.setPrice(price);
                    d = d + Double.valueOf(dish.getPrice());
                    q = q + 1;
                    cartTotalPriceView.setText(String.valueOf(d));
                    cartTotalItemsView.setText(String.valueOf(q));
                    quantityView.setText(String.valueOf(cartItemInfo.getQuanity()));

            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hashMapSameDish.size() != 0) {
                    Toast.makeText(getContext(), String.valueOf(hashMapSameDish.size()), Toast.LENGTH_SHORT).show();

                }
            }
        });


        return convertView;
    }
    private void saveMap(HashMap<String, CartItemInfo> inputMap) {
        SharedPreferences pSharedPref = getContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(mapKey).apply();
            editor.putString(mapKey, jsonString);
            editor.commit();
        }
    }
    private HashMap<String, CartItemInfo> loadMap() {
        HashMap<String, CartItemInfo> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getContext().getSharedPreferences("MyVariables",
                Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, (CartItemInfo) jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

}
