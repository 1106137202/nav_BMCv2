package com.example.nav_bmcv2.mapTool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.RectEvaluator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_bmcv2.mapTool.my.Api;
import com.example.nav_bmcv2.mapTool.my.Data;
import com.example.nav_bmcv2.mapTool.my.InfoWindowLayout;
import com.example.nav_bmcv2.mapTool.my.Method;
import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.databinding.FragmentFinishBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        private GoogleMap mMap;
//        private FragmentFinishBinding binding;
        private FragmentFinishBinding binding;

        private ViewGroup infoWindow;
        private TextView stationName, stationAddr, stationLatLng, nowAttr, attrText1, attr1, attrText2, attr2, attrText3, attr3, memoText, now, nowAddr, nowLatLng;
        private Spinner memoAttr;
        private Button cancel, confirm;
        private OnInfoWindowElemTouchListener cancelListener, confirmListener;
        private MapWrapperLayout mapWrapperLayout;

        // 定義這個權限要求的編號
        private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION = 100;
        private GoogleApiClient mGoogleApiClient;
        private FusedLocationProviderClient mFusedLocationClient;
        private LocationCallback mLocationCallback;
        private LocationManager mLocationMgr;


        static final LatLng latlng1 = new LatLng(28.5355, 77.3910);
        static final LatLng latlng2 = new LatLng(28.6208768, 77.3726377);

        private InfoWindowLayout infoWindowLayout;

        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                //將layout新增至頁面中
                View view = inflater.inflate(R.layout.fragment_maps, container, false);
                //取得map_relative_layout
                mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_relative_layout);

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                //同步mapFragment
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                new Thread(new Runnable() {
                        @Override
                        public void run() {
                           Api.login();
                        }
                }).start();
                // 建立一個GoogleApiClient物件。
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                //設定位置更新所執行的事情
                mLocationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null)
                                        return;
                                Location location = locationResult.getLastLocation();
                                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                                new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                                Api.snaptoRoad(point);
                                                Api.punchInPoint(Data.now.get(Data.now.size()-1).latitude, Data.now.get(Data.now.size()-1).longitude);
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        public void run() {
                                                                View icon_marker = (View)view.findViewById(R.id.icon_marker_count);
//                                                                mMap.addMarker(new MarkerOptions().position(Data.now.get(Data.now.size()-1)).title("目前")
//                                                                        // below line is use to add custom marker on our map.
//                                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.getViewBitmap(icon_marker))));
                                                                mMap.addMarker(new MarkerOptions().position(Data.now.get(Data.now.size()-1)).title("目前")
                                                                        // below line is use to add custom marker on our map.
                                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.person, getActivity()))));
                                                        }
                                                });
                                        }
                                }).start();

                                Data.now_position = point;
                                System.out.println(mMap);
//                                mMap.addMarker(new MarkerOptions().position(Data.now.get(Data.now.size()-1)).title("目前")
//                                        // below line is use to add custom marker on our map.
//                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.person, getActivity()))));

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Data.now_position, 20));
                                //my_map.moveCamera(Data.now_position);
                                Toast.makeText(getActivity(), "更新位置", Toast.LENGTH_SHORT).show();
                        }
                };

                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                Log.d("poly", "poly");
                                ArrayList<LatLng> stat = new ArrayList<LatLng>();
                                Method.dir(22.651108,120.312858,22.653916, 120.322504);
                                Api.Polyline_decoder(Data.POIN, stat);
                                Method.dir(22.653916, 120.322504, 22.630083, 120.338154);
                                Api.Polyline_decoder(Data.POIN, stat);
                                Method.dir(22.630083, 120.338154, 22.504033, 120.386722);
                                Api.Polyline_decoder(Data.POIN, stat);
                                Method.dir(22.504033, 120.386722, 22.796455, 120.457184);
                                Api.Polyline_decoder(Data.POIN, stat);
                                Method.dir(22.796455, 120.457184,22.651108,120.312858);
                                Api.Polyline_decoder(Data.POIN, stat);

                                LatLng location1 = new LatLng(22.653916, 120.322504);
                                LatLng location2 = new LatLng(22.630083, 120.338154);
                                LatLng location3 = new LatLng(22.504033, 120.386722);
                                LatLng location4 = new LatLng(22.796455, 120.457184);

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                                RelativeLayout icon = (RelativeLayout)view.findViewById(R.id.icon_marker_count);
                                                mMap.addMarker(new MarkerOptions().position(location1)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.convertViewToBitmap(icon))));
                                                mMap.addMarker(new MarkerOptions().position(location2)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.flashb, getContext()))));
                                                mMap.addMarker(new MarkerOptions().position(location3)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.flash, getContext()))));
                                                mMap.addMarker(new MarkerOptions().position(location4)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.flash, getContext()))));

                                                PolylineOptions polylineOptions = new PolylineOptions();
                                                for(int i=0; i< stat.size();i++){
                                                        polylineOptions.add(stat.get(i)).width(25).color(getResources().getColor(R.color.route, null));
                                                }
//                        polylineOptions.color();
                                                polylineOptions.width(9f);
                                                if(polylineOptions.getWidth()<10) {
                                                        polylineOptions.width(polylineOptions.getWidth() * 6);
                                                }
