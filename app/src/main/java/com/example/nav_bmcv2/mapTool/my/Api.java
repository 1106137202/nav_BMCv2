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

    public static void login(){
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
//        station(com.example.nav_bmcv2.mapTool.my.Data.token);
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

    public static void Polyline_decoder(ArrayList<String> list, ArrayList<LatLng> Poly_List) {
        //get all the polylines point
        for (int i = 0; i < list.size(); i++) {
            String encoded = list.get(i);
            int index = 0, len = encoded.length();
            int decoded_lat = 0;
            int decoded_lng = 0;
            //get one char in loop
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    //get on char to calculate decoder
                    b = encoded.charAt(index++);
                    //step 1: number reduce 63
                    b = b - 63;
                    //step 2: number logic operation(AND) 0x1f and then left shift one bit
                    result |= (b & 0x1f) << shift;
                    //step 3: five bit for one block
                    shift += 5;
                } while (b >= 0x20);
                //step 4: if first bit is one need to bit upside down, and do shift on right one bit.
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                decoded_lat += dlat;
                shift = 0;
                result = 0;
                //do the same thing with lng
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                decoded_lng += dlng;
                LatLng p = new LatLng((((double) decoded_lat / 1E5)), (((double) decoded_lng / 1E5)));
                Poly_List.add(p);
            }
        }
    }

    public static String  punchInPoint(double lat, double lng){
        String url = "https://dr.kymco.com/es/eAPI/fmCheckInPoint";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n\"uid\": \"0a3c6d13-dac1-48f9-b001-f92d00773e3e\"," +
                "\r\n\"lat\": \"" + lat + "\",\r\n\"lng\": \"" + lng + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", Data.token)
                .build();

        String jsonStr = "";
        try{
            Response response = client.newCall(request).execute();
            jsonStr = response.body().string();

        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonStr);
        return jsonStr;
    }

    public static void snaptoRoad(LatLng latlng){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        System.out.println(latlng);
        String tmp = latlng.latitude + "," + latlng.longitude;
        String url = "https://roads.googleapis.com/v1/snapToRoads?path=" + tmp +"&key=AIzaSyBm6kC5U0Y_k3lfmggPRurC0C3o3wiUlA0";
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String result = response.body().string();
                ArrayList<String> snappedPoints = new ArrayList<>();
                ArrayList<String> location = new ArrayList<>();
                ArrayList<String> latitude = new ArrayList<>();
                ArrayList<String> longitude = new ArrayList<>();
                Method.get_json(result, snappedPoints, "snappedPoints");
                Method.show_json(snappedPoints);
                Method.get_json(snappedPoints, location, "location");
                Method.get_json(location, latitude, "latitude");
                Method.get_json(location, longitude, "longitude");
                System.out.println(latitude);
                Data.now.add(new LatLng(Double.parseDouble(latitude.get(0)),Double.parseDouble(longitude.get(0))));

            }
//            if (response.isSuccessful()){
//                String result = response.body().string();
//                JSONArray array = new JSONArray(result);
//                List<JSONObject> list = new ArrayList<JSONObject>();
//                List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
//                Data.snappedPoints = new Object[array.length()];
//                for (int i = 0; i< array.length(); i++){
//                    list.add(array.getJSONObject(i));
//                    JSONObject obj = new JSONObject(list.get(i).toString());
//                    Data.snappedPoints = (Object)obj.get("location");
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
