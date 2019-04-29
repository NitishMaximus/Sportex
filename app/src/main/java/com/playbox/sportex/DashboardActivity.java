package com.playbox.sportex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.playbox.sportex.utils.PreferenceUtils;

public class DashboardActivity extends FragmentActivity {

    //private TextView mTextMessage;
    private Dash_frag_1 F1 = new Dash_frag_1();
    private Dash_frag_2 F2 = new Dash_frag_2();
    private Dash_frag_3 F3 = new Dash_frag_3();

    android.os.Handler customHandler = new android.os.Handler();

    private String latitude = "12.929", longitude ="77.557";
    private Location mLocation;
    private FusedLocationProviderClient fusedLocationClient;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.replace(R.id.contentFragment, F1);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    ft.replace(R.id.contentFragment, F2);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                case R.id.navigation_notifications:
                    ft.replace(R.id.contentFragment, F3);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFragment,F1);
        ft.commit();

        customHandler.postDelayed(updateTimerThread, 0);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            checkcurlocation();
            PreferenceUtils.saveLatitude(latitude, DashboardActivity.this);
            PreferenceUtils.saveLongitude(longitude, DashboardActivity.this);
            if(mLocation != null){
                return;
            }
            customHandler.postDelayed(this, 100);
        }
    };
    public void checkcurlocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(DashboardActivity.this);


        //mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(DashboardActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        Task<Location> task = fusedLocationClient.getLastLocation()
                .addOnSuccessListener(DashboardActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Task completed successfully
                    mLocation = task.getResult();
                    if (mLocation != null) {
                        latitude = String.valueOf(mLocation.getLatitude());
                        longitude = String.valueOf(mLocation.getLongitude());
                    }
                    PreferenceUtils.saveLatitude(latitude, DashboardActivity.this);
                    PreferenceUtils.saveLongitude(longitude, DashboardActivity.this);
                } else {
                    // Task failed with an exception
                    Exception exception = task.getException();

                }
            }
        });
    }

}
