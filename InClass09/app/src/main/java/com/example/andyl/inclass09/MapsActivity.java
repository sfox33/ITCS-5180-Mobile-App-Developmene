package com.example.andyl.inclass09;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //ArrayList<JsonParse> points = new ArrayList<>()
    JsonParse jp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Gson gson = new Gson();
        //JsonParse jp = gson.fromJson(R.raw.trip, JsonParse.class);
        String json = "";
        Log.d("Demo", getResources().getResourceName(R.raw.trip));
        try {
            //InputStream is = this.getAssets().open(getResources().getResourceName(R.raw.trip));
            InputStream is = getResources().openRawResource(R.raw.trip);
            //InputStream is = this.getAssets().open("C:/Users/andyl/Documents/AndroidStudioApps/InClass09/app/src/main/res/raw/trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String (buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Demo", "JSON String is " + json);

        jp = gson.fromJson(json, JsonParse.class);

        Log.d("Demo", "IT Works" + jp.toString());

        /*Polyline line = mMap.addPolyline(new PolylineOptions()
            .add(jp.getLatLng(0), jp.getLatLng(0), jp.getLatLng(0),
                    jp.getLatLng(0), jp.getLatLng(0), jp.getLatLng(0),
                    jp.getLatLng(0), jp.getLatLng(0), jp.getLatLng(0)));*/



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(MapsActivity.this, "Yes, this is working", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng start = jp.getList().get(0);
        LatLng end = jp.getList().get(jp.getList().size()-1);

        mMap.addMarker(new MarkerOptions().position(start).title("Start location"));
        mMap.addMarker(new MarkerOptions().position(end).title("End Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

        Polyline line = mMap.addPolyline(new PolylineOptions());
        line.setPoints(jp.getList());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i= 0; i < jp.getList().size(); i++) {
            builder.include(jp.getList().get(i));
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);
    }
}
