package div.appd.divfoodzdeliveryapp;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RestaurentDetailsActivity extends AppCompatActivity {
    EditText restaurentNameView, restaurentCityView, restaurentStateView, restaurentContactView, restaurentAddressView, restaurentCuisineView, restaurentLocalityView;
    Button chooseImageView, uploadImageView, saveDetailsView, setAddressAsCurrentLocationView;
    ProgressBar progressBar;
    String nameString, cityString, stateString, contactString, addressString, cuisineString, restaurentIdForUse, downloadableImageUrl, localityString;
    FusedLocationProviderClient fusedLocationProviderClient;
    Restaurent restaurentObj;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    private final static int REQUEST_CODE = 100;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_details);
        restaurentNameView = findViewById(R.id.restaurentNameField);
        restaurentCityView = findViewById(R.id.cityField);
        restaurentStateView = findViewById(R.id.stateField);
        restaurentContactView = findViewById(R.id.contactField);
        restaurentAddressView = findViewById(R.id.addressField);
        restaurentCuisineView = findViewById(R.id.foodServiceField);
        restaurentLocalityView = findViewById(R.id.localityRestaurentField);
        progressBar = findViewById(R.id.progressBarRestaurentDetails);
        chooseImageView = findViewById(R.id.chooseImageButton);
        uploadImageView = findViewById(R.id.uploadRestoImageButton);
        saveDetailsView = findViewById(R.id.saveRestaurentButton);
        setAddressAsCurrentLocationView = findViewById(R.id.setAddressButton);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://food-delivery-app-91b3a.appspot.com");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurentIdForUse = pref.getString("restaurentId", "");
        databaseReference.child("restaurents").child(restaurentIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> categoriesList = new ArrayList<>();
                ArrayList<String> dishidList = new ArrayList<>();
                for(DataSnapshot categoriesSnapshot : snapshot.child("categories").getChildren()){
                    categoriesList.add(categoriesSnapshot.child("categoryName").getValue(String.class));
                }
                for(DataSnapshot dishidSnapshot : snapshot.child("fooditems").getChildren()){
                    dishidList.add(dishidSnapshot.child("dishId").getValue(String.class));
                }


                 restaurentObj = new Restaurent(snapshot.child("name").getValue(String.class)
                ,snapshot.child("address").getValue(String.class)
                         ,snapshot.child("locality").getValue(String.class)
                ,snapshot.child("cuisines").getValue(String.class)
                ,snapshot.child("city").getValue(String.class)
                ,snapshot.child("state").getValue(String.class)
                ,snapshot.child("imageurl").getValue(String.class)
                ,snapshot.child("contact").getValue(String.class)
                ,categoriesList
                ,dishidList
                ,snapshot.child("restaurentId").getValue(String.class)
                         ,snapshot.child("eligible").getValue(Boolean.class));

                restaurentNameView.setText(restaurentObj.getName());
                restaurentCityView.setText(restaurentObj.getCity());
                restaurentStateView.setText(restaurentObj.getState());
                restaurentContactView.setText(restaurentObj.getContact());
                restaurentAddressView.setText(restaurentObj.getAddress());
                restaurentCuisineView.setText(restaurentObj.getCuisines());
                restaurentLocalityView.setText(restaurentObj.getLocality());
//                enabling buttons
                chooseImageView.setEnabled(true);
                setAddressAsCurrentLocationView.setEnabled(true);

                saveDetailsView.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        saveDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                nameString = restaurentNameView.getText().toString();
                cityString = restaurentCityView.getText().toString();
                stateString = restaurentStateView.getText().toString();
                contactString = restaurentContactView.getText().toString();
                addressString = restaurentAddressView.getText().toString();
                cuisineString = restaurentCuisineView.getText().toString();
                localityString = restaurentLocalityView.getText().toString();
                if (nameString.isEmpty() || cityString.isEmpty() || stateString.isEmpty() || contactString.isEmpty() || addressString.isEmpty() || cuisineString.isEmpty() || localityString.isEmpty()) {
                    Toast.makeText(RestaurentDetailsActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                databaseReference.child("restaurents").child(restaurentIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (!restaurentNameView.getText().toString().equals(restaurentObj.getName())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("name", restaurentNameView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentCityView.getText().toString().equals(restaurentObj.getCity())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("city", restaurentCityView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentStateView.getText().toString().equals(restaurentObj.getState())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("state", restaurentStateView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentContactView.getText().toString().equals(restaurentObj.getContact())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("contact", restaurentContactView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentLocalityView.getText().toString().equals(restaurentObj.getLocality())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("locality", restaurentLocalityView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentAddressView.getText().toString().equals(restaurentObj.getAddress())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("address", restaurentAddressView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if (!restaurentCuisineView.getText().toString().equals(restaurentObj.getCuisines())) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("cuisines", restaurentCuisineView.getText().toString());
                            snapshot.getRef().updateChildren(updates);
                        }
                        if(downloadableImageUrl!=null) {
                            if (!downloadableImageUrl.equals(restaurentObj.getImageUrl())) {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("imageUrl", downloadableImageUrl);
                                snapshot.getRef().updateChildren(updates);
                            }
                        }
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("eligible", true);
                        snapshot.getRef().updateChildren(updates);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("restaurentName", restaurentNameView.getText().toString());
                        editor.apply();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RestaurentDetailsActivity.this, "Saved Changes!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RestaurentDetailsActivity.this, RestaurentHomeActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            }
        });
        setAddressAsCurrentLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLastLocation();
            }
        });


        chooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLaunch.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            filePath = data.getData();
                            Toast.makeText(RestaurentDetailsActivity.this, "Image has been chosen", Toast.LENGTH_SHORT).show();
                            uploadImageView.setEnabled(true);
                        }
                    }
                }
            });

    private void uploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {// Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(RestaurentDetailsActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUrl) {
                                            String imageUrl = downloadUrl.toString();
                                            downloadableImageUrl = imageUrl;
                                            Toast.makeText(RestaurentDetailsActivity.this, downloadableImageUrl, Toast.LENGTH_SHORT).show();
                                            // Use the imageUrl as needed
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors while retrieving the download URL
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {// Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(RestaurentDetailsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
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
                        Geocoder geocoder = new Geocoder(RestaurentDetailsActivity.this, Locale.getDefault());
                        List<Address> addresses = null;

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String currentAddress = addresses.get(0).getSubLocality();
                            String currentLocality = addresses.get(0).getAddressLine(0);
                                restaurentAddressView.setText(currentLocality);
                                restaurentLocalityView.setText(currentAddress);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(RestaurentDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RestaurentDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            ActivityCompat.requestPermissions(RestaurentDetailsActivity.this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Toast.makeText(RestaurentDetailsActivity.this, "Location is turned off. Turn on location and refresh/restart app", Toast.LENGTH_LONG).show();
                        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                }
            });
        } else {
            askPermission();

        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(RestaurentDetailsActivity.this, new String[]
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