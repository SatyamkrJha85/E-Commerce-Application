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

public class Register extends AppCompatActivity {



    EditText name_user_signup,gmail_user_signup,password_user_signup;
    Button signbtn;
    TextView login_intent_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login_intent_text=findViewById(R.id.login_intent_text);
        signbtn=findViewById(R.id.signbtn);
        name_user_signup=findViewById(R.id.name_user_signup);
        gmail_user_signup=findViewById(R.id.gmail_user_signup);
        password_user_signup=findViewById(R.id.password_user_signup);

        signbtn.setOnClickListener(v -> createAccount());


        login_intent_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

    }
    void createAccount(){
        String email  = gmail_user_signup.getText().toString();
        String mainpassword  = password_user_signup.getText().toString();
        String username  = name_user_signup.getText().toString();

        boolean isValidated = validateData(username,email,mainpassword);
        if(!isValidated){
            return;
        }
        createAccountInFirebase(email,mainpassword);
    }
    void createAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Done
                    Toast.makeText(Register.this, "Signup successfull Go to Login Activity", Toast.LENGTH_SHORT).show();
                    name_user_signup.setText("");
                    gmail_user_signup.setText("");
                    password_user_signup.setText("");
                    firebaseAuth.signOut();
                }
                else{
                    // Failed
                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




    boolean validateData(String user,String email,String mainpassword){
        //validate the data that are input by user.
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            gmail_user_signup.setError("Email is invalid");
            return false;
        }
        if(mainpassword.length()<6){
            password_user_signup.setError("Password length is invalid");
            return false;
        }
        if(user.length()<=4){
            name_user_signup.setError("Invalid Username");
            return false;
        }
        return true;


    }
}