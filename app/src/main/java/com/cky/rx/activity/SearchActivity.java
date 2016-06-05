package com.cky.rx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.cky.rx.R;
import com.cky.rx.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @Bind(R.id.searchView)
    FloatingSearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSearchView();

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private void setSearchView() {

        mSearchView.setSearchFocused(true);

        if (mSearchView != null) {
            mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
                @Override
                public void onHomeClicked() {
                    finish();
                }
            });

            mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                }

                @Override
                public void onSearchAction(String currentQuery) {

                }
            });
        }


        /*if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setHint(R.string.input_key_word);
            mSearchView.setTextSize(16);
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText(getString(R.string.speak_key_word));
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.open(true);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    //mSearchView.close(false);
                    mSearchView.setQuery(query);
                    return true;
                }
            });
            mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
                @Override
                public void onMenuClick() {
                    finish();
                }
            });
        }
        */
    }
}
