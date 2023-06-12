package div.appd.divfoodzdeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserRestoActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    ProgressBar progressBarForDish;
    String clickedRestaurentId;
    TextView restaurentNameView, cuisinesView, addressView, contactView;
    Restaurent restaurentObj;
    LinearLayout lll;
    ArrayList <Dish> arrayOfDishes = new ArrayList<Dish>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_resto);
        restaurentNameView = findViewById(R.id.user_resto_name);
        cuisinesView = findViewById(R.id.user_resto_cuisines);
        addressView = findViewById(R.id.user_resto_address);
        contactView = findViewById(R.id.user_resto_contact);
        progressBarForDish = findViewById(R.id.progressBarinUserResto);
        restaurentObj = (Restaurent) getIntent().getSerializableExtra("restaurentObj");
        clickedRestaurentId = getIntent().getStringExtra("restaurentId");
        lll = findViewById(R.id.ll_parent_2);
        if (clickedRestaurentId != null) {
            ArrayList<String> arrayOfDishIdsRestaurent = new ArrayList<String>();
            databaseReference.child("restaurents").child(clickedRestaurentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name, locality, contact, cuisines;
                    name = snapshot.child("name").getValue(String.class);
                    locality = snapshot.child("locality").getValue(String.class);
                    contact = snapshot.child("contact").getValue(String.class);
                    cuisines = snapshot.child("cuisines").getValue(String.class);
                    if (name != null) {
                        restaurentNameView.setText(name);
                    }
                    if (locality != null) {
                        addressView.setText(locality);
                    }
                    if (contact != null) {
                        contactView.setText(contact);
                    }
                    if (cuisines != null) {
                        cuisinesView.setText(cuisines);
                    }
                    for (DataSnapshot snapshot1 : snapshot.child("fooditems").getChildren()) {
                        arrayOfDishIdsRestaurent.add(snapshot1.child("dishId").getValue(String.class));
                    }
                    databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (String s : arrayOfDishIdsRestaurent) {
                                arrayOfDishes.add(new Dish(snapshot.child(s).child("title").getValue(String.class)
                                        , snapshot.child(s).child("dishTag").getValue(String.class)
                                        , snapshot.child(s).child("price").getValue(String.class)
                                        , snapshot.child(s).child("perquantity").getValue(String.class)
                                        , snapshot.child(s).child("imageurl").getValue(String.class)
                                        , snapshot.child(s).child("rating").getValue(Double.class)
                                        , snapshot.child(s).child("timesOrdered").getValue(Integer.class)
                                        , snapshot.child(s).child("restaurentId").getValue(String.class)
                                        , snapshot.child(s).child("category").getValue(String.class)
                                        , snapshot.child(s).child("instock").getValue(Boolean.class)
                                        , snapshot.child(s).child("restaurentName").getValue(String.class)
                                        , snapshot.child(s).child("vegOrNonveg").getValue(String.class)
                                        , s));
                            }
                            RestoViewOfUserAdapter adapter = new RestoViewOfUserAdapter(UserRestoActivity.this, arrayOfDishes);
                            // Attach the adapter to a ListView
                            ListView listView = (ListView) findViewById(R.id.dishListRestoUser);
                            listView.setAdapter(adapter);
                            Toast.makeText(UserRestoActivity.this, "userrestoactvity and adapter is restoviewofadapter", Toast.LENGTH_SHORT).show();
                            listView.setTag(lll);
                            getListViewSize(listView);

                            progressBarForDish.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                }
                            });
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
        } else {
            if (restaurentObj.getName() != null) {
                restaurentNameView.setText(restaurentObj.getName());
            }
            if (restaurentObj.getCuisines() != null) {
                cuisinesView.setText(restaurentObj.getCuisines());
            }
            if (restaurentObj.getLocality() != null) {
                addressView.setText(restaurentObj.getAddress());
            }
            if (restaurentObj.getContact() != null) {
                contactView.setText(restaurentObj.getContact());
            }

            databaseReference.child("dishes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (String s : restaurentObj.getDishIds()) {
                        arrayOfDishes.add(new Dish(snapshot.child(s).child("title").getValue(String.class)
                                , snapshot.child(s).child("dishTag").getValue(String.class)
                                , snapshot.child(s).child("price").getValue(String.class)
                                , snapshot.child(s).child("perquantity").getValue(String.class)
                                , snapshot.child(s).child("imageurl").getValue(String.class)
                                , snapshot.child(s).child("rating").getValue(Double.class)
                                , snapshot.child(s).child("timesOrdered").getValue(Integer.class)
                                , snapshot.child(s).child("restaurentId").getValue(String.class)
                                , snapshot.child(s).child("category").getValue(String.class)
                                , snapshot.child(s).child("instock").getValue(Boolean.class)
                                , snapshot.child(s).child("restaurentName").getValue(String.class)
                                , snapshot.child(s).child("vegOrNonveg").getValue(String.class)
                                , s));
                    }
                    RestoViewOfUserAdapter adapter = new RestoViewOfUserAdapter(UserRestoActivity.this, arrayOfDishes);
                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.dishListRestoUser);
                    listView.setAdapter(adapter);
                    Toast.makeText(UserRestoActivity.this, "userrestoactivity", Toast.LENGTH_SHORT).show();
                    progressBarForDish.setVisibility(View.GONE);
                    listView.setTag(lll);
                    getListViewSize(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
    private void getListViewSize(ListView myListView){
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


    }
}
