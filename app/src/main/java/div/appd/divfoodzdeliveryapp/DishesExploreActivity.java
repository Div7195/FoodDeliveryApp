package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class DishesExploreActivity extends AppCompatActivity {
    ProgressBar progressBarForDish;
    Dishtype dishtypeObj;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    ArrayList<Dish> arrayOfDishes = new ArrayList<Dish>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_explore);
        progressBarForDish = findViewById(R.id.progressBarExploreDish);
        dishtypeObj = (Dishtype) getIntent().getSerializableExtra("dishTypeObj");
        databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String s : (List<String>) dishtypeObj.getDishIds()){
//                    Toast.makeText(DishesExploreActivity.this, dishtypeObj.getDishIds().size(), Toast.LENGTH_SHORT).show();
                    Log.d("debugging", s);
                    arrayOfDishes.add(new Dish(snapshot.child(s).child("title").getValue(String.class)
                            ,snapshot.child(s).child("dishTag").getValue(String.class)
                            ,snapshot.child(s).child("price").getValue(String.class)
                            ,snapshot.child(s).child("perquantity").getValue(String.class)
                            ,snapshot.child(s).child("imageurl").getValue(String.class)
                            ,snapshot.child(s).child("rating").getValue(Double.class)
                            ,snapshot.child(s).child("timesOrdered").getValue(Integer.class)
                            ,snapshot.child(s).child("restaurentId").getValue(String.class)
                            ,snapshot.child(s).child("category").getValue(String.class)
                            ,snapshot.child(s).child("instock").getValue(Boolean.class)
                            ,snapshot.child(s).child("restaurentName").getValue(String.class)
                            ,snapshot.child(s).child("vegOrNonveg").getValue(String.class)
                            ,s));

                };
                DishAdapterUser adapter = new DishAdapterUser(DishesExploreActivity.this, arrayOfDishes);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.dish_foodItemListView_res);
                listView.setAdapter(adapter);
                Toast.makeText(DishesExploreActivity.this, "dishesexploreactivity and adapter is dishadapteruser", Toast.LENGTH_SHORT).show();
                progressBarForDish.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}