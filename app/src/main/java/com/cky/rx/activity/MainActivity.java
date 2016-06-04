package com.cky.rx.activity;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cky.rx.R;
import com.cky.rx.activity.base.BaseActivity;
import com.cky.rx.fragment.impl.BookAndroidFragment;
import com.cky.rx.fragment.impl.BookJavaFragment;
import com.cky.rx.fragment.impl.BookJavaScriptFragment;
import com.cky.rx.fragment.impl.BookMYSQLFragment;
import com.cky.rx.fragment.impl.BookPHPFragment;
import com.cky.rx.fragment.impl.BookPythonFragment;
import com.cky.rx.receiver.DownloadCompleteReceiver;
import com.cky.rx.receiver.NetworkChangeReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(android.R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.toolBar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private RelativeLayout navHeaderBackground;
    private long exitTime;

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    private DownloadCompleteReceiver downloadCompleteReceiver = new DownloadCompleteReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navHeaderBackground = (RelativeLayout) headerView.findViewById(R.id.nav_header_background);
        navHeaderBackground.setBackground(ContextCompat.getDrawable(this, R.mipmap.nav_header_bg));

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                       return new BookAndroidFragment();
                    case 1:
                        return new BookPythonFragment();
                    case 2:
                        return new BookJavaFragment();
                    case 3:
                        return new BookMYSQLFragment();
                    case 4:
                        return new BookJavaScriptFragment();
                    case 5:
                        return new BookPHPFragment();
                    default:
                        return new BookAndroidFragment();
                }
            }

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_Android);
                    case 1:
                        return getString(R.string.title_Python);
                    case 2:
                        return getString(R.string.title_Java);
                    case 3:
                        return getString(R.string.title_Mysql);
                    case 4:
                        return getString(R.string.title_JavaScript);
                    case 5:
                        return getString(R.string.title_PHP);
                    default:
                        return getString(R.string.title_Android);
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        navigationView.setNavigationItemSelectedListener(this);

        registNetworkChangeReceiver();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistNetworkChangeReceiver();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.nav_set:
                break;
            case R.id.nav_about:
                break;
        }
        */

        int id = item.getItemId();
        if (id == R.id.nav_set) {
            Toast.makeText(MainActivity.this, getString(R.string.under_developing), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            AboutActivity.start(MainActivity.this);
        } else if (id == R.id.nav_manage) {
            Toast.makeText(MainActivity.this, getString(R.string.under_developing), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_search) {
            SearchActivity.start(MainActivity.this);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //Toast.makeText(MainActivity.this, R.string.press_back_again, Toast.LENGTH_SHORT).show();
                Snackbar.make(drawerLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
                finish();
            }
        }
    }


    private void registNetworkChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void unRegistNetworkChangeReceiver() {
        this.unregisterReceiver(networkChangeReceiver);
    }


}

