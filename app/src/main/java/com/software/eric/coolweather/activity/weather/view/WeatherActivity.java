package com.software.eric.coolweather.activity.weather.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.SettingsActivity;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.weather.presenter.IWeatherPresenter;
import com.software.eric.coolweather.activity.weather.presenter.WeatherPresenterImpl;
import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity
        implements IWeatherView, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WeatherActivity";

    @Bind(R.id.weather_info_layout)
    LinearLayout weatherInfoLayout;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.publish_text)
    TextView publishTimeText;
    @Bind(R.id.weather_desp)
    TextView weatherDespText;
    @Bind(R.id.temp1)
    TextView temp1Text;
    @Bind(R.id.temp2)
    TextView temp2Text;
    @Bind(R.id.current_temp)
    TextView currentTemp;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layout_weather_content)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.layout_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.layout_app_bar)
    AppBarLayout appBarLayout;
    @Bind(R.id.weather_chart)
    WeatherChartView weatherChartView;

    private MBroadcastReceiver mBroadcastReceiver;

    private LocalBroadcastManager localBroadcastManager;

    private IWeatherPresenter mWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeatherPresenter = new WeatherPresenterImpl(this);

        if (!mWeatherPresenter.checkCountySelected()) {
            //if not select county
            goChooseArea();
            return;
        }

        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setBackgroundImg();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d(TAG, "Refresh");
                mWeatherPresenter.queryWeather(true);
            }
        });
        boolean isFromChooseArea = getIntent().getBooleanExtra("isFromChooseArea", false);
        mWeatherPresenter.queryWeather(isFromChooseArea);
        mWeatherPresenter.setAutoUpdateService();

        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("City Not Supported");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.chooseArea) {
            goChooseArea();
        } else if (id == R.id.settings) {
            Intent i = new Intent(WeatherActivity.this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showWeather(final WeatherInfoBean weatherInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                collapsingToolbarLayout.setTitle(weatherInfo.getBasic().getCity());
                String publishTime = weatherInfo.getBasic().getUpdate().getLoc() + " " + getResources().getString(R.string.publish);
                publishTimeText.setText(publishTime.substring(publishTime.length() - 8));
                weatherDespText.setText(weatherInfo.getNow().getCond().getTxt());
                temp1Text.setText(weatherInfo.getDaily_forecast()[0].getTmp().getMin() + "");
                temp2Text.setText(weatherInfo.getDaily_forecast()[0].getTmp().getMax() + "");
                currentTemp.setText(weatherInfo.getNow().getTmp() + getString(R.string.degree));
                weatherChartView.setData(weatherInfo);
                weatherInfoLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publishTimeText.setText("同步失败");
            }
        });
    }

    @Override
    public void showSyncing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publishTimeText.setText("同步中……");
                weatherInfoLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void goChooseArea() {
        ChooseAreaActivity.actionStart(this, true);
        finish();
    }

    @Override
    public void setRefreshing(final boolean isRefreshing) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }


    private void setBackgroundImg() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg, options);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //对图片进行模糊处理。同时减少内存占用。
        options.inSampleSize = Utility.calculateInSampleSize(options, dm.widthPixels / 500, dm.heightPixels / 500);
        options.inJustDecodeBounds = false;
        LogUtil.d(TAG, "inSampleSize " + options.inSampleSize);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg, options);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        coordinatorLayout.setBackground(drawable);
    }

    public static void actionStart(Context context, boolean isFromChooseArea) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra("isFromChooseArea", isFromChooseArea);
        context.startActivity(intent);
    }

    private class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("BroadCast", "MBroadcastReceiver");
            Toast.makeText(WeatherActivity.this, "Sorry, City Not Supported", Toast.LENGTH_SHORT).show();
        }
    }
}
