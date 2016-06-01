package com.cky.rx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cky.rx.R;
import com.cky.rx.model.BookDetailResult;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cuikangyuan on 16/5/18.
 */
public class BookDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = BookDetailAdapter.class.getSimpleName();

    private Context mContext;

    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;

    private BookDetailResult bookDetailResult;

    public BookDetailAdapter(Context context, BookDetailResult bookDetailResult) {
        mContext = context;
        this.bookDetailResult = bookDetailResult;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_ONE) {
            return TYPE_ONE;
        } else if (position == TYPE_TWO) {
            return TYPE_TWO;
        } else if (position == TYPE_THREE) {
            return TYPE_THREE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            return new BookDetailImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_book_image, parent, false));
        } else if (viewType == TYPE_TWO) {
            return new BookDetailDescViewHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_book_desc, parent, false));
        } else if (viewType == TYPE_THREE) {
            return new BookDetailInfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_book_info, parent, false));
        }

        return null;
    }

    static class BookDetailDescViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.book_description_value)
        TextView tvBookDesc;

        public BookDetailDescViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class BookDetailImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.book_image)
        ImageView ivBookImage;

        public BookDetailImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class BookDetailInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.book_publisher_value)
        TextView tvBookPublisher;
        @Bind(R.id.book_pages_value)
        TextView tvBookPages;
        @Bind(R.id.book_author_value)
        TextView tvBookAuthor;
        @Bind(R.id.book_year_value)
        TextView tvBookYear;
        @Bind(R.id.book_isbn_value)
        TextView tvBookIsbn;

        public BookDetailInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookDetailImageViewHolder) {
            try {
                Glide.with(mContext)
                        .load(bookDetailResult.Image)
                        .fitCenter()
                        .placeholder(R.mipmap.default_book_image_1)
                        .error(R.mipmap.default_book_image_1)
                        .into(((BookDetailImageViewHolder)holder).ivBookImage);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        if (holder instanceof BookDetailInfoViewHolder) {
            try {
                ((BookDetailInfoViewHolder)holder).tvBookPublisher.setText(bookDetailResult.Publisher);
                ((BookDetailInfoViewHolder)holder).tvBookAuthor.setText(bookDetailResult.Author);
                ((BookDetailInfoViewHolder)holder).tvBookPages.setText(bookDetailResult.Page);
                ((BookDetailInfoViewHolder)holder).tvBookIsbn.setText(bookDetailResult.ISBN);
                ((BookDetailInfoViewHolder)holder).tvBookYear.setText(bookDetailResult.Year);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        }
        if (holder instanceof BookDetailDescViewHolder) {
            try {
                ((BookDetailDescViewHolder)holder).tvBookDesc.setText(bookDetailResult.Description);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
