package com.cky.rx.fragment.impl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cky.rx.R;
import com.cky.rx.activity.BookDetailActivity2;
import com.cky.rx.adapter.BookListAdapter;
import com.cky.rx.fragment.base.BaseFragment;
import com.cky.rx.model.BookItemToShow;
import com.cky.rx.network.Network;
import com.cky.rx.util.SearchBookResultToItemsMapper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by cuikangyuan on 16/5/12.
 */
public class BookMYSQLFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private static final String TAG = "BookMysqlFragment";
    private static final String QUERY_KEY = "Mysql";

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    /*
        @Bind(R.id.springView)
        SpringView springView;
    */
    @Bind(R.id.rvGrid)
    RecyclerView recyclerView;
    @Bind(R.id.tv_load_error)
    TextView tvLoadError;
    @Bind(R.id.tv_load_empty)
    TextView tvLoadEmpty;

    BookListAdapter adapter = new BookListAdapter();

    private int page_index = 1;

    Observer<List<BookItemToShow>> observer = new Observer<List<BookItemToShow>>() {
        @Override
        public void onCompleted() {
            if (adapter.getItems() == null || adapter.getItems().size() == 0) {
                tvLoadError.setVisibility(View.GONE);
                tvLoadEmpty.setVisibility(View.VISIBLE);
            } else {
                //Snackbar.make(swipeRefreshLayout, R.string.loading_finished, Snackbar.LENGTH_SHORT).show();
                tvLoadEmpty.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            hideProgress();
            tvLoadError.setVisibility(View.VISIBLE);
            tvLoadEmpty.setVisibility(View.GONE);
            //Snackbar.make(swipeRefreshLayout, R.string.loading_failed, Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), getString(R.string.loading_failed), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<BookItemToShow> bookItemToShows) {
            hideProgress();

            if (adapter.getItems() == null) {
                String strSuccess = getResources().getString(R.string.loading_success);
                //Toast.makeText(getActivity(), String.format(strSuccess, bookItemToShows.size()), Toast.LENGTH_SHORT).show();
                adapter.setItems(bookItemToShows);
            } else {
                adapter.getItems().addAll(bookItemToShows);
                adapter.notifyDataSetChanged();
            }


        }
    };

    private void search(String query) {

       subscription =  Network.getIteBooksApi()
               .getBooksByCategory(query)
               .doOnSubscribe(new Action0() {
                   @Override
                   public void call() {
                       showProgress();
                       //adapter.setItems(null);
                   }
               })
               .map(SearchBookResultToItemsMapper.getInstance())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(observer);

    }

    private void getNextPage() {

//        String cueeentIndex = adapter.getItems().get(adapter.getItemCount() - 1).index;
//
//        String nextIndex = String.valueOf(Integer.valueOf(cueeentIndex) + 1);
//        Log.d(TAG, "next index --->" + nextIndex);
        Log.d(TAG, "next index --->" + String.valueOf(++page_index));
        subscription =  Network.getIteBooksApi()
                .getBooksByPage(QUERY_KEY, String.valueOf(page_index))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .map(SearchBookResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        //recyclerView.addOnScrollListener(new MyLoadMoreListener(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlideToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiableItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalCount = layoutManager.getItemCount();

                    if(lastVisiableItem == (totalCount - 1) && isSlideToLast) {
                        //Toast.makeText(getActivity(), getString(R.string.load_more), Toast.LENGTH_SHORT).show();
                        getNextPage();
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
        });

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(this);
        /*
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setHeader(new DefaultHeader(getActivity()));

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                search(QUERY_KEY);
            }

            @Override
            public void onLoadmore() {
                getNextPage();
            }
        });
        */


        search(QUERY_KEY);

        return view;
    }

    @Override
    public void onRefresh() {
        if (adapter.getItems() != null) {
            adapter.setItems(null);
            page_index = 1;
        }
        search(QUERY_KEY);
    }

    private void showProgress() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void hideProgress() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int position = recyclerView.getChildAdapterPosition(v);
        if (RecyclerView.NO_POSITION != position) {
            //Toast.makeText(getActivity(), adapter.getItemData(position).title, Toast.LENGTH_SHORT).show();
            BookDetailActivity2.start(getActivity(), adapter.getItemData(position).id);
        }
    }
}
