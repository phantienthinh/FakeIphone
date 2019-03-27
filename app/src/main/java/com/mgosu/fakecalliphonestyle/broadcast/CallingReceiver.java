package com.mgosu.fakecalliphonestyle.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import com.mgosu.fakecalliphonestyle.activity.CallActivity;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;


public class CallingReceiver extends BroadcastReceiver {
    private SharedPreferencesManager preferencesManager;
    public String TAG ="tag";

    @Override
    public void onReceive(Context context, Intent intent) {
        preferencesManager = SharedPreferencesManager.getInstance(context);
        if (isScreenOn(context)){
            Log.e(TAG,"screen = true");
            preferencesManager.setScreenOnOff(true);

        }else {
            Log.e(TAG, "screen=false ");
            preferencesManager.setScreenOnOff(false);
        }

        if (preferencesManager.getcheckTimeOutApp()){
            intent = new Intent(context,CallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }


    }
    public boolean isScreenOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm.isScreenOn();
        }
    }
}
