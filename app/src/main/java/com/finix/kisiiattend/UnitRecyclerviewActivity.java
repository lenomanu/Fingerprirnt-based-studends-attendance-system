package com.finix.kisiiattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UnitRecyclerviewActivity extends AppCompatActivity {
    public static final String TAG = "Unit Recyclerview";
    private RecyclerView recyclerView;
    private UnitRecycleviewAdapter adapter;
    private ArrayList<Attend> list;
    FirebaseDatabase rootDode;
    String unitCode, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_recyclerview);

        LoadingDialog dialog = new LoadingDialog(UnitRecyclerviewActivity.this);
        dialog.loadingAlertDialog();

        list = new ArrayList<>();
        adapter = new UnitRecycleviewAdapter(this, list);
        rootDode = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        unitCode = intent.getStringExtra("unitCode");
        date = intent.getStringExtra("date");
        Log.d(TAG, "onCreate: "+date+unitCode);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Query query = rootDode.getReference("attendance").child(unitCode).child("all").orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismissAlertDialog();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Attend attend = dataSnapshot.getValue(Attend.class);
                        list.add(attend);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "No data on this filter", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}