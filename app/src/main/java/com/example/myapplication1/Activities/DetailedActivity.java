package com.example.myapplication1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication1.Models.NewProductsModel;
import com.example.myapplication1.Models.PopularProductModel;
import com.example.myapplication1.Models.ShowAllModel;
import com.example.myapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button addTocart,buyNow;
    ImageView addItems,removeItem;
    Toolbar toolbar;


    int totalQuality=1;
    int totalPrice=0;

    private FirebaseFirestore  firestore;

    // New Products
    NewProductsModel newProductsModel=null;

    // Popular product
    PopularProductModel popularProductModel=null;

    // Show all Product
    ShowAllModel showAllModel =null;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.detailed_toolbar);


        setSupportActionBar(toolbar);

        //  getSupportActionBar(home_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Object obj=getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewProductsModel){
            newProductsModel=(NewProductsModel) obj;
        }else if (obj instanceof PopularProductModel){
            popularProductModel=(PopularProductModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel=(ShowAllModel) obj;
        }

        detailedImg=findViewById(R.id.detailed_img);
        name=findViewById(R.id.detailed_name);
        rating=findViewById(R.id.rating);
        description=findViewById(R.id.detailed_desc);
        price=findViewById(R.id.detailed_price);
        addTocart=findViewById(R.id.add_to_cart);
        buyNow=findViewById(R.id.buy_now);
        addItems=findViewById(R.id.add_item);
        removeItem=findViewById(R.id.remove_item);
        quantity=findViewById(R.id.quantity);

        // New products

        if(newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice=newProductsModel.getPrice() * totalQuality;
        }

        // Popular products

        if(popularProductModel != null){
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            rating.setText(popularProductModel.getRating());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice=popularProductModel.getPrice() * totalQuality;

        }

        // Show All products

        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice=showAllModel.getPrice() * totalQuality;

        }


        // Buy Now

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailedActivity.this,AddressActivity.class);

                if(newProductsModel != null){
                    intent.putExtra("item",newProductsModel);
                }
                if(popularProductModel !=null){
                    intent.putExtra("item",popularProductModel);
                }
                if(showAllModel !=null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });

        // Add to cart

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuality<10){
                    totalQuality++;
                    quantity.setText(String.valueOf(totalQuality));

                    if(newProductsModel!= null){
                        totalPrice=newProductsModel.getPrice()*totalQuality;
                    }
                    if(popularProductModel!= null){
                        totalPrice=popularProductModel.getPrice()*totalQuality;
                    }
                    if(showAllModel!= null){
                        totalPrice=showAllModel.getPrice()*totalQuality;
                    }
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuality>1){
                    totalQuality--;
                    quantity.setText(String.valueOf(totalQuality));
                }
            }
        });


    }

    private void addtoCart() {

        String saveCurrentTime,saveCurrentDate;
        Calendar calFordate=Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calFordate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calFordate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);

        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);


        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Add To a Cart! ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }
}