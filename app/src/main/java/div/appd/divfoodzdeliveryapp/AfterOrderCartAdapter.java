package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;

import java.util.ArrayList;

import div.appd.divfoodzdeliveryapp.models.CartItemInfo;

public class AfterOrderCartAdapter extends ArrayAdapter<CartItemInfo> {
    public AfterOrderCartAdapter(Context context, ArrayList<CartItemInfo> cartItemInfoArrayList){
        super(context, 0 , cartItemInfoArrayList );
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CartItemInfo cartItemInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_checkout, parent, false);
        }
        ImageView vegNonvegView = (ImageView) convertView.findViewById(R.id.veg_non_veg_cart);
        TextView dishTitleView = (TextView) convertView.findViewById(R.id.checkout_dishname);
        TextView itemPriceView = (TextView) convertView.findViewById(R.id.checkout_price);
        ImageButton subtractView = (ImageButton) convertView.findViewById(R.id.subtract_button_checkout);
        ImageButton addView = (ImageButton) convertView.findViewById(R.id.increment_item_button_checkout);
        TextView quantityView = (TextView) convertView.findViewById(R.id.quantity_text_checkout);
        if(cartItemInfo.getVegNonVeg() != null) {
            if (cartItemInfo.getVegNonVeg().equals("VEG")) {
                vegNonvegView.setImageResource(R.drawable.veg);
            } else {
                vegNonvegView.setImageResource(R.drawable.nonveg);
            }
        }
        if(cartItemInfo.getDishName() != null){
            dishTitleView.setText(cartItemInfo.getDishName());
        }
        if(cartItemInfo.getPrice() != null){
            itemPriceView.setText(String.valueOf(cartItemInfo.getPrice().floatValue()));
        }
        if(cartItemInfo.getQuanity() != null){
            quantityView.setText(String.valueOf(cartItemInfo.getQuanity()));
        }
        subtractView.setEnabled(false);
        addView.setEnabled(false);
        return convertView;
    }
}