//                        polyline = mMap.addPolyline(polylineOptions);
                                                mMap.addPolyline(polylineOptions);
                                        }
                                });


                        }
                }).start();


                mLocationMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                return view;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                }
                mMap.setMyLocationEnabled(true);



        mapWrapperLayout.init(mMap, getPixelsFromDp(getActivity(), 39 + 20));
        //----------------------------------------------------------------------------------------
        InfoWindowLayout infoWindowLayout = new InfoWindowLayout(getActivity(), mMap, mapWrapperLayout);
        infoWindow = infoWindowLayout.set_infoWindow();
        //----------------------------------------------------------------------------------------
//        mMap.addMarker(new MarkerOptions().position(Data.now_position).title("目前")
//                // below line is use to add custom marker on our map.
//                .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.person, getActivity()))));
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Data.now_position, 20));

        }

private void enableLocation(boolean on) {
        if (ContextCompat.checkSelfPermission(getActivity(),
        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
        // 這項功能尚未取得使用者的同意
        // 開始執行徵詢使用者的流程
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
        AlertDialog.Builder altDlgBuilder =
        new AlertDialog.Builder(getActivity());
        altDlgBuilder.setTitle("提示");
        altDlgBuilder.setMessage("App需要啟動定位功能。");
        altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
        altDlgBuilder.setCancelable(false);
        altDlgBuilder.setPositiveButton("確定",
        new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface,
        int i) {
        // 顯示詢問使用者是否同意功能權限的對話盒
        // 使用者答覆後會執行onRequestPermissionsResult()
        ActivityCompat.requestPermissions(getActivity(),
        new String[]{
        android.Manifest.permission.ACCESS_FINE_LOCATION},
        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
        }
        });
        altDlgBuilder.show();

        return;
        } else {
        // 顯示詢問使用者是否同意功能權限的對話盒
        // 使用者答覆後會執行callback方法onRequestPermissionsResult()
        ActivityCompat.requestPermissions(getActivity(),
        new String[]{
        android.Manifest.permission.ACCESS_FINE_LOCATION},
        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);

        return;
        }
        }

        // 這項功能之前已經取得使用者的同意，可以直接使用
        if (on) {
        // 取得上一次定位資料
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
@Override
public void onSuccess(Location location) {
        if (location!=null) {
        Toast.makeText(getActivity(), "成功取得上一次定位", Toast.LENGTH_LONG).show();
        //取得上次定位點，並初始化zoom
        LatLng last = new LatLng(location.getLatitude(), location.getLongitude());
        Data.now_position = last;

        //float bearing = location.getBearing();
        //my_layout.setDataViewBearing(last.toString());
        //my_layout.setDataViewBearing(Float.toString(bearing));
        } else {
        Toast.makeText(getActivity(), "沒有上一次定位的資料", Toast.LENGTH_LONG).show();
        }
        }
        });

        // 準備一個LocationRequest物件，設定定位參數，在啟動定位時使用
        LocationRequest locationRequest = LocationRequest.create();
        // 設定二次定位之間的時間間隔，單位是千分之一秒。
        locationRequest.setInterval(5000);
        // 二次定位之間的最大距離，單位是公尺。
        locationRequest.setSmallestDisplacement(5);

        // 啟動定位，如果GPS功能有開啟，優先使用GPS定位，否則使用網路定位。
        if (mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        locationRequest.setPriority(
        LocationRequest.PRIORITY_HIGH_ACCURACY);
        Toast.makeText(getActivity(), "使用GPS定位",
        Toast.LENGTH_LONG).show();
        } else if (mLocationMgr.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER)) {
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        Toast.makeText(getActivity(), "使用網路定位",
        Toast.LENGTH_LONG).show();
        }

        // 啟動定位功能
        mFusedLocationClient.requestLocationUpdates(
        locationRequest, mLocationCallback, Looper.myLooper());
        } else {
        // 停止定位功能
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        Toast.makeText(getActivity(), "停止定位", Toast.LENGTH_LONG)
        .show();
        }
        }
@Override
public void onStart() {
        super.onStart();
        // 啟動 Google API。
        mGoogleApiClient.connect();
        }
@Override
public void onPause() {
        super.onPause();
        // 停止定位
        enableLocation(false);
        }
@Override
public void onStop() {
        super.onStop();
        // 停用 Google API
        Toast.makeText(getActivity(), "停用Google API", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.disconnect();
        }
@Override
public void onConnected(@Nullable Bundle bundle) {
        // Google API 連線成功時會執行這個方法
        Toast.makeText(getActivity(), "Google API 連線成功", Toast.LENGTH_SHORT).show();
        // 啟動定位
        enableLocation(true);
        }
@Override
public void onConnectionSuspended(int i) {
        // Google API 無故斷線時，才會執行這個方法
        // 程式呼叫disconnect()時不會執行這個方法
        switch (i) {
        case CAUSE_NETWORK_LOST:
        Toast.makeText(getActivity(), "網路斷線，無法定位",
        Toast.LENGTH_LONG).show();
        break;
        case CAUSE_SERVICE_DISCONNECTED:
        Toast.makeText(getActivity(), "Google API 異常，無法定位",
        Toast.LENGTH_LONG).show();
        break;
        }
        }
@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // 和 Google API 連線失敗時會執行這個方法
        Toast.makeText(getActivity(), "Google API 連線失敗", Toast.LENGTH_SHORT).show();
        }
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 檢查收到的權限要求編號是否和我們送出的相同
        if (requestCode == REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION) {
        if (grantResults.length != 0 &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // 再檢查一次，就會進入同意的狀態，並且順利啟動。
        enableLocation(true);
        return;
        }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
public static int getPixelsFromDp(Context context, float dp) {
final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
        }


}