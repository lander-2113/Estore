package com.bsrdigicoin.estore1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bsrdigicoin.estore1.DBproperties.SharedPrefManager;
import com.bsrdigicoin.estore1.MainActivity;
import com.bsrdigicoin.estore1.activities.location.MapsActivity;
import com.bsrdigicoin.estore1.fragments.OrdersFragment;
import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.fragments.UserAccountFragment;
import com.bsrdigicoin.estore1.recyclerView.ordersRecycerView.Orders;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private LocationManager locationManager;
    private LocationListener locationListener;
    static boolean locationPermissionGranted, gpsEnabled;
    List<Orders> ordersList;
    RequestQueue requestQueue;
    TextView nav_header_username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        HomeActivity.locationPermissionGranted = false;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new OrdersFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();

        // this to set the user name in the nav header.
        View header = navigationView.getHeaderView(0);
        nav_header_username = header.findViewById(R.id.nav_head_username);
        nav_header_username.setText(SharedPrefManager.getInstance(getApplicationContext()).getUsername());

        // to know if the gps is enabled or not?
        if (!isGpsEnabled()) {
            buildAlertMessageNoGps();
        }
    }// oncreate ends

    // menu options added in the appbar of home.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar_home, menu);
        return super.onCreateOptionsMenu(menu);

    }
    // react to action itms clicks with the onOptionsItemSelected() method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_location:
                // before getting the user location please confirm the user permissions
                // to get the user location.
                if (isGrantedAccessFineLocation() && isGpsEnabled()) {
                    // warning: here all the permission are dealth with,
                    // and only here the activity should be started.
                    // as all the security checks are dealth with.
                    startActivity(new Intent(this, MapsActivity.class));
                } else {
                    if (!HomeActivity.locationPermissionGranted) {
                        getLocationPermission();
                    }

                    if(!HomeActivity.gpsEnabled) {
                        buildAlertMessageNoGps();
                    }
                    Toast.makeText(this, "location permission not granted,\nor gps not enabled!",
                            Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "User location should be selected.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        HomeActivity.gpsEnabled = manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
        return HomeActivity.gpsEnabled;
    }

    private void buildAlertMessageNoGps() {
        if (!isGpsEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            HomeActivity.gpsEnabled = true;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            HomeActivity.gpsEnabled = false;

                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void getLocationPermission() {
        try{
            if (!isGrantedAccessFineLocation()) {// if permission not given get it.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);
                Toast.makeText(this, "Grant permission", Toast.LENGTH_SHORT).show();
            }else {
                /*
                 * if the user has already given permission
                 * then ask for location updates.
                 * */
                HomeActivity.locationPermissionGranted = true;
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } catch(Exception e) {
            Log.e("location", "requesterror", e);
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public boolean isGrantedAccessFineLocation() {
        // simple funciton to check fine location
        // permission granted or not.
        HomeActivity.locationPermissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return HomeActivity.locationPermissionGranted;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        switch (id) {
            case R.id.nav_orders:
                fragment = new OrdersFragment();
                break;
            case R.id.nav_user_info:
                fragment = new UserAccountFragment();
                break;
            case R.id.nav_help:
                intent = new Intent(getApplicationContext(), HelpActivity.class);
                break;
            case R.id.nav_ogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                intent = new Intent(getApplicationContext(), FeedbackActivity.class);
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClickDone(View view) {
        // Code that runs when the FAB is clicked
        Intent intent = new Intent(getApplicationContext(), StoresActivity.class);
        startActivity(intent);
    }
}