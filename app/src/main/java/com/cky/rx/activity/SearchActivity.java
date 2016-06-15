package com.cky.rx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.cky.rx.R;
import com.cky.rx.activity.base.BaseActivity;
import com.cky.rx.adapter.SearchResultListAdapter;
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

public class SearchActivity extends BaseActivity {

//    @Bind(R.id.searchView)
//    SearchView mSearchView;
    private static final String TAG = SearchActivity.class.getSimpleName();
    @Bind(R.id.searchView)
    FloatingSearchView mSearchView;
    @Bind(R.id.search_results_list)
    RecyclerView rvSearchResult;
    @Bind(R.id.tv_load_error)
    TextView tvLoadError;
    @Bind(R.id.tv_load_empty)
    TextView tvLoadEmpty;

    private SearchResultListAdapter mSearchResultListAdapter = new SearchResultListAdapter(this);

    Observer<List<BookItemToShow>> observer = new Observer<List<BookItemToShow>>() {
        @Override
        public void onCompleted() {
            if (mSearchResultListAdapter.getItems() == null || mSearchResultListAdapter.getItems().size() == 0) {
                tvLoadError.setVisibility(View.GONE);
                tvLoadEmpty.setVisibility(View.VISIBLE);
            } else {
                tvLoadEmpty.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            //Toast.makeText(SearchActivity.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            tvLoadError.setVisibility(View.VISIBLE);
            tvLoadEmpty.setVisibility(View.GONE);
        }

        @Override
        public void onNext(List<BookItemToShow> bookItemToShows) {
            //Toast.makeText(SearchActivity.this, String.valueOf(bookItemToShows.size()), Toast.LENGTH_SHORT).show();
            mSearchResultListAdapter.setItems(bookItemToShows);
        }
    };

    private void search(String query) {

        subscription =  Network.getIteBooksApi()
                .getBooksByCategory(query)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

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
        rvSearchResult.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        rvSearchResult.setAdapter(mSearchResultListAdapter);
        mSearchResultListAdapter.setOnItemClickListener(new SearchResultListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                final int position = rvSearchResult.getChildAdapterPosition(view);
                if (RecyclerView.NO_POSITION != position && mSearchResultListAdapter.getItems().size() != position) {
                    BookDetailActivity2.start(SearchActivity.this, mSearchResultListAdapter.getItemData(position).id);
                } else if (RecyclerView.NO_POSITION != position && mSearchResultListAdapter.getItems().size() == position) {

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
                search(currentQuery);
                //throw new RuntimeException("test crash handler");
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
