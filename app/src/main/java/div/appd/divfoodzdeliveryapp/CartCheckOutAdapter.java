package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;

import java.util.ArrayList;

import div.appd.divfoodzdeliveryapp.models.CartItemInfo;

public class CartCheckOutAdapter extends ArrayAdapter<CartItemInfo> {
    Double priceOfOneItem = 0.0;
    public CartCheckOutAdapter(Context context, ArrayList<CartItemInfo> cartItemInfoArrayList){
        super(context, 0 , cartItemInfoArrayList );
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CartItemInfo cartItemInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_checkout, parent, false);
        }

        LinearLayout layout = (LinearLayout) parent.getTag();
        TextView totalPriceView = (TextView) layout.findViewById(R.id.itemTotalField);
        TextView totalBillView = (TextView) layout.findViewById(R.id.totalAmountField);
        TextView taxView = (TextView) layout.findViewById(R.id.gstChargeField);
        TextView deliveryFeeView = (TextView) layout.findViewById(R.id.deliveryFeeField);

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

        subtractView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractFromCartList(cartItemInfo, totalPriceView, position, deliveryFeeView, taxView, totalBillView);
            }
        });
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartList(cartItemInfo, totalPriceView, taxView, totalBillView);
            }
        });

        return convertView;
    }
    private void subtractFromCartList(CartItemInfo cartItemInfo, TextView totalPriceView, int position, TextView deliveryFeeView, TextView taxView, TextView totalBillView){
        if (cartItemInfo.getQuanity() >= 1) {
            Integer quantity = cartItemInfo.getQuanity() - 1;
            Double newPrice = cartItemInfo.getPrice() - cartItemInfo.getSingleItemPrice();
            Double newTotalPrice = Double.valueOf(totalPriceView.getText().toString()) - cartItemInfo.getSingleItemPrice();
            Double newTax = 0.18 * newTotalPrice;
            Double deliveryFee = 30.0;
            Double newBillAmount = newTotalPrice + newTax + deliveryFee;
            cartItemInfo.setQuanity(quantity);
            if(quantity == 0){
                remove(getItem(position));
            }else {
                cartItemInfo.setPrice(newPrice);
            }
            notifyDataSetChanged();
            if(newTotalPrice.floatValue() == 0){
                newBillAmount = 0.0;
                deliveryFeeView.setText("0.0");
            }
            totalPriceView.setText(String.valueOf(newTotalPrice.floatValue()));
            taxView.setText(String.valueOf(newTax.floatValue()));
            totalBillView.setText(String.valueOf(newBillAmount.floatValue()));

        }
    }
    private void addToCartList(CartItemInfo cartItemInfo, TextView totalPriceView, TextView taxView, TextView totalBillView){
        Integer quantity = cartItemInfo.getQuanity() + 1;
        Double newPrice = cartItemInfo.getPrice() + cartItemInfo.getSingleItemPrice();
        Double newTotalPrice = Double.valueOf(totalPriceView.getText().toString()) + cartItemInfo.getSingleItemPrice();
        Double newTax = 0.18 * newTotalPrice;
        Double deliveryFee = 30.0;
        Double newBillAmount = newTotalPrice + newTax + deliveryFee;
        cartItemInfo.setQuanity(quantity);
        cartItemInfo.setPrice(newPrice);
        notifyDataSetChanged();
        totalPriceView.setText(String.valueOf(newTotalPrice.floatValue()));
        taxView.setText(String.valueOf(newTax.floatValue()));
        totalBillView.setText(String.valueOf(newBillAmount.floatValue()));
    }

}
