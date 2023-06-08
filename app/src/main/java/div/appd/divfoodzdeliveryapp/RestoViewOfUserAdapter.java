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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestoViewOfUserAdapter extends ArrayAdapter<Dish> {
    public RestoViewOfUserAdapter(Context context, ArrayList<Dish> dishes){
        super(context, 0 , dishes );
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Dish dish = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurent_dish_item, parent, false);
        }
        ViewFlipper viewFlipper = (ViewFlipper)convertView.findViewById(R.id.add_flipper);
        TextView dishNameView = (TextView) convertView.findViewById(R.id.restaurant_name_user);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.veg_icon_user);
        TextView dishTitleView = (TextView) convertView.findViewById(R.id.food_name_user);
        TextView foodPriceView = (TextView) convertView.findViewById(R.id.food_price_user);
        ImageView foodImgView = (ImageView) convertView.findViewById(R.id.food_image_user);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarForDishInUser);
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
        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), AddItemActivity.class);
                mIntent.putExtra("action", "edit");
                mIntent.putExtra("dishObj",dish);
                getContext().startActivity(mIntent);
            }
        });


        return convertView;
    }

}


