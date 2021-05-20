package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    String occupation;

    String name, email, password, confirmPassword, registrationNumber;
    Button signUpButton;
    TextInputEditText emailEt, nameEt, passwordEt, confirmPasswordEt, registrationNumberEt;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent i = getIntent();
        occupation = i.getStringExtra("occupation");
        Log.d(TAG, "onCreate: "+occupation);

        mAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email);
        nameEt = findViewById(R.id.name);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        registrationNumberEt = findViewById(R.id.registrationNumber);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference();


        signUpButton = findViewById(R.id.signUpBtn);
        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpBtn:
                register();
            break;
        }

    }

    private void register() {

        name = nameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        registrationNumber = registrationNumberEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();
        if(name.isEmpty()){
            nameEt.setError("Name is empty");
            return;
        }else if(registrationNumber.isEmpty()){
            registrationNumberEt.setError("Enter a registration number");
            return;
        }else if(email.isEmpty()){
            emailEt.setError("Enter a valid email");
            return;

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Enter a valid email");
            return;
        }else if(password.isEmpty()){
            passwordEt.setError("Enter a password");
            passwordEt.requestFocus();
            return;
        }else if(password.length()<6){
            passwordEt.setError("It should be more than 6 characters");
            return;
        }if(confirmPassword.isEmpty() | !password.equals(confirmPassword)){
            confirmPasswordEt.setError("Password does not match");
            return;
        }else{
            LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);
            loadingDialog.loadingAlertDialog();
            Log.d(TAG, "register: Registration process started");
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        User user = new User(name,registrationNumber,email,occupation);
                        reference.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    loadingDialog.dismissAlertDialog();
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Log.d(TAG, "onComplete: failed due to"+task.getException());
                                    Toast.makeText(getApplicationContext(), "Network failed",Toast.LENGTH_SHORT);
                                    loadingDialog.dismissAlertDialog();
                                }
                            }
                        });



                    }else {
                        Log.d(TAG, "onComplete: failed due to"+task.getException());
                        Toast.makeText(getApplicationContext(), "Registration failed"+ task.getException(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissAlertDialog();
                    }

                }
            });




        }
    }

    public void backbtn(View view) {
        super.onBackPressed();
    }
}