package div.appd.divfoodzdeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;

import java.util.ArrayList;

import div.appd.divfoodzdeliveryapp.models.OrderItem;

public class OrderItemAdapter extends ArrayAdapter<OrderItem> {
    public OrderItemAdapter(Context context, ArrayList<OrderItem> orderItems) {
        super(context, 0, orderItems);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderItem orderItem = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item_list, parent, false);
        }
        TextView orderIdView = (TextView) convertView.findViewById(R.id.orderid_item_list);
        TextView statusView = (TextView) convertView.findViewById(R.id.status_item_list);
        TextView partnerStatusView =(TextView) convertView.findViewById(R.id.partnerName);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateField);
        if(orderItem.getOrderId() != null){
            orderIdView.setText(orderItem.getOrderId());
        }
        if(orderItem.getStatus() != null){
            statusView.setText(orderItem.getStatus());
        }
        if(orderItem.getDate() != null){
            dateView.setText(orderItem.getDate());
        }
        if(orderItem.getPartnerName() != null){
            partnerStatusView.setText(orderItem.getPartnerName());
        }else{
            partnerStatusView.setText("Not assigned");
        }
        return convertView;
    }
}
