package com.example.authscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText registerFullName,registerEmail,registerPassword,registerConfPass;
    Button registerUserBtn,gotoLogin;
    FirebaseAuth fAuth;//this is to create a new user.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerFullName=findViewById(R.id.registerFullName);
        registerEmail=findViewById(R.id.registerEmail);
        registerPassword=findViewById(R.id.registerPassword);
        registerConfPass=findViewById(R.id.confPassword);
        registerUserBtn=findViewById(R.id.registerBtn);
        gotoLogin=findViewById(R.id.gotoLogin);

        fAuth=FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });


        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //since once the user has filled the form we will extract the data
                String fullName= registerFullName.getText().toString();
                String email=registerEmail.getText().toString();
                String password=registerPassword.getText().toString();
                String confPass=registerConfPass.getText().toString();

                if(fullName.isEmpty())
                {
                    registerFullName.setError("Full Name is required");
                    return;
                }
                if(email.isEmpty())
                {
                    registerEmail.setError("Enter correct Email");
                    return;
                }
                if(password.isEmpty())
                {
                    registerPassword.setError("Password is required");
                    return;
                }
                if(confPass.isEmpty())
                {
                    registerConfPass.setError("Re-enter the password");
                    return;
                }
              if(!password.equals(confPass))
              {
                  registerConfPass.setError("Passwords do not Match");
                  return;
              }
             //data is validated
             //register the user using firebase
                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();

              fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {
                      //if auth is successfull send the user to the dashboard
                      startActivity(new Intent(getApplicationContext(),MainActivity.class));
                      finish();//this means that once the user has successfully logged in
                      //remove all the preivous activites from the back stack and since we do not
                      //also the user will not be able to come back using the back button.
                      //wan' the user to come back to the register activity therfore we do this
                  }

              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      //if somewhow the email and password wala thing fails this will generate an exception
                      Toast.makeText(Register.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              });
     }
        });
}
}