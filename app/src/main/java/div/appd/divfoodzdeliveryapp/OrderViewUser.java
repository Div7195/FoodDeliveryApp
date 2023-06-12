package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderViewUser extends AppCompatActivity {
    TextView restaurentNameView, restaurentAddressView, customerAddressView, contactOfuserOrDelView, contactOfWhoView, totalBillView, taxView, priceView, deliveryFeeView, dateView, statusView;
    LinearLayout ll, llratingSectionView, llplaceOrderSectionView, llgetAssignedSectionView, llpickupSectionView, lldeliveredSectionView;
    Button placeOrderButton, getAssignedButton, pickupButton, deliveredButton, submitRatingButton, setAddressButton;
    String customerIdForUse, totalBillIntent, taxesIntent, deliveryIntent, totalPriceIntent, customerNameIntent, customerAddressIntent, customerContactIntent, restaurentIdIntent, restaurentNameIntent, restaurentAddressIntent;
    MaterialRatingBar materialRatingBar;
    ProgressBar progressBar;
    ArrayList<CartItemInfo> cartItemInfoArrayList;
    FusedLocationProviderClient fusedLocationProviderClient;
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
        cartItemInfoArrayList = (ArrayList<CartItemInfo>) args.getSerializable("arraylistcartitems");
        customerIdForUse = (String) args.getString("customerId");
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
        Date d = new Date();
        dateView.setText(d.toString()) ;

        CartCheckOutAdapter adapter = new CartCheckOutAdapter(OrderViewUser.this, cartItemInfoArrayList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.itemsOrderedList);
        listView.setAdapter(adapter);
        listView.setTag(ll);
        getListViewSize(listView);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(OrderViewUser.this, "OrderViewUser and adapter is cartCheckoutadapter", Toast.LENGTH_SHORT).show();
        setAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
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