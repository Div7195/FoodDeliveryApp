package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RestoViewOfUserAdapter extends ArrayAdapter<Dish> {
    Double d = 0.0;
    Integer q = 0;
    String resId = "";
    String mapKey = "map";
    HashMap<String, CartItemInfo> hashMapSameDish = new HashMap<String, CartItemInfo>();
    public RestoViewOfUserAdapter(Context context, ArrayList<Dish> dishes){
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

        TextView cartTotalPriceView = (TextView) layout.findViewById(R.id.priceInCart);
        TextView cartTotalItemsView = (TextView) layout.findViewById(R.id.itemInCart);
//        **************************************


        ViewFlipper viewFlipper = (ViewFlipper)convertView.findViewById(R.id.add_flipper);
        TextView dishNameView = (TextView) convertView.findViewById(R.id.restaurant_name_user);
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
        dishNameView.setText(dish.getCategory());
        dishNameView.setPadding(10,10,10,10);

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
                Toast.makeText(getContext(), String.valueOf(hashMapSameDish.size()), Toast.LENGTH_SHORT).show();
            }
        });




        return convertView;
    }

}


