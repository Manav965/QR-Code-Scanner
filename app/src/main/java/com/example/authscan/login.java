package com.example.authscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
     Button createAccountBtn,loginBtn,forget_password_btn;
     EditText username,password;
     FirebaseAuth firebaseAuth;
     AlertDialog.Builder reset_alert;
     LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        reset_alert=new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();

        createAccountBtn=findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        username=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        loginBtn=findViewById(R.id.loginb);
        forget_password_btn=findViewById(R.id.forget_password_btn);

        forget_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start alert Dialog
                View view=inflater.inflate(R.layout.reset_pop,null);
                reset_alert.setTitle("Reset Forgot Password ?")
                           .setMessage("Enter Your Email to get Password Rest Link.")
                           .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   //validate the email id first and then we will send the email link
                                   EditText email=view.findViewById(R.id.reset_email_pop);
                                   if(email.getText().toString().isEmpty()){
                                       email.setError("Required field");
                                       return;
                                   }
                                   //send the rest Link
                                   firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Toast.makeText(login.this, "Reset email is sent", Toast.LENGTH_SHORT).show();
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   });


                               }
                           }).setNegativeButton("Cancel",null)
                             .setView(view)
                             .create().show();

            }
        });




        //whenever the user clicks on the login button we need to evaluate ths data and also get that data first
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract// validate
                if(username.getText().toString().isEmpty()){
                    username.setError("Email is missing");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Password is Missing");
                    return;
                }
                //data is validate
                //login the user
                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        }
}