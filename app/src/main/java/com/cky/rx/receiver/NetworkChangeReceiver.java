package com.cky.rx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.cky.rx.R;

/**
 * Created by cuikangyuan on 16/5/30.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED == mobileState
                && NetworkInfo.State.CONNECTED != wifiState) {
            //Toast.makeText(context, context.getString(R.string.under_mobile_network),Toast.LENGTH_LONG).show();
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            Toast.makeText(context, context.getString(R.string.no_network),Toast.LENGTH_LONG).show();
        } else if (wifiState != null && wifiState == NetworkInfo.State.CONNECTED) {
            //Toast.makeText(context, context.getString(R.string.under_wifi),Toast.LENGTH_LONG).show();
        }
    }
}
