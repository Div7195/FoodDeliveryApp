package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import div.appd.divfoodzdeliveryapp.models.CartItemInfo;
import div.appd.divfoodzdeliveryapp.models.Dish;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DishAdapterUser extends ArrayAdapter<Dish> {
    Integer footerHeight;
    Double d = 0.0;
    Integer q = 0;
    String resId = "";
    String mapKey = "map";
    String customerNameForUse, customerAddressForUse, customerIdForUse, restaurentNameForUse;
    Double totalPrice, totalBill, taxes, deliveryFee;
    HashMap<String, CartItemInfo> hashMapSameDish = new HashMap<String, CartItemInfo>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");

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
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        customerIdForUse = pref.getString("customerId", "");
//        ***********************


//        **************************************
        TextView cartTotalPriceView = (TextView) layout.findViewById(R.id.priceInCart);
        TextView cartTotalItemsView = (TextView) layout.findViewById(R.id.itemInCart);
        ViewFlipper viewFlipper = (ViewFlipper)convertView.findViewById(R.id.add_flipper);
        ViewFlipper viewFlipper1 = (ViewFlipper)layout2.findViewById(R.id.flipperViewCart);
        TextView restaurentNameView = (TextView) convertView.findViewById(R.id.restaurant_name_user);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.veg_icon_user);
        TextView dishTitleView = (TextView) convertView.findViewById(R.id.food_name_user);
        TextView foodPriceView = (TextView) convertView.findViewById(R.id.food_price_user);
        ImageView foodImgView = (ImageView) convertView.findViewById(R.id.food_image_user);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarForDishInUser);
        MaterialRatingBar materialRatingBar = (MaterialRatingBar) convertView.findViewById(R.id.food_rating);
        Button addButtonView = (Button) convertView.findViewById(R.id.add_button_user);
        ImageButton imageButtonSubView= (ImageButton) convertView.findViewById(R.id.subtract_button_checkout);
        ImageButton imageButtonAddView = (ImageButton) convertView.findViewById(R.id.increment_item_button_checkout);
        TextView quantityView = (TextView) convertView.findViewById(R.id.quantity_text_checkout);
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
                addToCart(layout1, layout2, dish, viewFlipper, cartTotalPriceView, cartTotalItemsView, quantityView);
            }
        });

        imageButtonSubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromCart(dish, cartTotalPriceView, cartTotalItemsView, quantityView, viewFlipper, layout1, layout2);
            }
        });
        imageButtonAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToExistingCart(dish, cartTotalPriceView, cartTotalItemsView, quantityView);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hashMapSameDish.size() != 0) {
                    ArrayList<CartItemInfo> arrayListCartItems = new ArrayList<CartItemInfo>();
                    viewFlipper1.showNext();
                    for(CartItemInfo value : hashMapSameDish.values()){
                        arrayListCartItems.add(new CartItemInfo(value.getDishId()
                        ,value.getRestaurentId()
                        ,value.getDishName()
                        ,value.getQuanity()
                        ,value.getPrice()
                                ,value.getSingleItemPrice()
                        ,value.getVegNonVeg()));
                    }
                    totalPrice = 0.0;
                    totalBill = 0.0;
                    taxes = 0.0;
                    deliveryFee = 0.0;
                    for(CartItemInfo cartItemInfo : arrayListCartItems){
                        totalPrice = totalPrice + cartItemInfo.getPrice();
                    }
                    taxes = 0.18 * totalPrice;
                    deliveryFee = 30.0;
                    totalBill = totalPrice + taxes + deliveryFee;
                    Intent intent = new Intent(getContext(), OrderViewUser.class);
                    Bundle args = new Bundle();
                    args.putSerializable("arraylistcartitems",(Serializable)arrayListCartItems);
                    args.putString("totalbill", String.valueOf(totalBill.floatValue()));
                    args.putString("taxes", String.valueOf(taxes.floatValue()));
                    args.putString("deliveryfee", String.valueOf(deliveryFee.floatValue()));
                    args.putString("totalprice", String.valueOf(totalPrice.floatValue()));
                    databaseReference.child("customers").child(customerIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            args.putString("customerId",customerIdForUse);
                            args.putString("customerName", snapshot.child("name").getValue(String.class));
                            args.putString("customerAddress", snapshot.child("address").getValue(String.class));
                            args.putString("customerContact",snapshot.child("contact").getValue(String.class));
                            databaseReference.child("restaurents").child(arrayListCartItems.get(0).getRestaurentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    args.putString("restaurentId", arrayListCartItems.get(0).getRestaurentId());
                                    args.putString("restaurentName", snapshot.child("name").getValue(String.class));
                                    args.putString("restaurentAddress", snapshot.child("address").getValue(String.class));
                                    args.putString("restaurentContact", snapshot.child("contact").getValue(String.class));
                                    intent.putExtra("bundleData",args);
                                    viewFlipper1.showPrevious();
                                    getContext().startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });



        return convertView;
    }
    private void removeFromCart(Dish dish, TextView cartTotalPriceView, TextView cartTotalItemsView, TextView quantityView, ViewFlipper viewFlipper, LinearLayout layout1, LinearLayout layout2){
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
                int h = layout1.getHeight() + 169;
                int w = params1.width;
                layout2.setVisibility(View.GONE);
                params1.height = h;
                params1.width = w;
                layout1.setLayoutParams(params1);
            }
        }
    }

    private void addToCart(LinearLayout layout1, LinearLayout layout2, Dish dish, ViewFlipper viewFlipper, TextView cartTotalPriceView, TextView cartTotalItemsView, TextView quantityView) {
        if(resId.equals("") || resId.equals(dish.getRestaurentId())){
            viewFlipper.showNext();
            if(layout2 != null) {
                if(hashMapSameDish.size() == 0) {
                    ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                    int h = layout1.getHeight() - 169;
                    int w = params1.width;
                    params1.height = h;
                    params1.width = w;
                    layout1.setLayoutParams(params1);
                    layout2.setVisibility(View.VISIBLE);

                }
                hashMapSameDish.put(dish.getDishId(), new CartItemInfo(dish.getDishId(), dish.getRestaurentId(), dish.getTitle(), 0, 0.0,Double.valueOf(dish.getPrice()), dish.getVegOrNonveg()));
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
    private void addToExistingCart(Dish dish, TextView cartTotalPriceView, TextView cartTotalItemsView, TextView quantityView){
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
