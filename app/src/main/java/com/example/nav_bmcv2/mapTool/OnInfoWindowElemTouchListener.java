package com.example.nav_bmcv2.mapTool;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.Marker;

public class OnInfoWindowElemTouchListener implements OnTouchListener {
    private final View view;
//    private final Drawable bgDrawableNormal;
//    private final Drawable bgDrawablePressed;
    private final Handler handler = new Handler();

    private Marker marker;
    private boolean pressed = false;



    public OnInfoWindowElemTouchListener(View view) {
        this.view = view;
//        this.bgDrawableNormal = bgDrawableNormal;
//        this.bgDrawablePressed = bgDrawablePressed;
    }

    public void setMarker(Marker marker) {
        System.out.println("Listener setMarker");
        this.marker = marker;
    }

    @Override
    public boolean onTouch(View vv, MotionEvent event) {
        System.out.println("Listener onTouch");
        if (0 <= event.getX() && event.getX() <= view.getWidth() &&
                0 <= event.getY() && event.getY() <= view.getHeight())
        {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: startPress(); break;

                // We need to delay releasing of the view a little so it shows the pressed state on the screen
                case MotionEvent.ACTION_UP: handler.postDelayed(confirmClickRunnable, 150); break;

                case MotionEvent.ACTION_CANCEL: endPress(); break;
                default: break;
            }
        }
        else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress();
        }

        return true;
        //return false;
    }

    private void startPress() {
        System.out.println("Listener startPress");
        if (!pressed) {
            pressed = true;
            //handler.removeCallbacks(imageButtonRunnable);
            handler.removeCallbacks(confirmClickRunnable);
            //view.setBackground(bgDrawablePressed);
            if (marker != null)
                marker.showInfoWindow();
        }
    }

    private boolean endPress() {
        System.out.println("Listener endPress");
        if (pressed) {
            this.pressed = false;
            //handler.removeCallbacks(imageButtonRunnable);
            handler.removeCallbacks(confirmClickRunnable);
            //view.setBackground(bgDrawableNormal);
            if (marker != null)
                marker.showInfoWindow();
            return true;
        }
        else
            return false;
    }

    private final Runnable confirmClickRunnable = new Runnable() {
        public void run() {
            System.out.println("Listener confirmClickRunnable");
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };

<<<<<<< HEAD
=======
    private final Runnable imageButtonRunnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("Listener imageButtonRunnable");
             if (endPress()){
                 onClickImageButton(view, marker);
             }
        }
    };
>>>>>>> origin/master

    /**
     * This is called after a successful click
     */
<<<<<<< HEAD
=======
    protected void onClickImageButton(View v, Marker marker){
        System.out.println("Listener onClickImageButton");
    }
>>>>>>> origin/master

    protected void onClickConfirmed(View v, Marker marker) {
        System.out.println("Listener onClickConfirmed");
    }


}
