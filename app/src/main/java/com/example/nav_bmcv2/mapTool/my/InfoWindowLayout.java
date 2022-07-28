package com.example.nav_bmcv2.mapTool.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.text.Edits;
import android.icu.text.IDNA;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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


    private OnInfoWindowElemTouchListener editListener;
    //private OnInfoWindowElemOnClickListener editListener;
    private EditText memoEdt;
    private EditText tmpText;
    private InputMethodManager imm;
    private LatLng init_marker_position = new LatLng(0,0);
    private boolean keyboard_flag = false;



    public InfoWindowLayout(Context con, GoogleMap map, MapWrapperLayout WrapperLayout, EditText text){
        context = con;
        mMap = map;
        mapWrapperLayout = WrapperLayout;
        tmpText = text;
    }

    public ViewGroup set_infoWindow(){
        infoWindow = (ViewGroup)((Activity) context).getLayoutInflater().inflate(R.layout.custom_infowindow, null);

        cancel = (Button)infoWindow.findViewById(R.id.cancel);
        confirm = (Button)infoWindow.findViewById(R.id.confirm);
        edtMemo = (EditText)infoWindow.findViewById(R.id.edtMemo);

        LayoutInflater change = LayoutInflater.from(infoWindow.getContext());
        imgView = infoWindow.findViewById(R.id.imgView);
        //-------------------------------------------------------
        memoEdt = (EditText) infoWindow.findViewById(R.id.memoEdt);

        //-------------------------------------------------------
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

        //-----------------------------------------------------------------
        //EditText
        editListener = new OnInfoWindowElemTouchListener(memoEdt){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //System.out.println(marker);
                System.out.println("FYBR2");
                tmpText.setVisibility(View.VISIBLE);
                tmpText.setFocusable(true);
                tmpText.requestFocus();
                //memoEdt.requestFocus();
                extend_keyboard(true);
                //imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(R.id.tmpText, 0);
                //imm.toggleSoftInput(R.id.memoEdt, 0);
//                imm.showSoftInputFromInputMethod(v.getWindowToken(), 0);

                memoEdt.setText(tmpText.getText());
                marker.showInfoWindow();
                tmpText.setOnKeyListener(new View.OnKeyListener(){
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                if(i==66){
                                    view.setVisibility(View.GONE);
                                    view.clearFocus();
                                    extend_keyboard(false);
                                    keyboard_flag = false;
                                    //imm.toggleSoftInput(R.id.tmpText, 0);
                                    memoEdt.setText(tmpText.getText());
                                    marker.showInfoWindow();
                                }
                                return false;
                        }
                });

                //memoEdt.setFocusableInTouchMode(true);
                //memoEdt.requestFocus();
                //return false;
            }
        };
        memoEdt.setOnTouchListener(editListener);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                tmpText.setVisibility(View.GONE);
                tmpText.clearFocus();
                //imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(R.id.tmpText, 1);
                extend_keyboard(false);
                if(keyboard_flag) {
                    keyboard_flag = false;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(init_marker_position)
                            .zoom(mMap.getCameraPosition().zoom)
                            .bearing(mMap.getCameraPosition().bearing)
                            .tilt(mMap.getCameraPosition().tilt)
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else{
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(init_marker_position)
                            .zoom(mMap.getCameraPosition().zoom)
                            .bearing(mMap.getCameraPosition().bearing)
                            .tilt(mMap.getCameraPosition().tilt)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
        //-----------------------------------------------------------------
        //設定googlemap中的WindowsAdapter所取得的文字對應
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                init_marker_position = marker.getPosition();
                //初始化marker
                cancelListener.setMarker(marker);
                confirmListener.setMarker(marker);
                editListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
<<<<<<< HEAD

=======
        System.out.println("Adapter");
>>>>>>> origin/master
        return infoWindow;
    }
    public void extend_keyboard(boolean isOpen){

        if(isOpen && imm==null){
            System.out.println("ON");
            keyboard_flag = true;
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(R.id.tmpText, 1);
        }
        else if(imm!=null){
            System.out.println("OFF");
            imm.toggleSoftInput(R.id.tmpText, 0);
            imm = null;
        }
        else{
            System.out.println("NULL");
            imm = null;
        }
    }
}
