package com.example.andyl.inclass09;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonParse {

    ArrayList<Point> points = new ArrayList<Point>();

    public static class Point {
        double latitude;
        double longitude;
    }

    @Override
    public String toString() {
        return "JsonParse{" +
                "points=" + points +
                '}';
    }

    public LatLng getLatLng(int index){
        return new LatLng(points.get(index).latitude, points.get(index).longitude);
    }

    public ArrayList<LatLng> getList() {
        ArrayList<LatLng> lt = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            lt.add(new LatLng(points.get(i).latitude, points.get(i).longitude) );
        }
        return lt;
    }
}
