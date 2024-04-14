package div.appd.divfoodzdeliveryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    TextView spinnerDishType, headingAddOrEditView;
    EditText menuCategoryView, dishTitleView, priceView, dishTypeView;
    Button chooseButton, uploadButton, addItemButton;
    RadioGroup vegNonvegGroup;
    ProgressBar progressBarAdd;
    RadioButton vegRadioButton, nonvegRadioButton, vegnonvegButton,outOfStockButton;
    Spinner menuCategorySpinner, quantityClassesSpinner;
    ArrayList<String> dishArrayList;
    String restaurentIdForUse, dishIdForUse, restaurentNameForUse, downloadableImageUrl, categoryForUse, quantityForUse, dishTypeInSpinner, categoryInSpinner, actionAddOrEdit;
    Boolean dishtypeItemClicked = false;
    Boolean categoryItemClicked = false;
    Integer updateRequestCount = 0;
    Integer updateCompleteCount = 0;
    Dialog dialog;
    Dish dishObj;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-app-91b3a-default-rtdb.firebaseio.com/");
    private final static int REQUEST_CODE = 100;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        spinnerDishType=findViewById(R.id.spinnerDishType);
        headingAddOrEditView = findViewById(R.id.headingAddOrEdit);
        menuCategoryView = findViewById(R.id.categoryField);
        dishTitleView = findViewById(R.id.dishTitleField);
        priceView = findViewById(R.id.priceField);
        dishTypeView = findViewById(R.id.dishTypeField);
        chooseButton = findViewById(R.id.chooseButton);
        addItemButton = findViewById(R.id.addItemButton);
        uploadButton = findViewById(R.id.uploadImgButton);
        vegNonvegGroup = findViewById(R.id.radioGroupVedNonveg);
        vegRadioButton = findViewById(R.id.radioVeg);
        nonvegRadioButton = findViewById(R.id.radioNonveg);
        menuCategorySpinner = findViewById(R.id.spinnerCategory);
        progressBarAdd = findViewById(R.id.progressBarAddingItem);
        quantityClassesSpinner = findViewById(R.id.spinnerQuantity);
        outOfStockButton = findViewById(R.id.radioStock);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://food-delivery-app-91b3a.appspot.com");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        restaurentIdForUse = pref.getString("restaurentId", "");
        restaurentNameForUse = pref.getString("restaurentName", "");
        actionAddOrEdit = getIntent().getStringExtra("action");

        if(actionAddOrEdit.equals("edit")){
            dishObj = (Dish) getIntent().getSerializableExtra("dishObj");
            menuCategoryView.setText(dishObj.getCategory());
            dishTypeView.setText(dishObj.getDishTag());
            dishTitleView.setText(dishObj.getTitle());
            headingAddOrEditView.setText("Edit item");
            if(dishObj.getVegOrNonveg().equals("VEG")){
                vegRadioButton.setChecked(true);
            }else{
                nonvegRadioButton.setChecked(true);
            }
            priceView.setText(dishObj.getPrice().toString());
//            ArrayAdapter myAdapterQuantity = (ArrayAdapter) quantityClassesSpinner.getAdapter();
//
//            quantityClassesSpinner.setSelection(myAdapterQuantity.getPosition(dishObj.getPerQuantity()));

//            outOfStockButton.setVisibility(View.VISIBLE);
            addItemButton.setText("Save Changes");
            spinnerDishType.setText(dishObj.getDishTag());
            if(dishObj.getImageUrl()!=null){
                downloadableImageUrl = dishObj.getImageUrl();
            }
            quantityForUse = dishObj.getPerQuantity();
            //default value of category spinner is being set at the main code of itself
        }
