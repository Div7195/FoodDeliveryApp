package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuEditActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    String restaurentIdForUse;
    ArrayList<Dish> arrayOfDishes = new ArrayList<Dish>();
    ArrayList<String> arrayOfDishIds = new ArrayList<String>();
    ProgressBar loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        loadingView = findViewById(R.id.progressBar5);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurentIdForUse = pref.getString("restaurentId", "");
        databaseReference.child("restaurents").child(restaurentIdForUse).child("fooditems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    arrayOfDishIds.add(dataSnapshot.child("dishId").getValue(String.class));
                }
                databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(String dish : arrayOfDishIds){
                            arrayOfDishes.add(new Dish(snapshot.child(dish).child("title").getValue(String.class)
                                    ,snapshot.child(dish).child("dishTag").getValue(String.class)
                                    ,snapshot.child(dish).child("price").getValue(String.class)
                                    ,snapshot.child(dish).child("perquantity").getValue(String.class)
                                    ,snapshot.child(dish).child("imageurl").getValue(String.class)
                                    ,snapshot.child(dish).child("rating").getValue(Double.class)
                                    ,snapshot.child(dish).child("timesOrdered").getValue(Integer.class)
                                    ,snapshot.child(dish).child("restaurentId").getValue(String.class)
                                    ,snapshot.child(dish).child("category").getValue(String.class)
                                    ,snapshot.child(dish).child("instock").getValue(Boolean.class)
                                    ,snapshot.child(dish).child("restaurentName").getValue(String.class)
                                    ,snapshot.child(dish).child("vegOrNonveg").getValue(String.class)
                                    ,dish));
                        }
                        DishAdapterRestaurent adapter = new DishAdapterRestaurent(MenuEditActivity.this, arrayOfDishes);
                        // Attach the adapter to a ListView
                        ListView listView = (ListView) findViewById(R.id.foodItemListView_res);
                        listView.setAdapter(adapter);
                        loadingView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}