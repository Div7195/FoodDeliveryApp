package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryBoyDetailsActivity extends AppCompatActivity {
    EditText deliveryNameView, deliveryStateView, deliveryCityView, deliveryContactView, userEmailView, userContactView;
    ProgressBar progressBar;
    Button saveDetailsOfDeliveryButtonView;
    String deliveryBoyIdForUse, nameString, cityString, stateString, contactString;
    DeliveryBoy deliveryBoyObj;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_details);
        deliveryNameView = findViewById(R.id.deliveryNameField);
        deliveryCityView = findViewById(R.id.deliveryCityField);
        deliveryContactView = findViewById(R.id.deliveryContactField);
        deliveryStateView = findViewById(R.id.deliveryStateField);
        progressBar = findViewById(R.id.progressBarDeliveryDetails);
        saveDetailsOfDeliveryButtonView = findViewById(R.id.deliverySaveDetailsButton);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        deliveryBoyIdForUse = pref.getString("deliveryBoyId", "");
        databaseReference.child("deliveryboys").child(deliveryBoyIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> orderIdsList = new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.child("orders").getChildren()){
                    orderIdsList.add(dataSnapshot.child("value").getValue(String.class));
                }


                deliveryBoyObj = new DeliveryBoy(snapshot.child("name").getValue(String.class)
                        ,snapshot.child("city").getValue(String.class)
                        ,snapshot.child("state").getValue(String.class)
                        ,snapshot.child("contact").getValue(String.class)
                        ,snapshot.child("eligible").getValue(Boolean.class)
                        ,orderIdsList
                        ,snapshot.child("deliveryBoyId").getValue(String.class));

                deliveryNameView.setText(deliveryBoyObj.getName());
                deliveryCityView.setText(deliveryBoyObj.getCity());
                deliveryStateView.setText(deliveryBoyObj.getState());
                deliveryContactView.setText(deliveryBoyObj.getContact());

                saveDetailsOfDeliveryButtonView.setEnabled(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveDetailsOfDeliveryButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                nameString = deliveryNameView.getText().toString();
                cityString = deliveryCityView.getText().toString();
                stateString = deliveryStateView.getText().toString();
                contactString = deliveryContactView.getText().toString();
                if(nameString.isEmpty() || cityString.isEmpty() || stateString.isEmpty() || contactString.isEmpty()){
                    Toast.makeText(DeliveryBoyDetailsActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("deliveryboys").child(deliveryBoyIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!deliveryNameView.getText().toString().equals(deliveryBoyObj.getName())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", deliveryNameView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!deliveryCityView.getText().toString().equals(deliveryBoyObj.getCity())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("city", deliveryCityView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!deliveryStateView.getText().toString().equals(deliveryBoyObj.getState())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("state", deliveryStateView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            if(!deliveryContactView.getText().toString().equals(deliveryBoyObj.getContact())){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("contact", deliveryContactView.getText().toString());
                                snapshot.getRef().updateChildren(updates);
                            }
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("eligible", true);
                            snapshot.getRef().updateChildren(updates);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(DeliveryBoyDetailsActivity.this, "Saved Changes!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DeliveryBoyDetailsActivity.this, DeliveryBoyHomeActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}