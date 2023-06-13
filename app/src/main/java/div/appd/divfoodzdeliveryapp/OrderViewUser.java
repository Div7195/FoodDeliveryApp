package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderViewUser extends AppCompatActivity {
    TextView restaurentNameView, restaurentAddressView, contactOfuserOrDelView, contactOfWhoView, totalBillView, taxView, priceView, deliveryFeeView, dateView, statusView;
    EditText customerAddressView;
    LinearLayout ll, llratingSectionView, llplaceOrderSectionView, llgetAssignedSectionView, llpickupSectionView, lldeliveredSectionView;
    Button placeOrderButton, getAssignedButton, pickupButton, deliveredButton, submitRatingButton, setAddressButton;
    String customerIdForUse, totalBillIntent, taxesIntent, deliveryIntent, totalPriceIntent, customerNameIntent, customerAddressIntent, customerContactIntent, restaurentIdIntent, restaurentNameIntent, restaurentAddressIntent, orderIdForUse, orderViewConfig, orderIdIntent, deliveryIdIntent;
    String fDeliveryBoyId, fDeliveryBoyName, fCustomerName,fCustomerContact,fDeliveryBoyContact, fCustomerAddress,fRestaurentName, fRestaurentAddress, fStatus, fDate;
    Double fTotalPrice, fTaxes, fTotalBill, fRating, fDeliveryFee;
    MaterialRatingBar materialRatingBar;
    ProgressBar progressBar;
    ArrayList<CartItemInfo> cartItemInfoArrayList;
    ArrayList<CartItemInfo> fetchedCartItems;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view_user);
        ll = findViewById(R.id.ll_parent_3);
        restaurentNameView = findViewById(R.id.headingRestaurentName);
        restaurentAddressView = findViewById(R.id.restaurentAddressOrder);
        customerAddressView = findViewById(R.id.customerAddressOrder);
        contactOfWhoView = findViewById(R.id.contactOfWhat);
        contactOfuserOrDelView = findViewById(R.id.contactOfUserOrDel);
        totalBillView = findViewById(R.id.totalAmountField);
        taxView = findViewById(R.id.gstChargeField);
        priceView = findViewById(R.id.itemTotalField);
        deliveryFeeView = findViewById(R.id.deliveryFeeField);
        dateView = findViewById(R.id.orderedTimeField);
        statusView = findViewById(R.id.statusOrderField);

        llratingSectionView = findViewById(R.id.ratingSection);
        llplaceOrderSectionView = findViewById(R.id.placeOrderSection);
        llgetAssignedSectionView = findViewById(R.id.getAssignedSection);
        lldeliveredSectionView = findViewById(R.id.deliveredSection);
        llpickupSectionView = findViewById(R.id.pickedUpSection);

        setAddressButton = findViewById(R.id.setCurrentAddressButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        getAssignedButton = findViewById(R.id.getAssignedButton);
        pickupButton = findViewById(R.id.pickedUpButton);
        deliveredButton = findViewById(R.id.deliveredButton);
        submitRatingButton = findViewById(R.id.ratingSubmitButton);

        progressBar = findViewById(R.id.progressBarinOrderView);
        materialRatingBar = findViewById(R.id.ratingBarOrderVIew);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundleData");
        if(args != null) {
            SharedPreferences pref = OrderViewUser.this.getSharedPreferences("MyPref", 0);
            customerIdForUse = pref.getString("customerId", "");
            llplaceOrderSectionView.setVisibility(View.VISIBLE);
            cartItemInfoArrayList = (ArrayList<CartItemInfo>) args.getSerializable("arraylistcartitems");
            totalBillIntent = (String) args.getString("totalbill");
            taxesIntent = (String) args.getString("taxes");
            deliveryIntent = (String) args.getString("deliveryfee");
            totalPriceIntent = (String) args.getString("totalprice");
            customerNameIntent = (String) args.getString("customerName");
            customerAddressIntent = (String) args.getString("customerAddress");
            customerContactIntent = (String) args.getString("customerContact");
            restaurentIdIntent = (String) args.getString("restaurentId");
            restaurentNameIntent = (String) args.getString("restaurentName");
            restaurentAddressIntent = (String) args.getString("restaurentAddress");

            restaurentNameView.setText(restaurentNameIntent);
            restaurentAddressView.setText(restaurentAddressIntent);
            customerAddressView.setText(customerAddressIntent);
            contactOfWhoView.setText("Contact");
            contactOfuserOrDelView.setText(customerContactIntent);

            totalBillView.setText(totalBillIntent);
            taxView.setText(taxesIntent);
            deliveryFeeView.setText(deliveryIntent);
            priceView.setText(totalPriceIntent);
            statusView.setText("Preparing..");

            dateView.setVisibility(View.GONE);
            setAddressButton.setVisibility(View.VISIBLE);
            CartCheckOutAdapter adapter = new CartCheckOutAdapter(OrderViewUser.this, cartItemInfoArrayList);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.itemsOrderedList);
            listView.setAdapter(adapter);
            listView.setTag(ll);
            getListViewSize(listView);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(OrderViewUser.this, "Place order now!", Toast.LENGTH_SHORT).show();
        }else{
            orderViewConfig = intent.getStringExtra("orderViewConfig");
            orderIdIntent = getIntent().getStringExtra("orderId");
            Toast.makeText(this, "order id is" + orderIdIntent, Toast.LENGTH_SHORT).show();
            deliveryIdIntent = getIntent().getStringExtra("deliveryBoyId");
            customerAddressView.setFocusable(false);
            databaseReference.child("orders").child(orderIdIntent).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fDeliveryBoyId = snapshot.child("deliveryboyid").getValue(String.class);
                    fCustomerName = snapshot.child("customername").getValue(String.class);
                    fDeliveryBoyName = snapshot.child("deliveryboyname").getValue(String.class);
                    fRestaurentName = snapshot.child("restaurentname").getValue(String.class);
                    fCustomerContact = snapshot.child("customercontact").getValue(String.class);
                    fDeliveryBoyContact = snapshot.child("deliveryboycontact").getValue(String.class);
                    fCustomerAddress = snapshot.child("customeraddress").getValue(String.class);
                    fRestaurentAddress = snapshot.child("restaurentaddress").getValue(String.class);
                    fStatus = snapshot.child("status").getValue(String.class);
                    fDate = snapshot.child("date").getValue(String.class);
                    fRating = snapshot.child("rating").getValue(Double.class);
                    fTotalPrice = snapshot.child("totalprice").getValue(Double.class);
                    fTotalBill = snapshot.child("totalbill").getValue(Double.class);
                    fTaxes = snapshot.child("taxes").getValue(Double.class);
                    fDeliveryFee = 30.0;
                    fetchedCartItems = new ArrayList<CartItemInfo>();
                    for(DataSnapshot snapshot1 : snapshot.child("items").getChildren()){
                        fetchedCartItems.add(new CartItemInfo(snapshot1.child("dishid").getValue(String.class)
                        ,snapshot1.child("restaurentid").getValue(String.class)
                        ,snapshot1.child("dishname").getValue(String.class)
                        ,snapshot1.child("quantity").getValue(Integer.class)
                        ,snapshot1.child("price").getValue(Double.class)
                        ,snapshot1.child("single").getValue(Double.class)
                        ,snapshot1.child("vegnonveg").getValue(String.class)));
                    }
                    restaurentNameView.setText(fRestaurentName);
                    restaurentAddressView.setText(fRestaurentAddress);
                    customerAddressView.setText(fCustomerAddress);
                    totalBillView.setText(String.valueOf(fTotalBill.floatValue()));
                    priceView.setText(String.valueOf(fTotalPrice.floatValue()));
                    taxView.setText(String.valueOf(fTaxes.floatValue()));
                    deliveryFeeView.setText(String.valueOf(fDeliveryFee.floatValue()));
                    dateView.setText(fDate);
                    statusView.setText(fStatus);

                    if(orderViewConfig.equals("customer")){
                    contactOfWhoView.setText("Delivery Partner Contact");
                    if(fDeliveryBoyName == null){
                        contactOfuserOrDelView.setText("Delivery Partner will be assigned shortly");
                    }else{
                        contactOfuserOrDelView.setText(fDeliveryBoyContact);
                    }
                    if(fStatus.equals("Delivered")){
                        llratingSectionView.setVisibility(View.VISIBLE);
                    }


                    } else if (orderViewConfig.equals("restaurent")) {
                        contactOfWhoView.setText("Customer Contact");
                        contactOfuserOrDelView.setText(fCustomerContact);


                    } else if (orderViewConfig.equals("deliveryboy")) {
                        contactOfWhoView.setText("Customer Contact");
                        contactOfuserOrDelView.setText(fCustomerContact);
                        if(fDeliveryBoyName == null){
                            llgetAssignedSectionView.setVisibility(View.VISIBLE);
                        } else if (fStatus.equals("Picked Up")) {
                            lldeliveredSectionView.setVisibility(View.VISIBLE);
                        } else if (fStatus.equals("Being Prepared")) {
                            llpickupSectionView.setVisibility(View.VISIBLE);
                        }


                    }




                    AfterOrderCartAdapter adapter = new AfterOrderCartAdapter(OrderViewUser.this, fetchedCartItems);
                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.itemsOrderedList);
                    listView.setAdapter(adapter);
                    listView.setTag(ll);
                    getListViewSize(listView);
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        setAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Double.valueOf(totalBillView.getText().toString()) == 0.0){
                    Toast.makeText(OrderViewUser.this, "Your Cart is empty, can't place order", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("orders").push().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child("customerid").getRef().setValue(customerIdForUse);
                            snapshot.child("restaurentid").getRef().setValue(restaurentIdIntent);
                            snapshot.child("customername").getRef().setValue(customerNameIntent);
                            snapshot.child("restaurentname").getRef().setValue(restaurentNameIntent);
                            snapshot.child("customeraddress").getRef().setValue(customerAddressIntent);
                            snapshot.child("restaurentaddress").getRef().setValue(restaurentAddressIntent);
                            snapshot.child("customercontact").getRef().setValue(customerContactIntent);
                            snapshot.child("status").getRef().setValue("Being Prepared");
                            snapshot.child("date").getRef().setValue(new Date().toString());
                            snapshot.child("totalprice").getRef().setValue(Double.valueOf(priceView.getText().toString()));
                            snapshot.child("taxes").getRef().setValue(Double.valueOf(taxView.getText().toString()));
                            snapshot.child("deliveryfee").getRef().setValue(30.0);
                            snapshot.child("totalbill").getRef().setValue(Double.valueOf(totalBillView.getText().toString()));
                            orderIdForUse = snapshot.getKey();
                            databaseReference.child("customers").child(customerIdForUse).child("orders").push().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.child("value").getRef().setValue(orderIdForUse);
                                    databaseReference.child("restaurents").child(restaurentIdIntent).child("orders").push().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.child("value").getRef().setValue(orderIdForUse);

                                            for(CartItemInfo obj : cartItemInfoArrayList){
                                                databaseReference.child("orders").child(orderIdForUse).child("items").getRef().push().addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        snapshot.child("dishid").getRef().setValue(obj.getDishId());
                                                        snapshot.child("restaurentid").getRef().setValue(obj.getRestaurentId());
                                                        snapshot.child("dishname").getRef().setValue(obj.getDishName());
                                                        snapshot.child("quantity").getRef().setValue(obj.getQuanity());
                                                        snapshot.child("price").getRef().setValue(obj.getPrice());
                                                        snapshot.child("single").getRef().setValue(obj.getSingleItemPrice());
                                                        snapshot.child("vegnonveg").getRef().setValue(obj.getVegNonVeg());
                                                        Toast.makeText(OrderViewUser.this, "Done!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        getAssignedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("deliveryboys").child(deliveryIdIntent).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue(String.class);
                        String contact = snapshot.child("contact").getValue(String.class);
                        databaseReference.child("deliveryboys").child(deliveryIdIntent).child("orders").push().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.child("value").getRef().setValue(orderIdIntent);
                                DatabaseReference nodeRef2 = databaseReference.child("orders").child(orderIdIntent);
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("deliveryboyid", deliveryIdIntent);
                                updates.put("deliveryboycontact", contact);
                                updates.put("deliveryboyname", name);
                                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(OrderViewUser.this, "Order is assigned to you!", Toast.LENGTH_SHORT).show();
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
        });

        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference nodeRef2 = databaseReference.child("orders").child(orderIdIntent);
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "Picked Up");
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(OrderViewUser.this, "Updated status!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference nodeRef2 = databaseReference.child("orders").child(orderIdIntent);
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "Delivered");
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(OrderViewUser.this, "Updated Status!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double BarRating = Double.valueOf(materialRatingBar.getRating());
                DatabaseReference nodeRef2 = databaseReference.child("orders").child(orderIdIntent);
                Map<String, Object> updates = new HashMap<>();
                updates.put("rating", BarRating.floatValue());
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(CartItemInfo obj : fetchedCartItems){
                                    Integer timesRated;
                                    Double rating;
                                    timesRated = snapshot.child(obj.getDishId()).child("timesOrdered").getValue(Integer.class);
                                    rating = snapshot.child(obj.getDishId()).child("rating").getValue(Double.class);
                                    timesRated = timesRated + 1;
                                    rating = rating + BarRating.floatValue();
                                    rating = rating/timesRated;
                                    DatabaseReference nodeRef2 = databaseReference.child("dishes").child(obj.getDishId());
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("rating", rating);
                                    updates.put("timesOrdered", timesRated);
                                    nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(OrderViewUser.this, "Updated Rating in dishes!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });



    }
    private void getLastLocation() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

//                    Toast.makeText(MainActivity.this, "fdsfsad", Toast.LENGTH_SHORT).show();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(OrderViewUser.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String currentAddress = addresses.get(0).getAddressLine(0);
                            customerAddressView.setText(currentAddress);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(OrderViewUser.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderViewUser.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            ActivityCompat.requestPermissions(OrderViewUser.this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Toast.makeText(OrderViewUser.this, "Location is turned off. Turn on location and refresh/restart app", Toast.LENGTH_LONG).show();
                        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                }
            });
        } else {
            askPermission();

        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(OrderViewUser.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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