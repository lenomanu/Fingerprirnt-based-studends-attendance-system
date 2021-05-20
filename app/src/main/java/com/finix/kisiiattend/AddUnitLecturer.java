package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddUnitLecturer extends AppCompatActivity{
    public static final String TAG = "AddUnitLecturer";
    Button addUnitBtn;
    EditText unitCodeEt, unitNameEt,unitPasswordEt,unitTimesEt;
    FirebaseDatabase rootNode;
    DatabaseReference units;
    DatabaseReference myunits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit_lecturer);
        unitCodeEt = findViewById(R.id.unitCode);
        unitNameEt = findViewById(R.id.unitName);
        unitPasswordEt = findViewById(R.id.unitPassword);
        unitTimesEt = findViewById(R.id.unitTimes);
        addUnitBtn = findViewById(R.id.addUnitBtn);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        rootNode = FirebaseDatabase.getInstance();
        units = rootNode.getReference("units");
        myunits = rootNode.getReference("users").child(userId).child("myunits");


        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitCode = unitCodeEt.getText().toString().trim().toUpperCase();
                String unitName = unitNameEt.getText().toString().trim();
                String unitPassword = unitPasswordEt.getText().toString().trim();
                String unitTimes = unitTimesEt.getText().toString().trim();
                if (unitCode.isEmpty()){
                    unitCodeEt.setError("Enter unit code");
                    unitCodeEt.requestFocus();
                }else if(unitName.isEmpty()){
                    unitNameEt.setError("Enter unit name");
                    unitNameEt.requestFocus();
                }else if(unitPassword.isEmpty()){
                    unitPasswordEt.setError("Enter unit join password");
                    unitPasswordEt.requestFocus();
                }else if(unitTimes.isEmpty()){
                    unitTimesEt.setError("Enter unit attend times");
                    unitTimesEt.requestFocus();
                }else {
                    Log.d(TAG, "onClick: " + unitCode + unitName);
                    LoadingDialog dialog = new LoadingDialog(AddUnitLecturer.this);
                    dialog.loadingAlertDialog();
                    Query query = FirebaseDatabase.getInstance().getReference("units").orderByChild("unitCode").equalTo(unitCode);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                Unit unit = new Unit(userId, unitCode, unitName,unitPassword,unitTimes);
                                units.child(unitCode).setValue(unit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myunits.child(unitCode).setValue(unit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismissAlertDialog();
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        Toast.makeText(AddUnitLecturer.this, "Unit is added", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        dialog.dismissAlertDialog();
                                                        Toast.makeText(AddUnitLecturer.this, "Failed to add unit try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            dialog.dismissAlertDialog();
                                            Toast.makeText(AddUnitLecturer.this, "Failed please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                dialog.dismissAlertDialog();
                                Toast.makeText(AddUnitLecturer.this, "Failed Unit Exists Contact admin", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
    }
}