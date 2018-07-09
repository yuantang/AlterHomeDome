package com.coder.alterhomedome;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TUS on 2018/7/6.
 */

public class AlterService extends Service {
    String TAG="AlterService";
    Handler handler;
    Dialog dialog;
    Toast toast;
    View toastView;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = (LayoutInflater) AlterService.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                toastView = inflater.inflate(R.layout.toast_clear_layout, null);
                TextView textView = toastView.findViewById(R.id.tv_toast_clear);
                textView.setText(String.format(getString(R.string.alter_msg),getString(R.string.app_name)));
                if (Rom.isEMUI()){
                    showDialog();
                }else {
                    showToast();
                }
                stopSelf();
            }
        },100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        },1300);

    }

    private void showDialog() {
        if (dialog==null) {
            dialog = new Dialog(this, R.style.dialog);
        }
        if (Build.VERSION.SDK_INT >=26 ) { //8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY-1);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        dialog.setContentView(toastView);
        dialog.show();
    }

    private void showToast() {
        if (toast==null) {
            toast = new Toast(getApplicationContext());
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        toast.show();
    }

}

