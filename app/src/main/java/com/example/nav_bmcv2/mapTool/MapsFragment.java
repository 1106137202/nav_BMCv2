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
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_bmcv2.mapTool.my.Api;
import com.example.nav_bmcv2.mapTool.my.Data;
import com.example.nav_bmcv2.mapTool.my.InfoWindowLayout;
import com.example.nav_bmcv2.mapTool.my.Method;
import com.example.nav_bmcv2.R;

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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener{

        private GoogleMap mMap;
//        private FragmentFinishBinding binding;


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
        private TextView txtNo;
        private ImageView imgVIew;

        static final LatLng latlng1 = new LatLng(28.5355, 77.3910);
        static final LatLng latlng2 = new LatLng(28.6208768, 77.3726377);

        private InfoWindowLayout infoWindowLayout;
        
        private Marker station_Marker;
        private boolean initcamera = true;


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


//                View myView = factory.inflate(R.layout.icon_marker_count, null);
//                txtNo = myView.findViewById(R.id.txtNo);
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
                                Data.now_position = point;
                                initCamera(Data.now_position);
                                new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                                Api.snaptoRoad(point);
                                                Api.punchInPoint(Data.now.get(Data.now.size()-1).latitude, Data.now.get(Data.now.size()-1).longitude);
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        public void run() {
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


                                //System.out.println(mMap);
//                                mMap.addMarker(new MarkerOptions().position(Data.now.get(Data.now.size()-1)).title("目前")
//                                        // below line is use to add custom marker on our map.
//                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.person, getActivity()))));

                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Data.now_position, 20));

                                //my_map.moveCamera(Data.now_position);
                                Toast.makeText(getActivity(), "更新位置", Toast.LENGTH_SHORT).show();
                        }
                };


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
//                mMap.addMarker(new MarkerOptions().position(Data.now_position).title("目前")
//                        // below line is use to add custom marker on our map.
//                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.person, getActivity()))));
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Data.now_position, 20));
                mMap.setOnMarkerClickListener(this);
                add_station();
        }
        private void enableLocation(boolean on) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 這項功能尚未取得使用者的同意，開始執行徵詢使用者的流程
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
                                altDlgBuilder.setTitle("提示");
                                altDlgBuilder.setMessage("App需要啟動定位功能。");
                                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                                altDlgBuilder.setCancelable(false);
                                altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface,int i) {
                                                // 顯示詢問使用者是否同意功能權限的對話盒
                                                // 使用者答覆後會執行onRequestPermissionsResult()
                                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                                        }
                                });
                                altDlgBuilder.show();
                                return;
                        } else {
                                // 顯示詢問使用者是否同意功能權限的對話盒
                                // 使用者答覆後會執行callback方法onRequestPermissionsResult()
                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
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
                                                // Toast.makeText(getActivity(), "成功取得上一次定位", Toast.LENGTH_LONG).show();
                                                // 取得上次定位點，並初始化zoom
                                                LatLng last = new LatLng(location.getLatitude(), location.getLongitude());
                                                Data.now_position = last;
                                                initCamera(Data.now_position);
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
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                //Toast.makeText(getActivity(), "使用GPS定位",Toast.LENGTH_LONG).show();
                        } else if (mLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                                //Toast.makeText(getActivity(), "使用網路定位", Toast.LENGTH_LONG).show();
                        }
                        // 啟動定位功能
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                } else {
                        // 停止定位功能
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        //Toast.makeText(getActivity(), "停止定位", Toast.LENGTH_LONG).show();
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
                        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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


        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
                //get the map container height
                LinearLayout mapContainer = (LinearLayout) infoWindow.findViewById(R.id.LLinfowindow);
                int container_height = mapContainer.getHeight();

                Projection projection = mMap.getProjection();

                LatLng markerLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
                Point pointHalfScreenAbove = new Point(markerScreenPosition.x, markerScreenPosition.y - (container_height / 2));

                LatLng aboveMarkerLatLng = projection.fromScreenLocation(pointHalfScreenAbove);

                marker.showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(aboveMarkerLatLng)
                        .zoom(mMap.getCameraPosition().zoom)
                        .bearing(mMap.getCameraPosition().bearing)
                        .tilt(mMap.getCameraPosition().tilt)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
                onMarkerDoubleClick(marker);
                return true;
        }
        public boolean onMarkerDoubleClick(Marker marker) {
                //get the map container height
                LinearLayout mapContainer = (LinearLayout) infoWindow.findViewById(R.id.LLinfowindow);
                int container_height = mapContainer.getHeight();

                Projection projection = mMap.getProjection();

                LatLng markerLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
                Point pointHalfScreenAbove = new Point(markerScreenPosition.x, markerScreenPosition.y - (container_height / 2));

                LatLng aboveMarkerLatLng = projection.fromScreenLocation(pointHalfScreenAbove);

                marker.showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(aboveMarkerLatLng)
                        .zoom(mMap.getCameraPosition().zoom)
                        .bearing(mMap.getCameraPosition().bearing)
                        .tilt(mMap.getCameraPosition().tilt)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
                return true;
        }

        public void initCamera(LatLng point){
                if(initcamera) {
                        initcamera = false;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(point)      // Sets the center of the map to Mountain View
                                .zoom(15)                   // Sets the zoom
                                .bearing(0)                // Sets the orientation of the camera to east
                                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                                .build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
        }
        public ArrayList<LatLng> create_polyline(){
                ArrayList<LatLng> stat = new ArrayList<LatLng>();
                Method.dir(22.651108, 120.312858, 22.653916, 120.322504);
                Api.Polyline_decoder(Data.POIN, stat);
                Method.dir(22.653916, 120.322504, 22.630083, 120.338154);
                Api.Polyline_decoder(Data.POIN, stat);
                Method.dir(22.630083, 120.338154, 22.504033, 120.386722);
                Api.Polyline_decoder(Data.POIN, stat);
                Method.dir(22.504033, 120.386722, 22.796455, 120.457184);
                Api.Polyline_decoder(Data.POIN, stat);
                Method.dir(22.796455, 120.457184, 22.651108, 120.312858);
                Api.Polyline_decoder(Data.POIN, stat);
                return stat;
        }
        public void create_station(){
                if(Data.loctionArray.size()==0) {
                        LatLng location1 = new LatLng(22.653916, 120.322504);
                        LatLng location2 = new LatLng(22.630083, 120.338154);
                        LatLng location3 = new LatLng(22.504033, 120.386722);
                        LatLng location4 = new LatLng(22.796455, 120.457184);
                        Data.loctionArray.add(location1);
                        Data.loctionArray.add(location2);
                        Data.loctionArray.add(location3);
                        Data.loctionArray.add(location4);
                }
        }
        public void add_station(){
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                LayoutInflater factory = LayoutInflater.from(getContext());
                                Log.d("poly", "poly");
                                create_station();

                                int number = 1;
                                ArrayList<Bitmap> icon = new ArrayList<>();

                                for (int n = 0; n<Data.loctionArray.size(); n++){
                                        View myView = (View) factory.inflate(R.layout.icon_marker_count, null);
                                        txtNo = myView.findViewById(R.id.txtNo);
                                        System.out.println(number);
                                        txtNo.setText(Integer.toString(number));
                                        //icon.add(Method.convertViewToBitmap(txtNo, 5));
                                        Bitmap b = Method.convertViewToBitmap(myView, 5);
                                        icon.add(Method.convertViewToBitmap(myView, 5));
                                        int finalN = n;
                                        int finalNumber = number;
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                        station_Marker = mMap.addMarker(new MarkerOptions().position(Data.loctionArray.get(finalN))
                                                                .icon(BitmapDescriptorFactory.fromBitmap(b)));
                                                        station_Marker.setTag(finalNumber);
                                                }
                                        });
                                        number++;
                                }
                                ArrayList<LatLng> stat = create_polyline();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                                PolylineOptions polylineOptions = new PolylineOptions();
                                                System.out.println(stat);
                                                for(int i=0; i< stat.size();i++){
                                                        polylineOptions.add(stat.get(i)).width(25).color(getResources().getColor(R.color.route, null));
                                                }
                                                polylineOptions.width(9f);
                                                if(polylineOptions.getWidth()<10) {
                                                        polylineOptions.width(polylineOptions.getWidth() * 3);
                                                }
                                                mMap.addPolyline(polylineOptions);
                                        }
                                });
                        }
                }).start();
        }

}