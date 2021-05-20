package com.finix.kisiiattend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    ChipNavigationBar chipNavigationBar;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    FirebaseDatabase rootDode;
    DatabaseReference userdetails;
    DatabaseReference myunits;
    public String name, registrationNumber, email, occupation;
    private UnitAdapter adapter;
    private ArrayList<Unit> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "onCreate: " + userId);
        list = new ArrayList<>();
        sharedPreferences = getSharedPreferences("occupation", Context.MODE_PRIVATE);

        adapter = new UnitAdapter(this,list);

        rootDode = FirebaseDatabase.getInstance();
        userdetails = rootDode.getReference("users").child(userId);
        myunits = userdetails.child("myunits");

        LoadingDialog dialog = new LoadingDialog(MainActivity.this);
        dialog.loadingAlertDialog();


        ValueEventListener userDetails = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name = user.getName();
                registrationNumber = user.getRegistrationNumber();
                email = user.getEmail();
                occupation = user.getOccupation();

                dialog.dismissAlertDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userdetails.addValueEventListener(userDetails);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        myunits.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Unit unit = dataSnapshot.getValue(Unit.class);
                    list.add(unit);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);

        bottomMenu();
    }

    @Override
    public void onClick(View v) {

    }
    private void bottomMenu() {


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.addUnit:
                        Log.d(TAG, "onItemSelected: add Unit clicked");
                        if(occupation.equals("student")){
                            startActivity(new Intent(MainActivity.this,AddUnitStudent.class));
                        }else {
                            startActivity(new Intent(MainActivity.this,AddUnitLecturer.class));
                        }
                        chipNavigationBar.setItemSelected(R.id.addUnit,false);
                        break;
                    case R.id.profile:
                        showUsDetails();
                        break;
                    case R.id.logOutBtn:
                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                        alertDialogBuilder.setTitle("Log out");
                        alertDialogBuilder.setMessage("Are you sure you want to log out?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this,SelectOccupation.class));
                                finish();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                chipNavigationBar.setItemSelected(R.id.logOutBtn,false);
                                dialog.dismiss();
                            }
                        });
                        alertDialogBuilder.show();
                        break;
                }
            }
        });


    }
    public void showUsDetails(){
        String[] items = {registrationNumber, name, occupation};
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setTitle("User Details");
        alertDialogBuilder.setItems(items,null);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chipNavigationBar.setItemSelected(R.id.profile,false);
            }
        });
        alertDialogBuilder.show();
    }
}