package com.cky.rx.fragment.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.cky.rx.R;
import com.cky.rx.util.Util;

/**
 * Created by cuikangyuan on 16/5/22.
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    private final String INTRODUCTION = "introduction";
    private final String AUTHOR = "author";
    private final String CURRENT_VERSION = "current_version";
    private final String GITHUB = "github";
    private final String SINA_WEIBO = "sina_weibo";
    private final String EMAIL = "email";
    private final String SHARE = "share";

    private Preference mIntroduction;
    private Preference mVersion;
    private Preference mAuthor;
    private Preference mGithub;
    private Preference mSinaWeibo;
    private Preference mEmail;
    private Preference mShare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.about);

        mVersion = findPreference(CURRENT_VERSION);
        mGithub = findPreference(GITHUB);
        mSinaWeibo = findPreference(SINA_WEIBO);
        mEmail = findPreference(EMAIL);
        mShare = findPreference(SHARE);

        mGithub.setOnPreferenceClickListener(this);
        mEmail.setOnPreferenceClickListener(this);
        mSinaWeibo.setOnPreferenceClickListener(this);
        mShare.setOnPreferenceClickListener(this);

        mVersion.setSummary(Util.getVerion(getActivity()));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference == mGithub) {
            copyToClipboard(getView(), mGithub.getSummary().toString());
        } else if (preference == mEmail) {
            copyToClipboard(getView(), mEmail.getSummary().toString());
        } else if (preference == mSinaWeibo) {
            copyToClipboard(getView(), mSinaWeibo.getSummary().toString());
        } else if (preference == mShare) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)));
        }

        return false;
    }

    //复制黏贴板
    private void copyToClipboard(View view, String info) {
        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        Snackbar.make(view, getString(R.string.copy_finish), Snackbar.LENGTH_SHORT).show();
    }
}
