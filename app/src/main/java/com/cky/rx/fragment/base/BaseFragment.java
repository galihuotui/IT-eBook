package com.cky.rx.fragment.base;

import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by cuikangyuan on 16/5/7.
 */
public abstract class BaseFragment extends Fragment{
    protected Subscription subscription;


    public BaseFragment() {
        super();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

}
