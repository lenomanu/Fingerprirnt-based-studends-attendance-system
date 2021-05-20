package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OveralAttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private ArrayList<TotalAttandance> list;
    FirebaseDatabase rootDode;
    DatabaseReference overallAttendance;
    String unitCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overal_attendance);
        list = new ArrayList<>();
        adapter = new AttendanceAdapter(this,list);
        rootDode = FirebaseDatabase.getInstance();
        Intent intent =getIntent();
        unitCode = intent.getStringExtra("unitCode");

        overallAttendance = rootDode.getReference("attendance").child(unitCode).child("overallAttendance");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        overallAttendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    TotalAttandance totalAttandance = dataSnapshot.getValue(TotalAttandance.class);
                    list.add(totalAttandance);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}