package com.software.eric.coolweather.activity.weather.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.SettingsActivity;
import com.software.eric.coolweather.activity.weather.presenter.IWeatherPresenter;
import com.software.eric.coolweather.activity.weather.presenter.WeatherPresenterImpl;
import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity
        implements IWeatherView, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishTimeText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCityButton;
    private Button refreshButton;
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

        setContentView(R.layout.weather_activity);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishTimeText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        switchCityButton = (Button) findViewById(R.id.switch_city);
        refreshButton = (Button) findViewById(R.id.refresh_weather);

        switchCityButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButton.callOnClick();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mWeatherPresenter.queryWeather(false);
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
        getMenuInflater().inflate(R.menu.main2, menu);
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
            Toast.makeText(this, "ChooseArea", Toast.LENGTH_SHORT).show();
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
                cityNameText.setText(weatherInfo.getBasic().getCity());
                String publishTime = weatherInfo.getBasic().getUpdate().getLoc() + getResources().getString(R.string.publish);
                publishTimeText.setText(publishTime);
                weatherDespText.setText(weatherInfo.getNow().getCond().getTxt());
                temp1Text.setText(weatherInfo.getDaily_forecast()[0].getTmp().getMin());
                temp2Text.setText(weatherInfo.getDaily_forecast()[0].getTmp().getMax());
                currentDateText.setText(new SimpleDateFormat("yyyy年M月d日HH:mm", Locale.CHINA).format(new Date()));
                weatherInfoLayout.setVisibility(View.VISIBLE);
                cityNameText.setVisibility(View.VISIBLE);
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
                Snackbar.make(weatherInfoLayout, "Synchronizing……", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                weatherInfoLayout.setVisibility(View.INVISIBLE);
                cityNameText.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void goChooseArea() {
        ChooseAreaActivity.actionStart(this);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                showSyncing();
                mWeatherPresenter.queryWeather(true);
                break;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
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
