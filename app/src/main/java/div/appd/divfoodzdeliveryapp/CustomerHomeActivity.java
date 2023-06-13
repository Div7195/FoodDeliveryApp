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

public class CustomerHomeActivity extends AppCompatActivity {

    Button openUserDetailsView, openExploreDishesView, openExploreRestoView, openViewOrdersView;
    String customerIdForUse;
    Boolean eligibleForUse;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        openUserDetailsView = findViewById(R.id.openEditCustoButton);
        openExploreDishesView = findViewById(R.id.openExploreDishButton);
        openExploreRestoView = findViewById(R.id.openExploreRestoButton);
        openViewOrdersView = findViewById(R.id.openExploreOrdersButton);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        customerIdForUse = pref.getString("customerId", "");
        databaseReference.child("customers").child(customerIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eligibleForUse = snapshot.child("eligible").getValue(Boolean.class);
                if(eligibleForUse != null){
                    openExploreDishesView.setEnabled(true);
                    openExploreRestoView.setEnabled(true);
                    openViewOrdersView.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        openUserDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, CustomerDetailsActivity.class);

                startActivity(intent);
            }
        });

        openExploreDishesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, DishtypeExploreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("actionExplore", "exploreDishtypes");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        openExploreRestoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, DishtypeExploreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("actionExplore", "exploreRestaurents");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        openViewOrdersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, OrdersListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accessOrders", "customer");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}