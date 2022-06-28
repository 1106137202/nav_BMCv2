package com.example.nav_bmcv2.mapTool.my;

import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Data {


    public static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    public String commandStr;
    public LocationManager locationManager;
    public double lat = 0;
    public double lng = 0;
    //    private MapsLayout mapsLayout;
    public static LatLng[] stationArray;
    public static String[] stationName;
    public static double[] staLat;
    public static double[] staLng;
    public static String token = "";
    public static double stationLat = 0;
    public static double stationLng = 0;
    public static String[] address;
    public static ArrayList<String> POIN = new ArrayList<String>();
    public static String[] attr = {"OK", "NG"};

    public static LatLng now_position;

}

