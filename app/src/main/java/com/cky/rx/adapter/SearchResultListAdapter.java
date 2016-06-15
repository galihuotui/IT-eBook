package com.cky.rx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public SearchResultListAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == books.size()) {
            return TYPE_FOOTER;
        } else  {
            return TYPE_NORMAL;
        }
    }

    public  BookItemToShow getItemData(int position) {
        return books.get(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View view;

        if (viewType == TYPE_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_hor, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v);
                    }

                }
            });
            holder = new ItemViewHolder(view, TYPE_NORMAL);
            return holder;
        } else if (viewType == TYPE_FOOTER) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v);
                    }

                }
            });
            holder = new ItemViewHolder(view, TYPE_FOOTER);
            return holder;

        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position != books.size() && holder.getItemViewType() == TYPE_NORMAL) {
            ItemViewHolder viewHolder = (ItemViewHolder)holder;
            BookItemToShow item = books.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(item.imageUrl)
                    .fitCenter()
                    .placeholder(R.mipmap.default_book_image_1)
                    .error(R.mipmap.default_book_image_1)
                    .into(viewHolder.ivImage);
            viewHolder.tvTitle.setText(item.title);
        }
        if (position == books.size() && holder.getItemViewType() == TYPE_FOOTER) {

        }


    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size() + 1;
    }

    public void setItems(List<BookItemToShow> items) {
        this.books = items;
        notifyDataSetChanged();
    }

    public List<BookItemToShow> getItems() {
        return books;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTitle;
        Button btnLoadMore;
        public ItemViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_NORMAL) {
                ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            } else if (viewType == TYPE_FOOTER) {
                btnLoadMore = (Button) itemView.findViewById(R.id.btn_load_more);
            }

        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.btn_load_more)
        Button btnLoadMore;

        public FooterViewHolder(View itemView) {
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
