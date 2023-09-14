package com.example.myapplication1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    EditText gmail_user_login,password_user_login;
    Button loginbtn;
    TextView signup_intent_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        signup_intent_text=findViewById(R.id.signup_intent_text);
        loginbtn=findViewById(R.id.loginbtn);
        gmail_user_login=findViewById(R.id.gmail_user_login);
        password_user_login=findViewById(R.id.password_user_login);

        loginbtn.setOnClickListener((v) -> loginUser());

        signup_intent_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });


    }
    void loginUser(){
        String email  = gmail_user_login.getText().toString();
        String mainpassword  = password_user_login.getText().toString();

        boolean isValidated = validateData(email,mainpassword);
        if(!isValidated){
            return;
        }
        // database

        loginAccountInFirebase(email,mainpassword);
    }

    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "Something Wrong"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    boolean validateData(String email,String mainpassword){

        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            gmail_user_login.setError("Email is invalid");
            return false;
        }
        if(mainpassword.length()<6){
            password_user_login.setError("Password length is invalid");
            return false;
        }
        return true;
    }

}
