package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import div.appd.divfoodzdeliveryapp.models.OrderItem;

public class OrdersListActivity extends AppCompatActivity {
    ProgressBar progressBarForOrder;

    String accessOrders, customerIdForUse, restaurenIdForUse, deliveryBoyIdForUse;
    ArrayList<OrderItem> orderItemArrayList = new ArrayList<OrderItem>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        progressBarForOrder = findViewById(R.id.progressBarInOrderList);
        accessOrders = getIntent().getStringExtra("accessOrders");
        if(accessOrders.equals("customer")){
            viewOrdersCustomer();
        } else if (accessOrders.equals("restaurent")) {
            viewOrdersRestaurant();
        } else if (accessOrders.equals("deliveryBoy")) {
            String config = getIntent().getStringExtra("deliveryBoyConfig");
            if (config.equals("assignedOnly")) {
                viewAssignedOrders();
            }else{
                viewOrdersDeliveryBoy();
            }
        }
    }
    private void viewOrdersDeliveryBoy(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        deliveryBoyIdForUse = pref.getString("deliveryBoyId", "");
        ArrayList<String> orderIds = new ArrayList<String>();
        databaseReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String s = snapshot1.child("deliveryboyid").getValue(String.class);
                    if(s == null) {
                        orderItemArrayList.add(new OrderItem(snapshot1.getRef().getKey()
                                , snapshot.child(snapshot1.getRef().getKey()).child("status").getValue(String.class)
                                , snapshot.child(snapshot1.getRef().getKey()).child("partnerName").getValue(String.class)
                                , snapshot.child(snapshot1.getRef().getKey()).child("date").getValue(String.class)));
                    }
                }
                Collections.reverse(orderItemArrayList);
                OrderItemAdapter adapter = new OrderItemAdapter(OrdersListActivity.this, orderItemArrayList);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.orderList);
                listView.setAdapter(adapter);
                getListViewSize(listView);
                progressBarForOrder.setVisibility(View.GONE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(OrdersListActivity.this, OrderViewUser.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderViewConfig", "deliveryboy");
                        bundle.putString("deliveryBoyId", deliveryBoyIdForUse);
                        bundle.putString("orderId",orderItemArrayList.get(i).getOrderId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void viewAssignedOrders(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        deliveryBoyIdForUse = pref.getString("deliveryBoyId", "");
//                Toast.makeText(this, deliveryBoyIdForUse, Toast.LENGTH_SHORT).show();
        ArrayList<String> orderIds = new ArrayList<String>();
        databaseReference.child("deliveryboys").child(deliveryBoyIdForUse).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    orderIds.add(snapshot1.child("value").getValue(String.class));
                }
                databaseReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String s : orderIds) {
                            orderItemArrayList.add(new OrderItem(s
                                    , snapshot.child(s).child("status").getValue(String.class)
                                    , snapshot.child(s).child("deliveryboyname").getValue(String.class)
                                    , snapshot.child(s).child("date").getValue(String.class)));
                        }
                        Collections.reverse(orderItemArrayList);
                        OrderItemAdapter adapter = new OrderItemAdapter(OrdersListActivity.this, orderItemArrayList);
                        // Attach the adapter to a ListView
                        ListView listView = (ListView) findViewById(R.id.orderList);
                        listView.setAdapter(adapter);
                        getListViewSize(listView);
                        progressBarForOrder.setVisibility(View.GONE);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Intent intent = new Intent(OrdersListActivity.this, OrderViewUser.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("orderViewConfig", "deliveryboy");
                                bundle.putString("deliveryBoyId", deliveryBoyIdForUse);
                                bundle.putString("orderId",orderItemArrayList.get(i).getOrderId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
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
    private void viewOrdersCustomer(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        customerIdForUse = pref.getString("customerId", "");
        ArrayList<String> orderIds = new ArrayList<String>();
        databaseReference.child("customers").child(customerIdForUse).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    orderIds.add(snapshot1.child("value").getValue(String.class));
                }
                databaseReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(String s : orderIds){
                            orderItemArrayList.add(new OrderItem(s
                                    ,snapshot.child(s).child("status").getValue(String.class)
                                    ,snapshot.child(s).child("deliveryboyname").getValue(String.class)
                                    ,snapshot.child(s).child("date").getValue(String.class)));
                        }
                        Collections.reverse(orderItemArrayList);
                        OrderItemAdapter adapter = new OrderItemAdapter(OrdersListActivity.this, orderItemArrayList);
                        // Attach the adapter to a ListView
                        ListView listView = (ListView) findViewById(R.id.orderList);
                        listView.setAdapter(adapter);
                        getListViewSize(listView);
                        progressBarForOrder.setVisibility(View.GONE);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(OrdersListActivity.this, OrderViewUser.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("orderViewConfig", "customer");
                                bundle.putString("orderId",orderItemArrayList.get(i).getOrderId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
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
    private void viewOrdersRestaurant(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurenIdForUse = pref.getString("restaurentId", "");
        ArrayList<String> orderIds = new ArrayList<String>();
        databaseReference.child("restaurents").child(restaurenIdForUse).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    orderIds.add(snapshot1.child("value").getValue(String.class));
                }
                databaseReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(String s : orderIds){
                            orderItemArrayList.add(new OrderItem(s
                                    ,snapshot.child(s).child("status").getValue(String.class)
                                    ,snapshot.child(s).child("deliveryboyname").getValue(String.class)
                                    ,snapshot.child(s).child("date").getValue(String.class)));
                        }
                        Collections.reverse(orderItemArrayList);
                        OrderItemAdapter adapter = new OrderItemAdapter(OrdersListActivity.this, orderItemArrayList);
                        // Attach the adapter to a ListView
                        ListView listView = (ListView) findViewById(R.id.orderList);
                        listView.setAdapter(adapter);
                        getListViewSize(listView);
                        progressBarForOrder.setVisibility(View.GONE);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(OrdersListActivity.this, OrderViewUser.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("orderViewConfig", "restaurent");
                                bundle.putString("orderId",orderItemArrayList.get(i).getOrderId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
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
    private void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));

    }
}