//        ***adding the onChange listeners here, so if verified, the add/save button will be enabled******
        menuCategoryView.addTextChangedListener(new TextChangedListener<EditText>(menuCategoryView) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                enableButton();
            }
        });
        dishTypeView.addTextChangedListener(new TextChangedListener<EditText>(dishTypeView) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                enableButton();
            }
        });
        priceView.addTextChangedListener(new TextChangedListener<EditText>(priceView) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                enableButton();
            }
        });
        dishTitleView.addTextChangedListener(new TextChangedListener<EditText>(dishTitleView) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                enableButton();
            }
        });
        vegNonvegGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                enableButton();
            }
        });






        // initialize array list
        //***********1.START access db to get all dishtags and setting up the spinner for it*********************
        dishArrayList=new ArrayList<>();
        databaseReference.child("dishtags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dishtagsSnapshot : snapshot.getChildren()) {
                        String dishTag = dishtagsSnapshot.child("title").getValue(String.class);

                        if (dishTag != null) {
                            dishArrayList.add(dishTag);
                        }
                }
                spinnerDishType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Initialize dialog
                        dialog=new Dialog(AddItemActivity.this);

                        // set custom dialog
                        dialog.setContentView(R.layout.dialog_searchable_spinner);

                        // set custom height and width
                        dialog.getWindow().setLayout(650,800);

                        // set transparent background
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        // show dialog
                        dialog.show();

                        // Initialize and assign variable
                        EditText editText=dialog.findViewById(R.id.edit_text);
                        ListView listView=dialog.findViewById(R.id.list_view);
                        editText.setTextColor(ContextCompat.getColor(AddItemActivity.this, R.color.black));
                        // Initialize array adapter
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_list_item_1,dishArrayList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text = (TextView) view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                return view;
                            }
                        };
                        // set adapter
                        listView.setAdapter(adapter);

                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adapter.getFilter().filter(s);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        if(actionAddOrEdit.equals("edit")) {
                            Integer position = adapter.getPosition(dishObj.getDishTag());
//                            editText.setText(adapter.getItem(position));
                            spinnerDishType.setText(adapter.getItem(position));
                            listView.setSelection(position);
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // when item selected from list
                                // set selected item on textView
                                spinnerDishType.setText(adapter.getItem(position));
                                dishTypeView.setText(adapter.getItem(position));
                                dishtypeItemClicked = true;
                                dishTypeInSpinner = adapter.getItem(position);
                                // Dismiss dialog
                                dialog.dismiss();
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        ***********1.END*************
//        ***********2.START access db to get the menu categories******************
        List<String> menuCategoryList = new ArrayList<>();
        menuCategoryList.add(0, "Select category");
        databaseReference.child("restaurents").child(restaurentIdForUse).child("menucategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    String catg = restaurantSnapshot.child("categoryName").getValue(String.class);

                    if (catg != null) {
                        menuCategoryList.add(catg);
                    }
                }
                ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter(AddItemActivity.this, R.layout.spinner_list_item, menuCategoryList);
                menuCategorySpinner.setAdapter(categoryArrayAdapter);
                menuCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getItemAtPosition(position).equals("Choose category from list")){
                        }else {
                            String item = parent.getItemAtPosition(position).toString();
                            if(!item.equals("Select category")) {
                                menuCategoryView.setText(item);
                                categoryItemClicked = true;
                                categoryInSpinner = item;
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if(actionAddOrEdit.equals("edit")){
                    ArrayAdapter myAdapterCategory = (ArrayAdapter) menuCategorySpinner.getAdapter();
                    menuCategorySpinner.setSelection(myAdapterCategory.getPosition(dishObj.getCategory()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //        ***********2.END*************
//        *****************3.START setting up spinner for quantities***********
        List<String> quantityClass = new ArrayList<>();
        quantityClass.add(0, "Select quantity");
        quantityClass.add("pc");
        quantityClass.add("plate");
        quantityClass.add("1 kg");
        quantityClass.add("250 gm");
        quantityClass.add("125 gm");
        quantityClass.add("1 litre");
        quantityClass.add("250 ml");
        quantityClass.add("125 ml");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list_item, quantityClass);
        quantityClassesSpinner.setAdapter(arrayAdapter);
        quantityClassesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose quanitity from list")){
                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    quantityForUse = item;
                    enableButton();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(actionAddOrEdit.equals("edit")){
            quantityClassesSpinner.setSelection(arrayAdapter.getPosition(dishObj.getPerQuantity()));
        }
//**************3.END***********************
//        **************4.START adding on click listener methods for  choose and uplaod buttons*********
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(filePath);
            }
        });
//        ***********4.END************************
//***************5.START add item button on click listener,(most important code)***********

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionAddOrEdit.equals("add")) {
                    addNewItem();
                } else {
                    updateMenuItem();
                }
            }
        });



    }
