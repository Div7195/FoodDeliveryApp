package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {
    EditText userNameView, userCityView, userStateView, userAddressView, userEmailView, userContactView;
    ProgressBar progressBar;
    Button setAddressUserButtonView, saveDetailsOfUserButtonView;
    String customerIdForUse, nameString, cityString, stateString, contactString, addressString, emailString;
    Customer customerObj;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        userNameView = findViewById(R.id.userNameField);
        userCityView = findViewById(R.id.userCityField);
        userStateView = findViewById(R.id.userStateField);
        userAddressView = findViewById(R.id.userAddressField);
        userEmailView = findViewById(R.id.userEmailField);
        userContactView = findViewById(R.id.userContactField);
        progressBar = findViewById(R.id.progressBarCustomerDetails);
        setAddressUserButtonView = findViewById(R.id.setUserAddressButton);
        saveDetailsOfUserButtonView = findViewById(R.id.saveDetailsUserButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        customerIdForUse = pref.getString("customerId", "");

        databaseReference.child("customers").child(customerIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> orderIdsList = new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.child("orderIds").getChildren()){
                    orderIdsList.add(dataSnapshot.child("value").getValue(String.class));
                }


                customerObj = new Customer(snapshot.child("name").getValue(String.class)
                        ,snapshot.child("address").getValue(String.class)
                ,snapshot.child("city").getValue(String.class)
                ,snapshot.child("state").getValue(String.class)
                ,snapshot.child("contact").getValue(String.class)
                ,snapshot.child("email").getValue(String.class)
                ,snapshot.child("eligible").getValue(Boolean.class)
                ,orderIdsList
                        ,snapshot.child("customerId").getValue(String.class));

                userNameView.setText(customerObj.getName());
                userAddressView.setText(customerObj.getAddress());
                userCityView.setText(customerObj.getCity());
                userStateView.setText(customerObj.getState());
                userContactView.setText(customerObj.getContact());
                userEmailView.setText(customerObj.getEmail());


                setAddressUserButtonView.setEnabled(true);
                saveDetailsOfUserButtonView.setEnabled(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveDetailsOfUserButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                nameString = userNameView.getText().toString();
                addressString = userAddressView.getText().toString();
                cityString = userCityView.getText().toString();
                stateString = userStateView.getText().toString();
                contactString = userContactView.getText().toString();
                emailString = userEmailView.getText().toString();

                if(nameString.isEmpty() || addressString.isEmpty() || cityString.isEmpty() || stateString.isEmpty() || contactString.isEmpty()){
                    Toast.makeText(CustomerDetailsActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("customers").child(customerIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!userNameView.getText().toString().equals(customerObj.getName())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", userNameView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!userAddressView.getText().toString().equals(customerObj.getAddress())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("address", userAddressView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!userCityView.getText().toString().equals(customerObj.getCity())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("city", userCityView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!userStateView.getText().toString().equals(customerObj.getState())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("state", userStateView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!userContactView.getText().toString().equals(customerObj.getContact())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("contact", userContactView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(userEmailView.getText().toString()!=null) {
                                if (!userEmailView.getText().toString().equals(customerObj.getEmail())) {
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("email", userEmailView.getText().toString());
                                    snapshot.getRef().updateChildren(updates);
                                }
                            }
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("eligible", true);
                            snapshot.getRef().updateChildren(updates);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CustomerDetailsActivity.this, "Saved Changes!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        setAddressUserButtonView.setOnClickListener(new View.OnClickListener() {
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
                        Geocoder geocoder = new Geocoder(CustomerDetailsActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String currentAddress = addresses.get(0).getAddressLine(0);
                            userAddressView.setText(currentAddress);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(CustomerDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            ActivityCompat.requestPermissions(CustomerDetailsActivity.this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Toast.makeText(CustomerDetailsActivity.this, "Location is turned off. Turn on location and refresh/restart app", Toast.LENGTH_LONG).show();
                        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                }
            });
        } else {
            askPermission();

        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(CustomerDetailsActivity.this, new String[]
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
}