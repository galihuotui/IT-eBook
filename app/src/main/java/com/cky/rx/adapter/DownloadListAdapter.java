package com.cky.rx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cky.greendao.Book;
import com.cky.rx.R;
import com.cky.rx.data.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cuikangyuan on 16/5/8.
 */
public class DownloadListAdapter extends RecyclerView.Adapter {

    List<Book> books;
    private Context mContext;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public DownloadListAdapter(Context context) {
        super();
        mContext = context;
    }

    public  Book getItemData(int position) {
        return books.get(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v);
                }

            }
        });
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder)holder;

        //viewHolder.itemView.setOnClickListener(listener);
        Book item = books.get(position);

        viewHolder.tvTitle.setText(item.getBook_name());
        viewHolder.tvStatus.setText(mapStatus(item.getDownload_status()));
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public void setItems(List<Book> items) {
        this.books = items;
        notifyDataSetChanged();
    }

    public List<Book> getItems() {
        return books;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvStatus)
        TextView tvStatus;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String mapStatus(String statusCode) {

        String status = null;

        switch (statusCode) {
            case Constants.STATUS_DOWNLOAD_SUCCESS:
                status = mContext.getString(R.string.download_success);
                break;
            case Constants.STATUS_DOWNLOAD_FAIL:
                status = mContext.getString(R.string.download_fail);
                break;
            case Constants.STATUS_DOWNLOADING:
                status = mContext.getString(R.string.downloading);
                break;
            default:
                status = mContext.getString(R.string.unknown);
        }
        return status;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
