package com.cky.rx.fragment.base;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;

/**
 * Created by cuikangyuan on 16/5/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected Subscription subscription;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
