package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegSearchActivity extends AppCompatActivity {
    private static final String TAG = "RegSearchActivity";

    private RecyclerView recyclerView;
    private RegSearchAdapter adapter;
    private ArrayList<Attend>  list;
    FirebaseDatabase rootDode;
    DatabaseReference myAttendance;
    String unitCode, regNo;
    TextView name, regno;
    DatabaseReference userdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_search);

        LoadingDialog dialog =new LoadingDialog(RegSearchActivity.this);
        dialog.loadingAlertDialog();
        name= findViewById(R.id.name);
        regno = findViewById(R.id.regno);

        list = new ArrayList<>();
        adapter = new RegSearchAdapter(this,list);
        rootDode = FirebaseDatabase.getInstance();
        Intent intent =getIntent();
        unitCode = intent.getStringExtra("unitCode");
        regNo = intent.getStringExtra("regNo");
        regno.setText(regNo);

        myAttendance = rootDode.getReference("attendance").child(unitCode).child(regNo);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        myAttendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismissAlertDialog();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Attend attend = dataSnapshot.getValue(Attend.class);
                    list.add(attend);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("registrationNumber").equalTo(regNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText("Exits");
                    Log.d(TAG, "onDataChange: "+snapshot.getChildren());
                    snapshot.getChildren();
                }else {
                    name.setTextColor(getResources().getColor(R.color.red));
                    name.setText("Student not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}