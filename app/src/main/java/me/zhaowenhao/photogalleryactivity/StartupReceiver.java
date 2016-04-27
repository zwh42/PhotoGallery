package me.zhaowenhao.photogalleryactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by zhaowenhao on 16/4/27.
 */
public class StartupReceiver extends BroadcastReceiver {

    public static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent){
        Log.i(TAG, "receive broadcast intent: " + intent.getAction());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isOn = prefs.getBoolean(PollService.PREF_IS_ALARM_ON,  false);
        PollService.setServiceAlarm(context, isOn);
    }


}
