package com.example.nav_bmcv2.mapTool.my;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Method {

    private static ArrayList<String> POIN = new ArrayList<String>();

    //計算距離
    public double cal_distence(LatLng Start, LatLng End){
        double EARTH_RADIUS = 6378137.0;
        //double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (Start.latitude * Math.PI / 180.0);
        double radLat2 = (End.latitude * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (Start.longitude - End.longitude) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    //google direction
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
                get_json(result, routes, "routes");
                get_json(routes, overview_polyline, "overview_polyline");
                get_json(overview_polyline, points, "points");
                POIN = points;
                show_json(points);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void get_json(String text, ArrayList<String> arraylist, String tag){
        //JsonConfig conf = new JsonConfig();
        //往JSONArray中新增JSONObject物件。
        //發現JSONArray跟JSONObject的區別就是JSONArray比JSONObject多中括號[]
        String tmp = text;
        if (tmp.charAt(0) != '[') {
            tmp= "[" + tmp + "]";
        }
        try {
            //建立一個JSONArray並帶入JSON格式文字，getString(String key)取出欄位的數值
            JSONArray array = new JSONArray(tmp);
            for (int j = 0; j < array.length(); j++) {
                JSONObject json = array.getJSONObject(j);
                //String ch = delete_english(json.getString(tag));
                //arraylist.add(ch);
                arraylist.add(json.getString(tag));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void get_json(ArrayList<String> text, ArrayList<String> arraylist, String tag){
        //JsonConfig conf = new JsonConfig();
        //往JSONArray中新增JSONObject物件。
        //發現JSONArray跟JSONObject的區別就是JSONArray比JSONObject多中括號[]
        for(int i=0 ;i <text.size();i++) {
            String tmp = text.get(i);
            if (tmp.charAt(0) != '[') {
                tmp= "[" + tmp + "]";
            }
            try {
                //建立一個JSONArray並帶入JSON格式文字，getString(String key)取出欄位的數值
                JSONArray array = new JSONArray(tmp);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject json = array.getJSONObject(j);
                    //String ch = delete_english(json.getString(tag));
                    //arraylist.add(ch);
                    arraylist.add(json.getString(tag));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void show_json(ArrayList<String> text){
        for(int i=0; i<text.size(); i++){
            System.out.println(text.get(i));
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static Bitmap bitmap(int icon, Context context){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(context, icon);;
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus ();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View v) {
        v.setDrawingCacheEnabled(true);//w w  w . j av a 2  s  . c o m
        v.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }

    public static Bitmap getBitmapFromView(View view, int width, int height) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
                View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);//from ww w  . j  a  va 2s .c om
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

}
