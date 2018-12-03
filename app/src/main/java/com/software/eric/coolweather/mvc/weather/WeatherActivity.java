package com.software.eric.coolweather.mvc.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.software.eric.coolweather.R;
import com.software.eric.coolweather.constants.ExtraConstant;
import com.software.eric.coolweather.di.DaggerSingletonComponent;
import com.software.eric.coolweather.di.DaggerWeatherModelComponent;
import com.software.eric.coolweather.di.WeatherInfoModule;
import com.software.eric.coolweather.entity.HeWeather;
import com.software.eric.coolweather.mvc.CanaryActivity;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaActivity;
import com.software.eric.coolweather.mvc.setting.SettingsActivity;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;
import com.software.eric.coolweather.widget.WeatherChartView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity
        implements WeatherContract.IWeatherView, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WeatherActivity";

    @BindView(R.id.weather_info_layout)
    LinearLayout weatherInfoLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.publish_text)
    TextView publishTimeText;
    @BindView(R.id.weather_desp)
    TextView weatherDespText;
    @BindView(R.id.temp1)
    TextView temp1Text;
    @BindView(R.id.temp2)
    TextView temp2Text;
    @BindView(R.id.current_temp)
    TextView currentTemp;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_weather_content)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layout_app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.weather_chart)
    WeatherChartView weatherChartView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private MBroadcastReceiver mBroadcastReceiver;

    private LocalBroadcastManager localBroadcastManager;

    @Inject
    WeatherPresenterImpl mWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWeatherModelComponent
                .builder()
                .weatherInfoModule(new WeatherInfoModule(this))
                .singletonComponent(DaggerSingletonComponent.create())
                .build()
                .inject(this);
        mWeatherPresenter.ifGoChooseArea();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        StatusBarUtil.setTranslucentForDrawerLayout(this, drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
        boolean isFromChooseArea = getIntent().getBooleanExtra(ExtraConstant.IS_FROM_CHOOSE_AREA, false);
        mWeatherPresenter.queryWeather(isFromChooseArea);
        mWeatherPresenter.setAutoUpdateService();

        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ExtraConstant.CITY_NOT_SUPPORTED);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            SettingsActivity.actionStart(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.chooseArea:
                goChooseArea();
                break;
            case R.id.settings:
                SettingsActivity.actionStart(this);
                break;
            case R.id.canary_entrance:
                CanaryActivity.startActivity(this);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                    break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showWeather(final HeWeather weatherInfo) {
        if (weatherInfo == null) {
            showFailed();
            return;
        }
        collapsingToolbarLayout.setTitle(weatherInfo.getBasic().getLocation());
        String publishTime = weatherInfo.getUpdate().getLoc() + " " + getResources().getString(R.string.publish);
        publishTimeText.setText(publishTime.substring(publishTime.length() - 8));
        weatherDespText.setText(weatherInfo.getNow().getCondTxt());
        temp1Text.setText(weatherInfo.getDailyForecast().get(0).getTmpMin());
        temp2Text.setText(weatherInfo.getDailyForecast().get(0).getTmpMax());
        currentTemp.setText(weatherInfo.getNow().getTmp() + getString(R.string.degree));
        // FIXME: 2018/11/20 update chart
//        weatherChartView.setData(weatherInfo);
        weatherInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publishTimeText.setText(R.string.syn_failed);
            }
        });
    }

    @Override
    public void showSyncing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publishTimeText.setText(R.string.synchronizing);
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
        swipeRefreshLayout.setBackground(drawable);
    }

    public static void actionStart(Context context, boolean isFromChooseArea) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(ExtraConstant.IS_FROM_CHOOSE_AREA, isFromChooseArea);
        context.startActivity(intent);
    }

    private class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("BroadCast", "MBroadcastReceiver");
            Toast.makeText(WeatherActivity.this, R.string.notification_city_not_support, Toast.LENGTH_SHORT).show();
        }
    }
}