//    *****END of oncreate() method
    private void addNewItem(){
        progressBarAdd.setVisibility(View.VISIBLE);
//                    if (filePath == null || downloadableImageUrl == null) {
//                        Uri defaultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                                + "://" + AddItemActivity.this.getResources().getResourcePackageName(R.drawable.default_food)
//                                + '/' + AddItemActivity.this.getResources().getResourceTypeName(R.drawable.default_food)
//                                + '/' + AddItemActivity.this.getResources().getResourceEntryName(R.drawable.default_food));
//                        uploadImage(defaultUri);
//                    }
//                String generatedDishId = RandomStringGenerator.generateRandomString(13);
//                ************START updating dishes node in database************
        databaseReference.child("dishes").push().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("title").getRef().setValue(dishTitleView.getText().toString());
                snapshot.child("dishTag").getRef().setValue(dishTypeView.getText().toString());
                snapshot.child("price").getRef().setValue(priceView.getText().toString());
                snapshot.child("perquantity").getRef().setValue(quantityForUse);
                snapshot.child("imageurl").getRef().setValue(downloadableImageUrl);
                snapshot.child("rating").getRef().setValue(0.0);
                snapshot.child("timesOrdered").getRef().setValue(0);
                snapshot.child("restaurentId").getRef().setValue(restaurentIdForUse);
                snapshot.child("category").getRef().setValue(menuCategoryView.getText().toString());
                snapshot.child("instock").getRef().setValue(true);
                snapshot.child("restaurentName").getRef().setValue(restaurentNameForUse);
//                        snapshot.child("menu").child("category").getRef().setValue(menuCategoryView.getText().toString());
                dishIdForUse = snapshot.getRef().getKey();

                int selectedId = vegNonvegGroup.getCheckedRadioButtonId();
                vegnonvegButton = (RadioButton) findViewById(selectedId);
                if (vegnonvegButton.getText().toString().equals("VEG")) {
                    snapshot.child("vegOrNonveg").getRef().setValue("VEG");
                } else {
                    snapshot.child("vegOrNonveg").getRef().setValue("NONVEG");
                }
                progressBarAdd.setVisibility(View.INVISIBLE);
                Toast.makeText(AddItemActivity.this, "Added New Dish", Toast.LENGTH_SHORT).show();

                //                 *********START updating dishtags nodes in database********
                if (!dishTypeView.getText().toString().equals(dishTypeInSpinner) || !dishtypeItemClicked) {
                    databaseReference.child("dishtags").push().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child("title").getRef().setValue(dishTypeView.getText().toString());
                            snapshot.child("imageurl").getRef().setValue(downloadableImageUrl);
                            DatabaseReference dishReference = databaseReference.child("dishtags").child(snapshot.getKey()).child("dishIds").push();
                            dishReference.child("value").setValue(dishIdForUse);
//                                        Toast.makeText(AddItemActivity.this, "Updated dishtags", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    databaseReference.child("dishtags").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(dishTypeView.getText().toString().equals(dataSnapshot.child("title").getValue(String.class))){
                                    DatabaseReference ds = dataSnapshot.child("dishIds").getRef().push();
                                    ds.child("value").setValue(dishIdForUse);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//              **********END****************
                //               ********START updating restaurents nodes in database********
                if (!menuCategoryView.getText().toString().equals(categoryInSpinner) || !categoryItemClicked) {
                    databaseReference.child("restaurents").child(restaurentIdForUse).child("menucategories").push().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child("categoryName").getRef().setValue(menuCategoryView.getText().toString());
//                                        Toast.makeText(AddItemActivity.this, "Updated menu categories", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                databaseReference.child("restaurents").child(restaurentIdForUse).child("fooditems").push().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.child("dishId").getRef().setValue(dishIdForUse);
//                                    Toast.makeText(AddItemActivity.this, "Updated dishes in your menu", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                *********END******************




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateMenuItem(){
        progressBarAdd.setVisibility(View.VISIBLE);
        if (!menuCategoryView.getText().toString().equals(dishObj.getCategory())) {
            if (!menuCategoryView.getText().toString().equals(categoryInSpinner)) {
                updateRequestCount = updateRequestCount + 2;
            } else {
                updateRequestCount = updateRequestCount + 1;
            }
        }
        if (!dishTypeView.getText().toString().equals(dishObj.getDishTag())) {
            if (!dishTypeView.getText().toString().equals(dishTypeInSpinner)) {
                updateRequestCount = updateRequestCount + 4;
            } else {
                updateRequestCount = updateRequestCount + 3;
            }
        }
        if (!dishTitleView.getText().toString().equals(dishObj.getTitle())) {
            updateRequestCount = updateRequestCount + 1;
        }
        if (vegRadioButton.isChecked() && dishObj.getVegOrNonveg().equals("NONVEG") || nonvegRadioButton.isChecked() && dishObj.getVegOrNonveg().equals("VEG")) {
            updateRequestCount = updateRequestCount + 1;
        }
        if (!priceView.getText().toString().equals(dishObj.getPrice())) {
            updateRequestCount = updateRequestCount + 1;
        }
        if (!quantityForUse.equals(dishObj.getPerQuantity())) {
            updateRequestCount = updateRequestCount + 1;
        }

        if (downloadableImageUrl != null){
            if (!downloadableImageUrl.equals(dishObj.getImageUrl())) {
                updateRequestCount = updateRequestCount + 1;
            }
        }


        if(outOfStockButton.isChecked()){
            updateRequestCount = updateRequestCount + 1;
        }




        if (!menuCategoryView.getText().toString().equals(dishObj.getCategory())) {
            if (!menuCategoryView.getText().toString().equals(categoryInSpinner)) {
                DatabaseReference nodeRef1 = databaseReference.child("restaurents").child(dishObj.getRestaurentId()).child("menucategories").push();
                nodeRef1.child("categoryName").setValue(menuCategoryView.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                DatabaseReference nodeRef2 = databaseReference.child("dishes").child(dishObj.getDishId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("category",menuCategoryView.getText().toString());
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                DatabaseReference nodeRef2 = databaseReference.child("dishes").child(dishObj.getDishId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("category",menuCategoryView.getText().toString());
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        if (!dishTypeView.getText().toString().equals(dishObj.getDishTag())) {
            if (!dishTypeView.getText().toString().equals(dishTypeInSpinner)) {
                DatabaseReference nodeRef1 = databaseReference.child("dishtags").push();
                Map<String, Object> updates1 = new HashMap<>();
                updates1.put("title",dishTypeView.getText().toString());
                updates1.put("imageurl",downloadableImageUrl);
                nodeRef1.updateChildren(updates1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference nodeRef4 = nodeRef1.child("dishIds").push();
                        Map<String, Object> updates1 = new HashMap<>();
                        updates1.put("value",dishObj.getDishId());
                        nodeRef4.updateChildren(updates1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                updateCompleteCount = updateCompleteCount + 2;
                                if(updateCompleteCount == updateRequestCount){
                                    progressBarAdd.setVisibility(View.INVISIBLE);
                                    Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                databaseReference.child("dishtags").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            for(DataSnapshot snapshot2 : snapshot1.child("dishIds").getChildren()){
                                if(snapshot2.child("value").getValue(String.class).equals(dishObj.getDishId())){
                                    snapshot2.getRef().removeValue();
                                }
                            }
                        }
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                DatabaseReference nodeRef2 = databaseReference.child("dishes").child(dishObj.getDishId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("dishTag",dishTypeView.getText().toString());
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });;

            }else{
                DatabaseReference nodeRef2 = databaseReference.child("dishes").child(dishObj.getDishId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("dishTag",dishTypeView.getText().toString());
                nodeRef2.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });;

                databaseReference.child("dishtags").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            for(DataSnapshot snapshot2 : snapshot1.child("dishIds").getChildren()){
                                if(snapshot2.child("value").getValue(String.class).equals(dishObj.getDishId())){
//                                                Toast.makeText(AddItemActivity.this, "wowwow", Toast.LENGTH_SHORT).show();
                                    snapshot2.getRef().removeValue();
                                }
                            }
                        }
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            if(snapshot1.child("title").getValue(String.class).equals(dishTypeView.getText().toString())) {
                                DatabaseReference dr = snapshot1.child("dishIds").getRef().push();
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("value",dishObj.getDishId());
                                dr.updateChildren(updates);
                                break;
                            }
                        }

                        updateCompleteCount = updateCompleteCount + 2;
                        if(updateCompleteCount == updateRequestCount){
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        if(!dishTitleView.getText().toString().equals(dishObj.getTitle())){
            DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
            Map<String, Object> updates = new HashMap<>();
            updates.put("title",dishTitleView.getText().toString());
            nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCompleteCount = updateCompleteCount + 1;
                    if(updateCompleteCount == updateRequestCount){
                        progressBarAdd.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });;
        }

        if(vegRadioButton.isChecked() && dishObj.getVegOrNonveg().equals("NONVEG") || nonvegRadioButton.isChecked() && dishObj.getVegOrNonveg().equals("VEG")){
            DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
            Map<String, Object> updates = new HashMap<>();
            String s;
            if(vegRadioButton.isChecked() && dishObj.getVegOrNonveg().equals("NONVEG")){
                s = "VEG";
            }else{
                s = "NONVEG";
            }
            updates.put("vegOrNonveg",s);
            nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCompleteCount = updateCompleteCount + 1;
                    if(updateCompleteCount == updateRequestCount){
                        progressBarAdd.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });;
        }

        if(!priceView.getText().toString().equals(dishObj.getPrice())){
            DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
            Map<String, Object> updates = new HashMap<>();
            updates.put("price",priceView.getText().toString());
            nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCompleteCount = updateCompleteCount + 1;
                    if(updateCompleteCount == updateRequestCount){
                        progressBarAdd.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });;
        }

        if(!quantityForUse.equals(dishObj.getPerQuantity())){
            DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
            Map<String, Object> updates = new HashMap<>();
            updates.put("perquantity",quantityForUse);
            nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCompleteCount = updateCompleteCount + 1;
                    if(updateCompleteCount == updateRequestCount){
                        progressBarAdd.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(downloadableImageUrl!=null) {
            if (!downloadableImageUrl.equals(dishObj.getImageUrl())) {
                DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("imageurl", downloadableImageUrl);
                nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCompleteCount = updateCompleteCount + 1;
                        if (updateCompleteCount == updateRequestCount) {
                            progressBarAdd.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        if(outOfStockButton.isChecked()){
            DatabaseReference nodeRef1 = databaseReference.child("dishes").child(dishObj.getDishId());
            Map<String, Object> updates = new HashMap<>();
            updates.put("instock",false);
            nodeRef1.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCompleteCount = updateCompleteCount + 1;
                    if(updateCompleteCount == updateRequestCount){
                        progressBarAdd.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddItemActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//   definitions of selectImage() and uploadImage()
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLaunch.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            filePath = data.getData();
                            Toast.makeText(AddItemActivity.this, "Image has been chosen", Toast.LENGTH_SHORT).show();
                            uploadButton.setEnabled(true);
                        }
                    }
                }
            });

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {// Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast.makeText(AddItemActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    String imageUrl = downloadUrl.toString();
                                    downloadableImageUrl = imageUrl;
                                    // Use the imageUrl as needed
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors while retrieving the download URL
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(AddItemActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private Boolean enableButton(){
        if(menuCategoryView.getText().toString().length() == 0){
            addItemButton.setEnabled(false);
            return false;
        }
        if(dishTypeView.getText().toString().length() == 0){
            addItemButton.setEnabled(false);
            return false;
        }
        if(dishTitleView.getText().toString().length() == 0){
            addItemButton.setEnabled(false);
            return false;
        }
        if(vegNonvegGroup.getCheckedRadioButtonId() == -1){
            addItemButton.setEnabled(false);
            return false;
        }
        if(priceView.getText().toString().length() == 0){
            addItemButton.setEnabled(false);
            return false;
        }
        if (!quantityForUse.equals("Select quantity")) {
            addItemButton.setEnabled(true);
            return true;
        }
        addItemButton.setEnabled(false);
        return false;


    }

}