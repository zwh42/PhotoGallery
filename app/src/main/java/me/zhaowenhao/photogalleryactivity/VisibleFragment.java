package me.zhaowenhao.photogalleryactivity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhaowenhao on 16/4/27.
 */
public class VisibleFragment extends Fragment {
    public static final String TAG = "VisibleFragment";

    private BroadcastReceiver mOnshowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getActivity(), "Got a broadcast: " + intent.getAction(), Toast.LENGTH_LONG).show();
            Log.i(TAG, "cancel notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnshowNotification,filter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onPause(){
        super.onResume();
        getActivity().unregisterReceiver(mOnshowNotification);
    }
}
