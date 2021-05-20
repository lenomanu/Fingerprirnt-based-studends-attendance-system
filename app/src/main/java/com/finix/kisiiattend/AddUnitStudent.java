package com.finix.kisiiattend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddUnitStudent extends AppCompatActivity {
    public static final  String TAG = "AddUnitStudent";
    Button addUnitBtn;
    EditText unitCodeEt, unitPasswordEt;
    TextView errorTv;
    FirebaseDatabase rootDode;
    String userId,unitCode,unitName, unitTimes, unitPassword;
    DatabaseReference unitDetails;
    DatabaseReference myunits;
    private String fPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit_student);
        Log.d(TAG, "onCreate: Activity created");
        addUnitBtn = findViewById(R.id.addUnitBtn);
        unitCodeEt = findViewById(R.id.unitCodex);
        unitPasswordEt = findViewById(R.id.unitPasswordx);
        errorTv = findViewById(R.id.error);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        rootDode = FirebaseDatabase.getInstance();


        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitCode = unitCodeEt.getText().toString().trim().toUpperCase();
                unitPassword = unitPasswordEt.getText().toString().trim();
                if(unitCode.isEmpty()){
                    errorTv.setText("Please enter unit Code and try again");
                    return;
                }else if(unitPassword.isEmpty()){
                    errorTv.setText("Please enter Enrolment Key and try again");
                    return;
                } else {
                Log.d(TAG, "onClick:UnitCode:"+unitCode);
                Log.d(TAG, "onClick: Add unit process started ");
                    LoadingDialog dialog = new LoadingDialog(AddUnitStudent.this);
                    dialog.loadingAlertDialog();
                Query query = FirebaseDatabase.getInstance().getReference("units").orderByChild("unitCode").equalTo(unitCode);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.d(TAG, "onDataChange: Unit exits");
                            unitDetails = rootDode.getReference("units").child(unitCode);
                            ValueEventListener unitDetailsClass = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Unit unit = snapshot.getValue(Unit.class);
                                    unitTimes = unit.getUnitTimes();
                                    unitName = unit.getUnitName();
                                    unitCode = unit.getUnitCode();
                                    fPassword = unit.getUnitPassword();
                                    Log.d(TAG, "onDataChange: UNIT NAME "+unitName);
                                    Log.d(TAG, "onDataChange: UNIs Timees "+unitTimes);

                                    if (unitPassword.equals(fPassword)) {
                                        myunits = rootDode.getReference("users").child(userId).child("myunits").child(unitCode);
                                        Unit unit1 = new Unit(unitTimes,unitCode,unitName);
                                        myunits.setValue(unit1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    dialog.dismissAlertDialog();
                                                    AddUnitStudent.super.onBackPressed();
                                                    Toast.makeText(getApplicationContext(),"Unit enrolled successfully",Toast.LENGTH_LONG);
                                                    finish();
                                                }else{

                                                }
                                            }
                                        });
                                    }else {
                                        dialog.dismissAlertDialog();
                                        errorTv.setText("Wrong enrolment key");
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    dialog.dismissAlertDialog();
                                    errorTv.setText(error.getMessage());
                                    Log.d(TAG, "onCancelled: "+error.getMessage());

                                }
                            };
                            unitDetails.addValueEventListener(unitDetailsClass);
                        }else {
                            Log.d(TAG, "onDataChange: Unit does not exist");
                            errorTv.setText("Unit does not exist");

                            dialog.dismissAlertDialog();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: Error reading database");
                        Toast.makeText(getApplicationContext(), "Unit does not exist", Toast.LENGTH_LONG);
                        dialog.dismissAlertDialog();
                    }
                });}
            }
        });

    }

    public void backSuper(View view) {
        super.onBackPressed();
    }
}