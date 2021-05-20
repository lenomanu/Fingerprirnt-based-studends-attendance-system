package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView registerTextView;
    String occupation;
    String email, password;
    private FirebaseAuth mAuth;
    TextInputEditText emailEt, passwordEt;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    private static final String TAG = "AttendanceApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference();

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this);
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);


        Intent i = getIntent();
        occupation = i.getStringExtra("occupation");
        Log.d(TAG, "onCreate:"+ occupation);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                login();
                break;
            case R.id.registerTextView:
                openRegister(occupation);
                break;
        }
    }

    private void openRegister(String occupation ) {
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        i.putExtra("occupation",occupation);
        startActivity(i);
    }

    private void login() {
        Log.d(TAG, "login:Login process started");
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        if(email.isEmpty()){
            emailEt.setError("enter your email");
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("enter valid email address");
            return;
        }else if(password.isEmpty()){
            passwordEt.setError("enter a password");
            return;
        }else{
            LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
            loadingDialog.loadingAlertDialog();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                        finish();

                    }else{
                        loadingDialog.dismissAlertDialog();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public void backSuper(View view) {
        super.onBackPressed();
    }
}