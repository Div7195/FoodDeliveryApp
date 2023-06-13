package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView headingView;
    EditText usernameFieldView, passwordFieldView;
    Button loginButton, signupButton;
    Switch saveLoginDetails;
    String userUsername, userPassword, restaurentIdForUse, customerIdForUse, restaurentNameForUse, customerNameForUse, deliveryBoyIdForUse, deliveryBoyNameForUse;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        headingView = findViewById(R.id.headingViewLogin);
        usernameFieldView = findViewById(R.id.usernameField);
        passwordFieldView = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        Bundle bundle = getIntent().getExtras();
        String entryActor = bundle.getString("entryRole", "");
        if(entryActor.equals("user")){
            headingView.setText("Welcome User");
        } else if (entryActor.equals("restaurent")) {
            headingView.setText("Restaurent");
        } else if (entryActor.equals("deliveryboy")) {
            headingView.setText("Delivery Staff");
        } else if (entryActor.equals("admin")) {
            headingView.setText("Administrator");
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userUsername = usernameFieldView.getText().toString();
                userPassword = passwordFieldView.getText().toString();
                if(userUsername.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(entryActor.equals("user")){
                        databaseReference.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String storedPassword = "";
                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    String customerId = customerSnapshot.getKey();
                                    DataSnapshot usernameSnapshot = customerSnapshot.child("username");
                                    if (usernameSnapshot.exists() && usernameSnapshot.getValue().equals(userUsername)) {
                                        storedPassword = customerSnapshot.child("password").getValue(String.class);
                                        customerIdForUse = customerId;
                                        // Found a restaurant with matching usernamea
                                        // Use the retrieved restaurant ID (restaurantId)
                                        // For example, perform further operations or retrieve other data related to the restaurant
                                        break; // Exit the loop after finding the match
                                    }
                                }
                                if(storedPassword.equals("")){
                                    Toast.makeText(LoginActivity.this, "No customer exists with this username", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(storedPassword.equals(userPassword)){

                                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("customerId", customerIdForUse);
                                        editor.putString("customerName", customerNameForUse);
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, CustomerHomeActivity.class);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("restaurentId", restaurentIdForUse);
//                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if (entryActor.equals("restaurent")) {
                        databaseReference.child("restaurents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String storedPassword = "";
                                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                                    String restaurantId = restaurantSnapshot.getKey();
                                    DataSnapshot usernameSnapshot = restaurantSnapshot.child("username");
                                    if (usernameSnapshot.exists() && usernameSnapshot.getValue().equals(userUsername)) {
                                        storedPassword = restaurantSnapshot.child("password").getValue(String.class);
                                        restaurentIdForUse = restaurantId;
                                        // Found a restaurant with matching usernamea
                                        // Use the retrieved restaurant ID (restaurantId)
                                        // For example, perform further operations or retrieve other data related to the restaurant
                                        break; // Exit the loop after finding the match
                                    }
                                }
                                if(storedPassword.equals("")){
                                    Toast.makeText(LoginActivity.this, "No restaurent exists with this username", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(storedPassword.equals(userPassword)){

                                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("restaurentId", restaurentIdForUse);
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, RestaurentHomeActivity.class);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("restaurentId", restaurentIdForUse);
//                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else if (entryActor.equals("deliveryboy")) {

                        databaseReference.child("deliveryboys").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String storedPassword = "";
                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    String deliveryId = customerSnapshot.getKey();
                                    DataSnapshot usernameSnapshot = customerSnapshot.child("username");
                                    if (usernameSnapshot.exists() && usernameSnapshot.getValue().equals(userUsername)) {
                                        storedPassword = customerSnapshot.child("password").getValue(String.class);
                                        deliveryBoyIdForUse = deliveryId;
                                        break;
                                    }
                                }
                                if(storedPassword.equals("")){
                                    Toast.makeText(LoginActivity.this, "No delivery boy exists with this username", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(storedPassword.equals(userPassword)){

                                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("deliveryBoyId", deliveryBoyIdForUse);
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, DeliveryBoyHomeActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("entryRole", entryActor);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

//        if(saveLoginDetails.isChecked()){
//
//        }

    }
}
