package com.finix.kisiiattend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.Date;
import java.util.EventListener;
import java.util.UUID;
import java.util.concurrent.Executor;

public class UnitDetailsActivity extends AppCompatActivity {
    public static final String TAG = "UnitDetails";
    String unitCode, unitName, occupation, regNo, uuid,totalAtted,unitTimes, sName;
    TextView unitNameTv, unitCodeTv, authStatetv;
    Button allowSignIn, checkIn, fprintstatus, checkAttendance, reportsBtn, unitreportBtn,cheatBtn;
    SharedPreferences sharedPreferences;
    FirebaseDatabase rootDode;
    DatabaseReference userdetails, locationRef, attendance ,tme,ttotal,all;
    double latitude,longitude;
    double distance;
    FusedLocationProviderClient fusedLocationProviderClient;
    LoadingDialog dialog;
    double latitude1,longitude1;
    ValueEventListener locationDetails;
    LatLng lecturerloc, studentloc;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    boolean status = false;
    String Status;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_details);
        Intent intent = getIntent();
        unitCodeTv = findViewById(R.id.unitCode);
        unitNameTv = findViewById(R.id.unitName);
        authStatetv = findViewById(R.id.tv);
        allowSignIn = findViewById(R.id.allowsignin);
        fprintstatus = findViewById(R.id.fprintstatus);
        checkIn = findViewById(R.id.checkin);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        unitName = intent.getStringExtra("unitName");
        unitCode = intent.getStringExtra("unitCode");
        unitTimes = intent.getStringExtra("unitTimes");
        uuid = UUID.randomUUID().toString();
        checkAttendance =findViewById(R.id.checkOveralAttendance);
        reportsBtn = findViewById(R.id.reports);
        unitreportBtn = findViewById(R.id.unitreport);
        cheatBtn = findViewById(R.id.cheat);

        checkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OveralAttendanceActivity.class);
                intent.putExtra("unitCode", unitCode);
                startActivity(intent);
            }
        });

        dialog = new LoadingDialog(UnitDetailsActivity.this);
        LoadingDialog dialog = new LoadingDialog(UnitDetailsActivity.this);
        dialog.loadingAlertDialog();
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIn();
            }
        });
        allowSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowSignIn();
            }
        });

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(UnitDetailsActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                fprintstatus.setText("Successfully checked in for the lecturer ");
                addCheckToDatabase();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                fprintstatus.setText("Failed");
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric lecture Check-in")
                .setSubtitle("Check-in using your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        unitCodeTv.setText(unitCode);
        unitNameTv.setText(unitName);
        rootDode = FirebaseDatabase.getInstance();
        userdetails = rootDode.getReference("users").child(userId);
        locationRef =rootDode.getReference("units").child(unitCode).child("location");

        locationDetails = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    com.finix.kisiiattend.Location location = snapshot.getValue(com.finix.kisiiattend.Location.class);
                    latitude1 = Double.parseDouble(location.getLatitude());
                    longitude1 = Double.parseDouble(location.getLongitude());
                    dialog.dismissAlertDialog();
                    lecturerloc = new LatLng(latitude1,longitude1);
                    status = true;
                    authStatetv.setText(String.valueOf(latitude1)+","+String.valueOf(longitude1));
                }else {
                    dialog.dismissAlertDialog();
                    Status = "Lecturer has not posted location yet";
                    Toast.makeText(getApplicationContext(),"location details not updated ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener userDetails = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                occupation = user.getOccupation();
                regNo = user.getRegistrationNumber();
                sName = user.getName();
                if (occupation.equals("lecturer")) {
                    allowSignIn.setVisibility(View.VISIBLE);
                    checkAttendance.setVisibility(View.VISIBLE);
                    reportsBtn.setVisibility(View.VISIBLE);
                    unitreportBtn.setVisibility(View.VISIBLE);
                    dialog.dismissAlertDialog();
                } else {
                    cheatBtn.setVisibility(View.VISIBLE);
                    locationRef.addValueEventListener(locationDetails);
                    checkIn.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "onDataChange: Occupation :" + occupation);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userdetails.addValueEventListener(userDetails);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UnitDetailsActivity.this);
    }

    private void updateTotal() {
        Log.d(TAG, "onClick: Update total started");
       String regn = replace(regNo);
        tme = rootDode.getReference("attendance").child(unitCode).child(regn);
        ttotal = rootDode.getReference("attendance").child(unitCode).child("overallAttendance").child(regn);

        tme.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAttended = (int) snapshot.getChildrenCount();
                    totalAtted = String.valueOf(totalAttended);
                    Log.d(TAG, "onDataChange: On database "+unitTimes);
                    Log.d(TAG, "onDataChange: Total Attended : "+ totalAtted);
                    Integer t = Integer.parseInt(totalAtted);
                    Integer r = Integer.parseInt(unitTimes);
                    float percentage = ((float) t) / r *100;
                    Log.d(TAG, "Percentage calclated: "+ percentage);
                    TotalAttandance totalAttandance = new TotalAttandance(regNo,sName,String.valueOf(Math.round(percentage)));
                    ttotal.setValue(totalAttandance).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_LONG).show();
                                dialog.dismissAlertDialog();
                            }else{
                                Toast.makeText(getApplicationContext(),"Update Was No Successfully Retry later",Toast.LENGTH_LONG).show();
                                dialog.dismissAlertDialog();
                            }

                        }
                    });
                }else{
                    totalAtted = "0";
                    Log.d(TAG, "onDataChange: Total Attended :"+ totalAtted);
                    dialog.dismissAlertDialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addCheckToDatabase() {
        String regno = replace(regNo);
        attendance = rootDode.getReference("attendance").child(unitCode).child(regno).child(uuid);
        Date date = new Date();
        all = rootDode.getReference("attendance").child(unitCode).child("all").child(uuid);

        String currentdate  = (String) DateFormat.format("E-MMMM-d",date);
        Log.d(TAG, "addCheckToDatabase: "+replace(regNo));
        dialog.loadingAlertDialog();

        Attend today = new Attend(regNo,currentdate,sName);
        all.setValue(today).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Updated checked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        attendance.setValue(today).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    fprintstatus.setVisibility(View.VISIBLE);
                    updateTotal();
                    //dialog.dismissAlertDialog();
                }else{
                    fprintstatus.setVisibility(View.VISIBLE);
                    fprintstatus.setText("Check in was not succefull check for internet");
                    dialog.dismissAlertDialog();
                }
            }
        });
    }

    private void CheckIn() {
        Log.d(TAG, "CheckIn: On Click");
        if(status == false){
            Toast.makeText(getApplicationContext(),Status,Toast.LENGTH_LONG).show();
            return;
        } else if(ActivityCompat.checkSelfPermission(UnitDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(UnitDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(UnitDetailsActivity.this,new  String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    private void AllowSignIn() {
        Log.d(TAG, "AllowSignIn: Onclick done");
        if(ActivityCompat.checkSelfPermission(UnitDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(UnitDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(UnitDetailsActivity.this,new  String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults.length >0 &&  (grantResults[0]+ grantResults[1])== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            Toast.makeText(getApplicationContext(),"Permision Denied",Toast.LENGTH_LONG).show();
        }
    }
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            dialog.loadingAlertDialog();
                         LocationRequest locationRequest = LocationRequest.create()
                                 .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                 .setInterval(10)
                                 .setFastestInterval(10)
                                 .setNumUpdates(1);
                         LocationCallback locationCallback =new LocationCallback() {
                             @Override
                             public void onLocationResult(@NonNull LocationResult locationResult) {
                                 Location location1 = locationResult.getLastLocation();
                                 latitude = location1.getLatitude();
                                 longitude = location1.getLongitude();
                                 authStatetv.setText(String.valueOf(longitude)+","+String.valueOf(latitude));
                                 if(location1 != null){
                                     if(occupation.equals("lecturer")){
                                         updateLocationToFirebase();
                                     }else{
                                         studentloc = new LatLng(latitude,longitude);
                                         compareLocation();
                                     }
                                     
                                 }else{
                                     dialog.dismissAlertDialog();
                                     Toast.makeText(UnitDetailsActivity.this, "Check reason location is null", Toast.LENGTH_SHORT).show();
                                 }

                             }
                         };
                         //Location updates
                         fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                 locationCallback, Looper.myLooper());
        }else{
            startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void compareLocation() {
        Log.d(TAG, "compareLocation: Comparing Location");
        distance = SphericalUtil.computeDistanceBetween(studentloc,lecturerloc);
        Log.d(TAG, "compareLocation: "+latitude+","+longitude);
        Log.d(TAG, "compareLocation: "+latitude1+","+longitude1);
        Toast.makeText(getApplicationContext(),"Distance :"+String.format("%.4f",distance/1000)+"km",Toast.LENGTH_LONG).show();
        dialog.dismissAlertDialog();
        if(distance<0.011){
            fprintstatus.setText("You are not in the lecture room");
            fprintstatus.setVisibility(View.VISIBLE);
        }else {
            biometricPrompt.authenticate(promptInfo);
        }
    }

    public void updateLocationToFirebase() {
        com.finix.kisiiattend.Location location = new com.finix.kisiiattend.Location(String.valueOf(longitude),String.valueOf(latitude));
        locationRef.setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    LoadingDialog loadingDialog = new LoadingDialog(UnitDetailsActivity.this);
                    Toast.makeText(getApplicationContext(),"LOCATION POSTED SUCCESSFUL",Toast.LENGTH_LONG).show();
                    dialog.dismissAlertDialog();
                }else{
                    dialog.dismissAlertDialog();
                    Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static String replace(String text){
        text = text.replace("/", "");
        return text;
    }

    public void reportsActivity(View view) {
        Intent i = new Intent(getApplicationContext(),ReportsActivity.class);
        i.putExtra("unitName", unitName);
        i.putExtra("unitCode", unitCode);
        startActivity(i);
    }

    public void unitReportsActivity(View view) {
        Intent i = new Intent(getApplicationContext(),UnitReportActivity.class);
        i.putExtra("unitCode", unitCode);
        startActivity(i);
    }

    public void cheat(View view) {
        status = false;
        Status = "Not in class";
    }
}