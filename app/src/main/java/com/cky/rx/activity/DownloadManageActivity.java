package com.cky.rx.activity;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.greendao.Book;
import com.cky.rx.R;
import com.cky.rx.activity.base.BaseActivity;
import com.cky.rx.adapter.DownloadListAdapter;
import com.cky.rx.data.Constants;
import com.cky.rx.util.DaoUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadManageActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.rv_download_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_download_list_empty)
    TextView tvEmpty;

    private DownloadListAdapter mDownloadListAdapter = new DownloadListAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manage);

        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.nav_manage));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_left));

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mDownloadListAdapter);
        mDownloadListAdapter.setOnItemClickListener(new DownloadListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                openBook(view);
            }
        });
        getData();
    }


    private void getData() {
        List<Book> queryResult = DaoUtil.getBooks();
        if (queryResult.size() > 0) {
            hideDownloadListEmptyView();
        } else {
            showDownloadListEmptyView();
        }
        mDownloadListAdapter.setItems(queryResult);
    }
    public static void start(Context context) {
        Intent intent = new Intent(context, DownloadManageActivity.class);
        context.startActivity(intent);
    }

    private void showDownloadListEmptyView() {
        if (tvEmpty.getVisibility() == View.GONE) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void hideDownloadListEmptyView() {
        if (tvEmpty.getVisibility() == View.VISIBLE) {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    public void openBook(View v) {
        final int position = mRecyclerView.getChildAdapterPosition(v);
        if (RecyclerView.NO_POSITION != position) {
            Book book = mDownloadListAdapter.getItemData(position);
            if (book.getDownload_status().equals(Constants.STATUS_DOWNLOAD_SUCCESS)) {
                DownloadManager.Query query = new DownloadManager.Query().setFilterById(Long.valueOf(book.getRequest_id()));
                Cursor cursor =null;
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                cursor = downloadManager.query(query);

                if (cursor != null && cursor.moveToFirst()) {
                    String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    String mimetype = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(uri), mimetype);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        DownloadManageActivity.this.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(DownloadManageActivity.this, getString(R.string.activity_not_found), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }
    }
}
