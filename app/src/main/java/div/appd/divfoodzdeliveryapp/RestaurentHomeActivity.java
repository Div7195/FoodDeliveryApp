package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RestaurentHomeActivity extends AppCompatActivity {
    Button openAddView, openEditMenuView, openEditDetailView, openViewOrdersView;
    String restaurentIdForUse;
    Boolean eligibleForUse;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_home);
        openAddView = findViewById(R.id.openAddItemButton);
        openEditMenuView = findViewById(R.id.openEditMenuButton);
        openEditDetailView = findViewById(R.id.openEditRestaurentButton);
        openViewOrdersView = findViewById(R.id.openViewOrdersButton);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurentIdForUse = pref.getString("restaurentId", "");
        databaseReference.child("restaurents").child(restaurentIdForUse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eligibleForUse = snapshot.child("eligible").getValue(Boolean.class);
                if(eligibleForUse != null){
                    openAddView.setEnabled(true);
                    openEditMenuView.setEnabled(true);
                    openViewOrdersView.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        openAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(RestaurentHomeActivity.this, AddItemActivity.class);
                mIntent.putExtra("action", "add");
                startActivity(mIntent);
            }
        });

        openEditMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurentHomeActivity.this, MenuEditActivity.class);
                startActivity(intent);
            }
        });

        openEditDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurentHomeActivity.this, RestaurentDetailsActivity.class);
                startActivity(intent);

            }
        });

        openViewOrdersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}