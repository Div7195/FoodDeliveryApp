package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DishtypeExploreActivity extends AppCompatActivity {

    ProgressBar progressBarForDishTypes;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    ArrayList<String> arrayOfDishIds = new ArrayList<String>();
    ArrayList<Dishtype> arrayOfDishTypes = new ArrayList<Dishtype>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishtype_explore);
        progressBarForDishTypes = findViewById(R.id.progressBarExploreDishtype);
        databaseReference.child("dishtags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ArrayList<String> dishIdLocalArray = new ArrayList<String>();
                    for(DataSnapshot snapshot2 : snapshot1.child("dishIds").getChildren()){
                        dishIdLocalArray.add(snapshot2.child("value").getValue(String.class));
                    }
                    arrayOfDishTypes.add(new Dishtype(snapshot1.child("title").getValue(String.class)
                    ,snapshot1.child(("imageurl")).getValue(String.class)
                    ,dishIdLocalArray
                    ,snapshot1.getKey()));
                }
                Toast.makeText(DishtypeExploreActivity.this, "Data fetched", Toast.LENGTH_SHORT).show();
                DishtypeAdapter adapter = new DishtypeAdapter(DishtypeExploreActivity.this, arrayOfDishTypes);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.dishtype_foodItemListView_res);
                listView.setAdapter(adapter);
                progressBarForDishTypes.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent mIntent = new Intent(DishtypeExploreActivity.this, DishesExploreActivity.class);
//                        mIntent.putExtra("action", "edit");
                        mIntent.putExtra("dishTypeObj",(Dishtype) adapterView.getItemAtPosition(i));
//                        Toast.makeText(DishtypeExploreActivity.this,String.valueOf( ((Dishtype) adapterView.getItemAtPosition(i)).getDishIds().size()) , Toast.LENGTH_SHORT).show();
                        startActivity(mIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}