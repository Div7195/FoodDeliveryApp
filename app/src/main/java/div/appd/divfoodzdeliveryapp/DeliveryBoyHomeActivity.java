package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryBoyHomeActivity extends AppCompatActivity {
    Button viewOrdersButton, viewAssignedOrdersButton, editDetailsButton;
    String deliveryBoyIdForUse;
    Boolean eligibleForUse;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_home);
        viewOrdersButton = findViewById(R.id.viewOrderDeliveryButton);
        viewAssignedOrdersButton = findViewById(R.id.viewAssignedOrdersButton);
        editDetailsButton = findViewById(R.id.editDeliveryDetailsButton);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        deliveryBoyIdForUse = pref.getString("deliveryBoyId", "");
        databaseReference.child("customers").child(deliveryBoyIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eligibleForUse = snapshot.child("eligible").getValue(Boolean.class);
                if(eligibleForUse != null){
                    viewOrdersButton.setEnabled(true);
                    viewAssignedOrdersButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryBoyHomeActivity.this, DeliveryBoyDetailsActivity.class);

                startActivity(intent);
            }
        });
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewAssignedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}