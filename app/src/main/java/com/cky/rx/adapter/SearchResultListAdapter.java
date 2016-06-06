package com.cky.rx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cky.rx.R;
import com.cky.rx.model.BookItemToShow;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cuikangyuan on 16/5/8.
 */
public class SearchResultListAdapter extends RecyclerView.Adapter {

    List<BookItemToShow> books;
    private Context mContext;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public SearchResultListAdapter(Context context) {
        super();
        mContext = context;
    }

    public  BookItemToShow getItemData(int position) {
        return books.get(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
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
        BookItemToShow item = books.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .fitCenter()
                .placeholder(R.mipmap.default_book_image_1)
                .error(R.mipmap.default_book_image_1)
                .into(viewHolder.ivImage);

    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public void setItems(List<BookItemToShow> items) {
        this.books = items;
        notifyDataSetChanged();
    }

    public List<BookItemToShow> getItems() {
        return books;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivImage)
        ImageView ivImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
