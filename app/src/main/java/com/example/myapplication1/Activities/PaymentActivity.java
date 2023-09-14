package com.example.myapplication1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication1.R;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;

    Button pay_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        double amount=0.0;
        amount=getIntent().getDoubleExtra("amount",0.0);

        subTotal=findViewById(R.id.sub_total);
        discount=findViewById(R.id.textView17);
        shipping=findViewById(R.id.textView18);
        total=findViewById(R.id.total_amt);
        pay_btn=findViewById(R.id.pay_btn);

        subTotal.setText(amount+"$");
        total.setText(amount+"/-");

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.phonepe.com/"));

              //  startActivity(intent);

                Toast.makeText(PaymentActivity.this, "Dummy Payment SucessFull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            }
        });


    }
}