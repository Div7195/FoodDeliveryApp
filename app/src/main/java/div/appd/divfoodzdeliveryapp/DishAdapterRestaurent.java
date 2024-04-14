package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import div.appd.divfoodzdeliveryapp.models.Dish;

public class DishAdapterRestaurent extends ArrayAdapter<Dish> {
    public DishAdapterRestaurent(Context context, ArrayList<Dish> dishes){
        super(context, 0 , dishes );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Dish dish = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dish_item_res, parent, false);
        }
        TextView categoryView = (TextView) convertView.findViewById(R.id.category_res);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.veg_icon_res);
        TextView dishTitleView = (TextView) convertView.findViewById(R.id.food_name_res);
        TextView foodPriceView = (TextView) convertView.findViewById(R.id.food_price_res);
        ImageView foodImgView = (ImageView) convertView.findViewById(R.id.food_image_res);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarImageRes);
        Button editButtonView = (Button) convertView.findViewById(R.id.edit_button);

        categoryView.setText(dish.getCategory());
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
        foodPriceView.setText(dish.getPrice());
        if(dish.getVegOrNonveg() != null) {
            if (dish.getVegOrNonveg().equals("VEG")) {
                imgView.setImageResource(R.drawable.veg);
            }else{
                imgView.setImageResource(R.drawable.nonveg);
            }
        }

        editButtonView.setOnClickListener(new View.OnClickListener() {
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
