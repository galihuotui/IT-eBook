package com.cky.rx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.cky.rx.R;
import com.cky.rx.activity.base.BaseActivity;
import com.cky.rx.adapter.SearchResultListAdapter;
import com.cky.rx.model.BookItemToShow;
import com.cky.rx.network.Network;
import com.cky.rx.util.SearchBookResultToItemsMapper;
import com.cky.rx.widget.DividerLine;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {

//    @Bind(R.id.searchView)
//    SearchView mSearchView;
    private static final String TAG = SearchActivity.class.getSimpleName();
    @Bind(R.id.searchView)
    FloatingSearchView mSearchView;
    @Bind(R.id.search_results_list)
    RecyclerView rvSearchResult;
    @Bind(R.id.tv_load_empty)
    TextView tvLoadEmpty;
    @Bind(R.id.progressBar)
    ProgressBar pbLoading;

    private int page_index = 1;
    private int total_page = -1;
    private String query_key = "";

    private SearchResultListAdapter mSearchResultListAdapter = new SearchResultListAdapter(this);

    Observer<List<BookItemToShow>> observer = new Observer<List<BookItemToShow>>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted");
            if (mSearchResultListAdapter.getItems() == null || mSearchResultListAdapter.getItems().size() == 0) {
                tvLoadEmpty.setVisibility(View.VISIBLE);
            } else {
                tvLoadEmpty.setVisibility(View.GONE);
                if (total_page == -1) {
                    total_page = Integer.valueOf(mSearchResultListAdapter.getItems().get(0).totalPage);
                    Log.d(TAG, "总页数 -> " + total_page);
                }

            }
        }

        @Override
        public void onError(Throwable e) {
            //Toast.makeText(SearchActivity.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onError");
            hideProgressBar();
            tvLoadEmpty.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(List<BookItemToShow> bookItemToShows) {
            Log.d(TAG, "onNext -> " + bookItemToShows.size());
            hideProgressBar();
            if (mSearchResultListAdapter.getItems() == null) {
                mSearchResultListAdapter.setItems(bookItemToShows);

            } else {
                mSearchResultListAdapter.getItems().addAll(bookItemToShows);
                mSearchResultListAdapter.notifyDataSetChanged();
            }

        }
    };

    private void search(String query) {

        subscription =  Network.getIteBooksApi()
                .getBooksByCategory(query)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                       //tvLoadEmpty.setVisibility(View.GONE);
                       //tvLoadError.setVisibility(View.GONE);
                       showProgressBar();
                    }
                })
                .map(SearchBookResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private void getNextPage() {

        //String cueeentIndex = adapter.getItems().get(adapter.getItemCount() - 1).index;

        //String nextIndex = String.valueOf(Integer.valueOf(cueeentIndex) + 1);
        //Log.d(TAG, "next index --->" + nextIndex);
        //Log.d(TAG, "next index --->" + String.valueOf(++page_index));
        subscription =  Network.getIteBooksApi()
                .getBooksByPage(query_key, String.valueOf(page_index))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //showProgressBar();
                    }
                })
                .map(SearchBookResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupSearchView();

        mSearchView.setSearchFocused(true);
        //mSearchView.open(true);

        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(2);
        dividerLine.setColor(0xFFDDDDDD);
        rvSearchResult.addItemDecoration(dividerLine);
        rvSearchResult.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        rvSearchResult.setAdapter(mSearchResultListAdapter);
        rvSearchResult.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mSearchResultListAdapter.setOnItemClickListener(new SearchResultListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                final int position = rvSearchResult.getChildAdapterPosition(view);
                if (RecyclerView.NO_POSITION != position && mSearchResultListAdapter.getItems().size() != position) {
                    BookDetailActivity2.start(SearchActivity.this, mSearchResultListAdapter.getItemData(position).id);
                } else if (RecyclerView.NO_POSITION != position && mSearchResultListAdapter.getItems().size() == position) {
                    if (++page_index <= total_page) {
                        getNextPage();
                    }

                }


            }
        });

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
    private void setupSearchView() {

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {

            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                //Toast.makeText(SearchActivity.this, currentQuery, Toast.LENGTH_SHORT).show();
                if (!currentQuery.trim().equals("")) {
                    if (mSearchResultListAdapter.getItems()!= null) {
                        mSearchResultListAdapter.setItems(null);
                        mSearchResultListAdapter.notifyDataSetChanged();
                        page_index = 1;
                        total_page = -1;
                    }
                    query_key = currentQuery;
                    Log.d(TAG, "当前搜索 -> " + currentQuery);
                    tvLoadEmpty.setVisibility(View.GONE);
                    search(currentQuery);
                }

                //throw new RuntimeException("test crash handler");
            }
        });

    }

    private void showProgressBar() {
        SearchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pbLoading.getVisibility() == View.GONE) {
                    pbLoading.setVisibility(View.VISIBLE);
                }
                if (rvSearchResult.getVisibility() == View.VISIBLE) {
                    rvSearchResult.setVisibility(View.GONE);
                }
            }
        });
    }

    private void hideProgressBar() {
        SearchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pbLoading.getVisibility() == View.VISIBLE) {
                    pbLoading.setVisibility(View.GONE);
                }
                if (rvSearchResult.getVisibility() == View.GONE) {
                    rvSearchResult.setVisibility(View.VISIBLE);
                }
            }
        });

    }
/*
    private void setupSearchView() {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setTextSize(16);
            mSearchView.setHint(getString(R.string.input_key_word));
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Set permission on Android 6+ !");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                    //search(query);
                    //mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {

                }

                @Override
                public void onClose() {
                    SearchActivity.this.finish();
                }
            });


    }
    */
}
