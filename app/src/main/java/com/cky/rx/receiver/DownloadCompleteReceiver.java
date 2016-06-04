package com.cky.rx.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cky.rx.util.DaoUtil;

/**
 * Created by cuikangyuan on 16/6/2.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, context.getString(R.string.download_complete),Toast.LENGTH_SHORT).show();
        //取得 request id
        long request_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        DaoUtil.checkBookExistAndUpdate(String.valueOf(request_id));
    }
}
