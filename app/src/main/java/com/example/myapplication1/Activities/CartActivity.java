package com.example.myapplication1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication1.Adapters.MyCartAdapter;
import com.example.myapplication1.Models.MyCartModel;
import com.example.myapplication1.Models.ShowAllModel;
import com.example.myapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    MyCartAdapter cartAdapter;
    TextView overallamount;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    int overAllTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.cart_rec);
        toolbar=findViewById(R.id.my_cart_toolbar);
        overallamount=findViewById(R.id.overallamount);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get data from my cart adapter

        LocalBroadcastManager.getInstance(this)
                        .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myCartModelList=new ArrayList<>();
        cartAdapter=new MyCartAdapter(this,myCartModelList);
        recyclerView.setAdapter(cartAdapter);

        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){


                            for(DocumentSnapshot doc :task.getResult().getDocuments()){
                                MyCartModel myCartModel=doc.toObject(MyCartModel.class);
                                myCartModelList.add(myCartModel);
                                cartAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
    }

    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill=intent.getIntExtra("totalAmount",0);
            overallamount.setText("Total Amount :"+totalBill+"$");
        }
    };
}