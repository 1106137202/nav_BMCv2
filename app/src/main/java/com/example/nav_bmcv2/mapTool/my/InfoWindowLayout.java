package com.example.nav_bmcv2.mapTool.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nav_bmcv2.mapTool.MapWrapperLayout;
import com.example.nav_bmcv2.mapTool.MapsFragment;
import com.example.nav_bmcv2.mapTool.OnInfoWindowElemTouchListener;
import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.mapTool.MapWrapperLayout;
import com.example.nav_bmcv2.mapTool.OnInfoWindowElemTouchListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

public class InfoWindowLayout {
    private Context context;
    private GoogleMap mMap;
    private ViewGroup infoWindow;
    private Button cancel;
    private Button confirm;
    private OnInfoWindowElemTouchListener cancelListener, confirmListener;
    private MapWrapperLayout mapWrapperLayout;
    public InfoWindowLayout(Context con, GoogleMap map, MapWrapperLayout WrapperLayout){
        context = con;
        mMap = map;
        mapWrapperLayout = WrapperLayout;
    }
    public ViewGroup set_infoWindow(){
        infoWindow = (ViewGroup)((Activity) context).getLayoutInflater().inflate(R.layout.custom_infowindow, null);

        cancel = (Button)infoWindow.findViewById(R.id.cancel);
        confirm = (Button)infoWindow.findViewById(R.id.confirm);

        //cancel
        cancelListener = new OnInfoWindowElemTouchListener(cancel){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //todo

                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
            }
        };
        //cancel事件加入
        cancel.setOnTouchListener(cancelListener);

        //confirm
        confirmListener = new OnInfoWindowElemTouchListener(confirm){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                System.out.println(marker);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("確定要在此打卡嗎？").setTitle("您不再範圍內").setIcon(R.drawable.warning);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                marker.remove();
                                System.out.println(marker.getId());
                                marker.remove();
                                mMap.addMarker(new MarkerOptions().position(Data.now_position).title("目前")
                                        // below line is use to add custom marker on our map.
                                        .icon(BitmapDescriptorFactory.fromBitmap(Method.bitmap(R.drawable.flashb, context))));
                            }
                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

                //todo
                Toast.makeText(context, "confirm", Toast.LENGTH_SHORT).show();
            }
        };
        //confirm事件加入
        confirm.setOnTouchListener(confirmListener);
        System.out.println("FYBR1");
        //設定googlemap中的WindowsAdapter所取得的文字對應
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                cancelListener.setMarker(marker);
                confirmListener.setMarker(marker);
                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
        return infoWindow;
    }
}
