package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import div.appd.divfoodzdeliveryapp.models.Dish;

public class MenuEditActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    String restaurentIdForUse;
    ArrayList<Dish> arrayOfDishes = new ArrayList<Dish>();
    TextView tv;
    ArrayList<String> arrayOfDishIds = new ArrayList<String>();
    ProgressBar loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        loadingView = findViewById(R.id.progressBar5);
//        tv = findViewById(R.id.textView8);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurentIdForUse = pref.getString("restaurentId", "");
        databaseReference.child("restaurents").child(restaurentIdForUse).child("fooditems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayOfDishIds.add(dataSnapshot.child("dishId").getValue(String.class));
                }
                databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String dish : arrayOfDishIds) {
                            arrayOfDishes.add(new Dish(snapshot.child(dish).child("title").getValue(String.class)
                                    , snapshot.child(dish).child("dishTag").getValue(String.class)
                                    , snapshot.child(dish).child("price").getValue(String.class)
                                    , snapshot.child(dish).child("perquantity").getValue(String.class)
                                    , snapshot.child(dish).child("imageurl").getValue(String.class)
                                    , snapshot.child(dish).child("rating").getValue(Double.class)
                                    , snapshot.child(dish).child("timesOrdered").getValue(Integer.class)
                                    , snapshot.child(dish).child("restaurentId").getValue(String.class)
                                    , snapshot.child(dish).child("category").getValue(String.class)
                                    , snapshot.child(dish).child("instock").getValue(Boolean.class)
                                    , snapshot.child(dish).child("restaurentName").getValue(String.class)
                                    , snapshot.child(dish).child("vegOrNonveg").getValue(String.class)
                                    , dish));
                        }
                        DishAdapterRestaurent adapter = new DishAdapterRestaurent(MenuEditActivity.this, arrayOfDishes);
                        // Attach the adapter to a ListView
                        ListView listView = (ListView) findViewById(R.id.foodItemListView_res);
                        listView.setAdapter(adapter);
                        getListViewSize(listView);
                        loadingView.setVisibility(View.GONE);


//                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//                            }
//
//                            @Override
//                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                                if (i == 0) {
//                                    // check if we reached the top or bottom of the list
//                                    View v = listView.getChildAt(0);
//                                    int offset = (v == null) ? 0 : v.getTop();
//                                    if (offset == 0) {
//                                        // reached the top: visible header and footer
//                                        tv.setVisibility(View.VISIBLE);
//                                    }
//                                }else{
//                                    tv.setVisibility(View.GONE);
//
//                                }
//                            }
//                        });

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