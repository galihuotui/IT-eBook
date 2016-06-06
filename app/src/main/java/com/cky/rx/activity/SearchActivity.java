package com.cky.rx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    FloatingSearchView mSearchView;
    @Bind(R.id.search_results_list)
    RecyclerView rvSearchResult;

    private SearchResultListAdapter mSearchResultListAdapter = new SearchResultListAdapter(this);

    Observer<List<BookItemToShow>> observer = new Observer<List<BookItemToShow>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(SearchActivity.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<BookItemToShow> bookItemToShows) {
            Toast.makeText(SearchActivity.this, String.valueOf(bookItemToShows.size()), Toast.LENGTH_SHORT).show();
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
        mSearchView = (FloatingSearchView) findViewById(R.id.searchView);
        setupSearchView();
        //setSearchView();
        rvSearchResult.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
        rvSearchResult.setAdapter(mSearchResultListAdapter);

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private void setupSearchView() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Toast.makeText(SearchActivity.this, currentQuery, Toast.LENGTH_SHORT).show();
                search(currentQuery);
            }
        });

    }
/*
    protected void setSearchView() {

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setTextSize(16);
            mSearchView.setHint("Search");
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Set permission on Android 6+ !");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                    search(query);
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

                }
            });

        }

    }*/
}
