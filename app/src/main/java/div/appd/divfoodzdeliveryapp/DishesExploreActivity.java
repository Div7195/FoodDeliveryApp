package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import div.appd.divfoodzdeliveryapp.models.Dish;
import div.appd.divfoodzdeliveryapp.models.Dishtype;

public class DishesExploreActivity extends AppCompatActivity {
    ProgressBar progressBarForDish;
    Dishtype dishtypeObj;
    LinearLayout ll, llcartView;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    ArrayList<Dish> arrayOfDishes = new ArrayList<Dish>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_explore);
        progressBarForDish = findViewById(R.id.progressBarExploreDish);
        ll = findViewById(R.id.ll_parent_1);
        llcartView = findViewById(R.id.cartfooterview);
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
                listView.setTag(ll);
                getListViewSize(listView);
//                Toast.makeText(DishesExploreActivity.this, "dishesexploreactivity and adapter is dishadapteruser", Toast.LENGTH_SHORT).show();
                progressBarForDish.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));

    }
}