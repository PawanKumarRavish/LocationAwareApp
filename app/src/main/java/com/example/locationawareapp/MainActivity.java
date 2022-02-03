package com.example.locationawareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvlong, tvlat, tvalt, tvacc, tvadd;
    Button btn;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvlong = findViewById(R.id.tvLongitude);
        tvlat = findViewById(R.id.tvLatitude);
        tvalt = findViewById(R.id.tvAltitude);
        tvacc = findViewById(R.id.tvAccuracy);
        tvadd = findViewById(R.id.tvAddress);
        btn = findViewById(R.id.btnGetLC);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 44);

                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if(!GrantedPermission()){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },1);

        }
        else{
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        tvlong.setText(Html.fromHtml("<b>Longitude:" + addresses.get(0).getLongitude()));
                        tvlat.setText(Html.fromHtml("<b>Latitude:" + addresses.get(0).getLatitude()));
                        tvacc.setText(Html.fromHtml("<b>Postal:" + addresses.get(0).getPostalCode()));
                        tvalt.setText(Html.fromHtml("<b>Country" + addresses.get(0).getCountryName()));
                        tvadd.setText(Html.fromHtml("<b>Address:</b>" + addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
//
//    private void requestLocationPermission(){
//        ActivityCompat.requestPermissions(this,new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION
//        },44);
//    }
}

    private boolean GrantedPermission() {
        return ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;
    }
    }