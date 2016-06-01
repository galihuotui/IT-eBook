package com.cky.rx.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cky.rx.R;

/**
 * Created by cuikangyuan on 16/5/13.
 */
public class MyLoadMoreListener extends RecyclerView.OnScrollListener {

    private Context mContext;
    private boolean isSlideToLast = false;

    public MyLoadMoreListener(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisiableItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int totalCount = layoutManager.getItemCount();

            if(lastVisiableItem == (totalCount - 1) && isSlideToLast) {
                Toast.makeText(mContext, mContext.getString(R.string.load_more), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            isSlideToLast = true;
        } else {
            isSlideToLast = false;
        }
    }
}
