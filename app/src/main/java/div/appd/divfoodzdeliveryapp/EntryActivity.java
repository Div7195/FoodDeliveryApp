package div.appd.divfoodzdeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fooddelivery.R;


public class EntryActivity extends AppCompatActivity {
    Button userEntry, restaurentEntry, deliveryBoyEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        userEntry = findViewById(R.id.userEntry);
        restaurentEntry = findViewById(R.id.restaurentEntry);
        deliveryBoyEntry = findViewById(R.id.deliveryEntry);
        userEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("entryRole", "user");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        restaurentEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("entryRole", "restaurent");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        deliveryBoyEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("entryRole", "deliveryboy");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}