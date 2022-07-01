package com.example.nav_bmcv2.mapTool.my;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    private static void login(double lat, double lng){
        String url = "https://dr.kymco.com/api/login";
        //String token = "";
        String acc = "ky5910";
        String pwd = "KY5910";

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("acc=" + acc + "&pwd=" + pwd, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String header = response.header("Set-Cookie");
                com.example.nav_bmcv2.mapTool.my.Data.token = header;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        station(com.example.nav_bmcv2.mapTool.my.Data.token);
    }

    private static String station(String token) {
        String url = "https://dr.kymco.com/es/eAPI/fmStation";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", token)
                .build();
        String jsonStr = "";
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String result = response.body().string();
                JSONArray array = new JSONArray(result);
                List<JSONObject> list = new ArrayList<JSONObject>();
                List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
                com.example.nav_bmcv2.mapTool.my.Data.stationArray = new LatLng[array.length()];
                com.example.nav_bmcv2.mapTool.my.Data.stationName = new String[array.length()];
                com.example.nav_bmcv2.mapTool.my.Data.staLat = new double[array.length()];
                com.example.nav_bmcv2.mapTool.my.Data.staLng = new double[array.length()];
                com.example.nav_bmcv2.mapTool.my.Data.address = new String[array.length()];
                for (int i = 0; i<array.length(); i++){
                    list.add(array.getJSONObject(i));
                    JSONObject obj = new JSONObject(list.get(i).toString());
                    com.example.nav_bmcv2.mapTool.my.Data.stationLat = (double)obj.get("lat");
                    com.example.nav_bmcv2.mapTool.my.Data.stationLng = (double)obj.get("lng");
                    String name = (String) obj.get("spDesc");
                    String add = (String) obj.get("street");
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("lat", com.example.nav_bmcv2.mapTool.my.Data.stationLat);
                    params.put("lng", com.example.nav_bmcv2.mapTool.my.Data.stationLng);
                    params.put("name", name);
                    params.put("address", add);
                    ls.add(params);
                    LatLng stationPoint = new LatLng(com.example.nav_bmcv2.mapTool.my.Data.stationLat, com.example.nav_bmcv2.mapTool.my.Data.stationLng);
                    com.example.nav_bmcv2.mapTool.my.Data.staLat[i] = com.example.nav_bmcv2.mapTool.my.Data.stationLat;
                    com.example.nav_bmcv2.mapTool.my.Data.staLng[i] = com.example.nav_bmcv2.mapTool.my.Data.stationLng;
                    com.example.nav_bmcv2.mapTool.my.Data.stationArray[i] = stationPoint;
                    com.example.nav_bmcv2.mapTool.my.Data.stationName[i] = name;
                    com.example.nav_bmcv2.mapTool.my.Data.address[i] = add;
                    Log.d("station", com.example.nav_bmcv2.mapTool.my.Data.stationArray.toString());
                    Log.d("lat", "" + com.example.nav_bmcv2.mapTool.my.Data.stationLat);
                    Log.d("lng", "" + com.example.nav_bmcv2.mapTool.my.Data.stationLng);
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public static void dir(double slat, double slng, double elat, double elng){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/directions/json?destination=" + elat + "," + elng + "&mode=driving&origin=" + slat + "," + slng + "&language=zh-TW&key=AIzaSyBm6kC5U0Y_k3lfmggPRurC0C3o3wiUlA0")
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String result = response.body().string();
                ArrayList<String> routes = new ArrayList<String>();
                ArrayList<String> points = new ArrayList<String>();
                ArrayList<String> overview_polyline = new ArrayList<String>();
                Method.get_json(result, routes, "routes");
                Method.get_json(routes, overview_polyline, "overview_polyline");
                Method.get_json(overview_polyline, points, "points");
                com.example.nav_bmcv2.mapTool.my.Data.POIN = points;
                Method.show_json(points);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void dataList(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url("http://nekomatsuri.ddns.net/test.json")
                .method("GET", body)
                .build();
    }

}
