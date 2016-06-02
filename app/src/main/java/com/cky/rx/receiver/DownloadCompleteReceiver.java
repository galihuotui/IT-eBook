package com.cky.rx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cky.rx.R;

/**
 * Created by cuikangyuan on 16/6/2.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.download_complete),Toast.LENGTH_SHORT).show();
    }
}
