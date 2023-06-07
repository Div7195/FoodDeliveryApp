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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RestaurentAdapter extends ArrayAdapter<Restaurent> {
    public RestaurentAdapter(Context context, ArrayList<Restaurent> restaurents) {
        super(context, 0, restaurents);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Restaurent restaurent = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurent_item, parent, false);
        }
        TextView restaurenName = (TextView) convertView.findViewById(R.id.restaurent_name_resto);
        TextView dishOrResto = (TextView) convertView.findViewById(R.id.dishOrResto);
        TextView cuisinesView = (TextView) convertView.findViewById(R.id.cuisines_restaurent);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_restaurent);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarinRestaurentItem);

        if(restaurent.getName() != null){
            restaurenName.setText(restaurent.getName());
        }
        if(restaurent.getCuisines() !=null){
            cuisinesView.setText(restaurent.getCuisines());
        }
        if(restaurent.getImageUrl() != null){
            Picasso.get().load(restaurent.getImageUrl()).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }


        return convertView;
    }

    }

