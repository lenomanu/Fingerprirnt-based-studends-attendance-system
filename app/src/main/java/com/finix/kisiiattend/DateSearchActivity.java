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

public class DateSearchActivity extends AppCompatActivity {
    private static final String TAG = "RegSearchActivity";

    private RecyclerView recyclerView;
    private RegSearchAdapter adapter;
    private ArrayList<Attend> list;
    FirebaseDatabase rootDode;
    String unitCode, regNo, date;
    TextView name, regno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_search);

        LoadingDialog dialog = new LoadingDialog(DateSearchActivity.this);
        dialog.loadingAlertDialog();
        name = findViewById(R.id.name);
        regno = findViewById(R.id.regno);

        list = new ArrayList<>();
        adapter = new RegSearchAdapter(this, list);
        rootDode = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        unitCode = intent.getStringExtra("unitCode");
        regNo = intent.getStringExtra("regNo");
        date = intent.getStringExtra("date");
        regno.setText(regNo);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Query query = FirebaseDatabase.getInstance().getReference("attendance").child(unitCode).child(regNo).orderByChild("date").equalTo(date);
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
                    Toast.makeText(getApplicationContext(), "Student did not attend", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}