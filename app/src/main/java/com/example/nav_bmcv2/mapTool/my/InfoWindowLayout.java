package com.example.nav_bmcv2.mapTool.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import java.util.ArrayList;
import java.util.Date;

public class InfoWindowLayout {
    private ImageView imgView;
    private Context context;
    private GoogleMap mMap;
    private ViewGroup infoWindow;

    private OnInfoWindowElemTouchListener cancelListener, confirmListener, imageButtonListener;
    private MapWrapperLayout mapWrapperLayout;

    private Button cancel;
    private Button confirm;
    private EditText edtMemo;
    private ImageButton imageButton;

    private final String[] items = {"待解析", "待料/待工", "其他"};
    private final ArrayList<Integer> itemSelect = new ArrayList<>();

    public InfoWindowLayout(Context con, GoogleMap map, MapWrapperLayout WrapperLayout){
        context = con;
        mMap = map;
        mapWrapperLayout = WrapperLayout;
    }
    public ViewGroup set_infoWindow(){
        infoWindow = (ViewGroup)((Activity) context).getLayoutInflater().inflate(R.layout.custom_infowindow, null);

        cancel = (Button)infoWindow.findViewById(R.id.cancel);
        confirm = (Button)infoWindow.findViewById(R.id.confirm);
        edtMemo = (EditText)infoWindow.findViewById(R.id.edtMemo);

        LayoutInflater change = LayoutInflater.from(infoWindow.getContext());
        imgView = infoWindow.findViewById(R.id.imgView);

        //cancel
        cancelListener = new OnInfoWindowElemTouchListener(cancel){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //todo
                marker.hideInfoWindow();
            }
        };
        //cancel事件加入
        cancel.setOnTouchListener(cancelListener);
        System.out.println("cancel事件加入");

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
                                LayoutInflater factory = LayoutInflater.from(context);
                                View myView = (View) factory.inflate(R.layout.icon_marker_count, null);
                                ImageView imgView = myView.findViewById(R.id.imgView);
                                imgView.setImageResource(R.drawable.marker);
                                TextView txtNo = myView.findViewById(R.id.txtNo);
                                String tag = Integer.toString((int)marker.getTag());
                                txtNo.setText(tag);
                                Bitmap b = Method.convertViewToBitmap(myView, 5);
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(b));
                                marker.hideInfoWindow();
                            }
                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                System.out.println("confirm dialog");
            }
        };
        //confirm事件加入
        confirm.setOnTouchListener(confirmListener);
        System.out.println("confirm事件加入");

        imageButtonListener = new OnInfoWindowElemTouchListener(imageButton){
            @Override
            protected void onClickImageButton(View v, Marker marker){
                AlertDialog.Builder build = new AlertDialog.Builder(context);
                build.setTitle("請選擇您要備註的項目");
                build.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedItemId, boolean isSelected) {
                        if (isSelected) {
                            itemSelect.add(selectedItemId);
                        } else if (itemSelect.contains(selectedItemId)) {
                            itemSelect.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                });
                build.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    }
                });
                build.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                build.show();
            }
        };
        System.out.println("FYBR1");
        //設定googlemap中的WindowsAdapter所取得的文字對應
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                //初始化marker
                cancelListener.setMarker(marker);
                confirmListener.setMarker(marker);
                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
        System.out.println("Adapter");
        return infoWindow;
    }
}
