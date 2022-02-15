package com.kotlin.taskapplication;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class App extends Application
{
    public static class LoadingDialog extends Dialog {

        public LoadingDialog(@NonNull Context context) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        public LoadingDialog(@NonNull Context context, boolean cancelable) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(cancelable);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        public LoadingDialog(@NonNull Context context, String msg, boolean cancelable) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(cancelable);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);

            //((TextView) findViewById(com.adoisstudio.helper.R.id.msg)).setText(msg);
            ((TextView) findViewById(R.id.msg)).setText(msg);

            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        @Override
        public void show() {
            super.show();
        }

        public void show(String msg) {
            //((TextView) findViewById(com.adoisstudio.helper.R.id.msg)).setText(msg);
            ((TextView) findViewById(R.id.msg)).setText(msg);
            super.show();
        }

        public void setVisibility(boolean isVisible) {
            if (isVisible)
                show();
            else
                dismiss();
        }

        public void setVisibility(boolean isVisible, String msg) {
            if (isVisible)
                show(msg);
            else
                dismiss();
        }

    }//class
    public static boolean isInternetAvailable(Context context) {
        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    // connected to wifi
                    connected = true;
                    break;

                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    connected = true;
                    break;

                default:
                    connected = false;
            }
        } else {
            // not connected to the internet
            connected = false;
        }

        return connected;
    }
}
