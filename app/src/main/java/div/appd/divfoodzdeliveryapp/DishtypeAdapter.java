package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DishtypeAdapter extends ArrayAdapter<Dishtype> {
    public DishtypeAdapter(Context context, ArrayList<Dishtype> dishes) {
        super(context, 0, dishes);
    }
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Dishtype dishtype = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dishtype_item, parent, false);
            }
            TextView dishTypeTitle = (TextView) convertView.findViewById(R.id.dish_type_name);
            TextView dishOrResto = (TextView) convertView.findViewById(R.id.dish_or_resto);
            ImageView foodImgView = (ImageView) convertView.findViewById(R.id.food_image_type_user);
            ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarForDishType);
            if(dishtype.getImageUrl()!=null) {
                Picasso.get().load(dishtype.getImageUrl()).into(foodImgView, new Callback() {
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
            dishTypeTitle.setText(dishtype.getTypeName());
            dishOrResto.setText("Dish");
            return convertView;
        }

}
