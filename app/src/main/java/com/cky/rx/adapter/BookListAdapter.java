package com.cky.rx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class BookListAdapter extends RecyclerView.Adapter {

    List<BookItemToShow> books;
    private View.OnClickListener listener;



    public  BookItemToShow getItemData(int position) {
        return books.get(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder)holder;
        viewHolder.itemView.setOnClickListener(listener);
        BookItemToShow item = books.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .fitCenter()
                .placeholder(R.mipmap.default_book_image_1)
                .error(R.mipmap.default_book_image_1)
                .into(viewHolder.ivImage);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDesc.setText(item.desc);

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

    public void setOnClickListener(View.OnClickListener listener) {
       this.listener = listener;
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivImage)
        ImageView ivImage;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDesc)
        TextView tvDesc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
