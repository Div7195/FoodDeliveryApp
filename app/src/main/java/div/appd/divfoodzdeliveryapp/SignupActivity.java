package div.appd.divfoodzdeliveryapp;
import div.appd.divfoodzdeliveryapp.RandomStringGenerator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    TextView headingSignup;
    EditText usernameField, passwordField, confirmPasswordField;
    Button accountSignup, goToLoginView;
    String username, password, confirmPassword;
    ProgressBar progressBar;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        headingSignup = findViewById(R.id.signupHeadingView);
        usernameField = findViewById(R.id.usernameSignupField);
        passwordField = findViewById(R.id.passwordSignupField);
        confirmPasswordField = findViewById(R.id.confirmPasswordSignupField);
        accountSignup = findViewById(R.id.accountSignupButton);
        goToLoginView = findViewById(R.id.goToLoginButton);
        progressBar = findViewById(R.id.progressBarInSignup);
        Bundle bundle = getIntent().getExtras();
        String entryActor = bundle.getString("entryRole", "");
        if(entryActor.equals("user")){
            headingSignup.setText("User Signup");
        } else if (entryActor.equals("restaurent")) {
            headingSignup.setText("Restaurent Signup");
        } else if (entryActor.equals("deliveryboy")) {
            headingSignup.setText("Delivery Staff Signup");
        } else if (entryActor.equals("admin")) {
            headingSignup.setText("Administrator Signup");
        }
        accountSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                confirmPassword = passwordField.getText().toString();
                if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 2 || password.length() < 2 || confirmPassword.length() < 2) {
                    Toast.makeText(SignupActivity.this, "Fields must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Password fields must be equal", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(entryActor.equals("user")){
                        progressBar.setVisibility(View.VISIBLE);
                        databaseReference.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
                            //                            String restaurantId = RandomStringGenerator.generateRandomString(10);
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean customerExists = false;

                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    String usernameI = customerSnapshot.child("username").getValue(String.class);

                                    if (usernameI != null && username.equals(usernameI)) {
                                        customerExists = true;
                                        break;
                                    }
                                }

                                if (customerExists) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignupActivity.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference restaurentReference = databaseReference.child("customers").push();
                                    restaurentReference.child("username").setValue(username);
                                    restaurentReference.child("password").setValue(password);
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("entryRole", "user");
                                    intent.putExtras(bundle);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(intent);
                                    Toast.makeText(SignupActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                            }
                        });

                    }
                    else if(entryActor.equals("restaurent")){
                        progressBar.setVisibility(View.VISIBLE);
                        databaseReference.child("restaurents").addListenerForSingleValueEvent(new ValueEventListener() {
//                            String restaurantId = RandomStringGenerator.generateRandomString(10);
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean restaurantExists = false;

                                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                                    String usernameI = restaurantSnapshot.child("username").getValue(String.class);

                                    if (usernameI != null && username.equals(usernameI)) {
                                        restaurantExists = true;
                                        break;
                                    }
                                }

                                if (restaurantExists) {
                                    Toast.makeText(SignupActivity.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    DatabaseReference restaurentReference = databaseReference.child("restaurents").push();
                                    restaurentReference.child("username").setValue(username);
                                    restaurentReference.child("password").setValue(password);
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("entryRole", "restaurent");
                                    intent.putExtras(bundle);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(intent);
                                    Toast.makeText(SignupActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                            }
                        });


                    }
                    else if(entryActor.equals("deliveryboy")){
                        progressBar.setVisibility(View.VISIBLE);
                        databaseReference.child("deliveryboys").addListenerForSingleValueEvent(new ValueEventListener() {
                            //                            String restaurantId = RandomStringGenerator.generateRandomString(10);
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean deliveryBoyExists = false;

                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    String usernameI = customerSnapshot.child("username").getValue(String.class);

                                    if (usernameI != null && username.equals(usernameI)) {
                                        deliveryBoyExists = true;
                                        break;
                                    }
                                }

                                if (deliveryBoyExists) {
                                    Toast.makeText(SignupActivity.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    DatabaseReference restaurentReference = databaseReference.child("deliveryboys").push();
                                    restaurentReference.child("username").setValue(username);
                                    restaurentReference.child("password").setValue(password);
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("entryRole", "deliveryboy");
                                    intent.putExtras(bundle);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(intent);
                                    Toast.makeText(SignupActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                            }
                        });

                    }


                }
            }
        });

        goToLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}