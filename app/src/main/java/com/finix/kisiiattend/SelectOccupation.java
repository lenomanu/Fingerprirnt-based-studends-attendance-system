package com.finix.kisiiattend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectOccupation extends AppCompatActivity implements View.OnClickListener {
    Button studentBtn, lecturerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_occupation);

        mAuth = FirebaseAuth.getInstance();

        studentBtn = findViewById(R.id.studentBtn);
        studentBtn.setOnClickListener(this);
        lecturerBtn = findViewById(R.id.lecturerBtn);
        lecturerBtn.setOnClickListener(this);

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.studentBtn:
                String occupation = "student";
                openLogin(occupation);
                break;
            case R.id.lecturerBtn:
                String occupation1 = "lecturer";
                openLogin("lecturer");
                break;
        }
    }

    private void openLogin(String occupation) {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        i.putExtra("occupation",occupation);
        startActivity(i);
    }
}