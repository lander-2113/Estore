package com.bsrdigicoin.estore1.activities.location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bsrdigicoin.estore1.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* what this activity does?
* this activity opens and locates the user.
* then after locating we get deails like:-
* 1. house no.
* 2. landmark.
* 3. directions to reach.
*
* but how to get current location, after gps has located the phone?
*
*
* */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private List<Address> address;

    public LatLng userlocation;

    /*****************************/
    public EditText houseNoEdittext;
    public EditText landmarkEdittext;
    public EditText directionsEdittext;
    /*****************************/

    private boolean markerDraged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        houseNoEdittext = findViewById(R.id.hno_edit);
        landmarkEdittext = findViewById(R.id.landmark_edit);
        directionsEdittext = findViewById(R.id.direction_edit);

        markerDraged = false;
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //creating object for location call back event
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                location = locationResult.getLastLocation();

            }
        };

        //creating location request object;
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(20000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //get the client location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                MapsActivity.this.location = location;

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 42) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.i("requestcode",  "" + PackageManager.PERMISSION_GRANTED);
                // if granted then set the variable to true
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Instruction:
     * if the marker draged then get user location there,
     * if marker not draged then get location from user location variable.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // this consumes batter more than usual.
        mMap.setMyLocationEnabled(true);

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,  locationListener);

        if(location != null) {
            // Add a marker in Sydney and move the camera
            Toast.makeText(MapsActivity.this, "Maps ready", Toast.LENGTH_SHORT).show();
            float zoomlevel = 16.6f;
            userlocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userlocation).draggable(true).title("User Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, zoomlevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                @Override
                public void onMarkerDrag(@NonNull Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    LatLng latlng=marker.getPosition();
                    userlocation = new LatLng(latlng.latitude, latlng.longitude);
                    Toast.makeText(MapsActivity.this, userlocation.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {
                }

            });
        } else {
            Toast.makeText(MapsActivity.this, "location is null move the marker",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // the function that gets called when the confirm button
    // is clicked, in this activity's layout file.
    public void done(View view) {
        String houseNo = "", landmark = "", directions = "";
        try {
            houseNo = houseNoEdittext.getText().toString();
        } catch(Exception e) {
            Log.e("error", "error in house no edittext", e);
        }

        try {
            landmark = landmarkEdittext.getText().toString();
        } catch(Exception e) {
            Log.e("error", "error in landmark no edittext", e);
        }

        try {
            directions = directionsEdittext.getText().toString();
        } catch(Exception e) {
            Log.e("error", "error in directions no edittext", e);
        }

        try{
            Geocoder geocoder=new Geocoder(MapsActivity.this);
            if (userlocation != null) {
                address=geocoder.getFromLocation(userlocation.latitude,userlocation.longitude,1);
                Address add=address.get(0);
                //Toast.makeText(MapsActivity.this, ""+add.getAddressLine(0), Toast.LENGTH_SHORT).show();

                Map<String, String> addressTable = new HashMap<>();
                addressTable.put("latitude", String.valueOf(userlocation.latitude));
                addressTable.put("longitude", String.valueOf(userlocation.longitude));

                if (add != null) {
                    if(add.getSubLocality() != null){
                        addressTable.put("sublocality", add.getSubLocality());
                    }

                    if(add.getLocality() != null){
                        addressTable.put("locality", add.getLocality());
                    }

                    if(add.getSubThoroughfare() != null){
                        addressTable.put("subthoroughfare", add.getSubThoroughfare());
                    }

                    if(add.getThoroughfare() != null){

                        addressTable.put("thoroughfare", add.getThoroughfare());
                    }

                    if(add.getSubAdminArea() != null){
                        addressTable.put("subadminarea", add.getSubAdminArea());
                    }

                    if(add.getAdminArea() != null){
                        addressTable.put("adminarea", add.getAdminArea());
                    }

                    if(add.getPostalCode() != null){
                        addressTable.put("postalcode", add.getPostalCode());
                    }

                    // check if the houseno, landmark, and directions are given.
                    if (houseNo.length() > 0 && landmark.length() > 0 && directions.length() > 0) {
                        addressTable.put("house no", houseNo.trim());
                        addressTable.put("landmark", landmark.trim());
                        addressTable.put("directions", directions.trim());
                        addressTable.put("type", "0");
                        // after all this send the data though volley.
                        Toast.makeText(MapsActivity.this, addressTable.toString(), Toast.LENGTH_LONG).show();
                        sendUserLocationData(addressTable);

                    } else {
                        Toast.makeText(MapsActivity.this, "Enter all the value!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Couldn't get the location.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MapsActivity.this, "Error getting location", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex)
        {
            Log.e("error", "some error occured", ex);
            //Toast.makeText(MapsActivity.this, "error: Internet and gps should be on"+ex, Toast.LENGTH_SHORT).show();
            Toast.makeText(MapsActivity.this, "Error in the done method", Toast.LENGTH_SHORT).show();
        }

    }

    public void sendUserLocationData(Map<String, String> addresstable) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MapsActivity.this, response, Toast.LENGTH_SHORT).show();
                //finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "error", error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return addresstable;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}