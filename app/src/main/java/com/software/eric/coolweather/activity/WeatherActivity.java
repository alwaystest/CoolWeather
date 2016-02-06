package com.software.eric.coolweather.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.model.Key;
import com.software.eric.coolweather.service.UpdateWeatherInfoService;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

        String countyName = getIntent().getStringExtra("county_name");
        if (!TextUtils.isEmpty(countyName)) {
            publishTimeText.setText("同步中……");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherInfo(countyName);
        } else {
            showWeather();
        }
        //set alarm , update per 24H if not set
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int autoUpdateTime = prefs.getInt("auto_update_time",24);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        long nowtime = System.currentTimeMillis();
        long time = autoUpdateTime * 60 * 60 * 1000 + nowtime;
        Intent i = new Intent(this, UpdateWeatherInfoService.class);
        //TODO: change 4th param
        PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC, time, pi);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButton.callOnClick();
                Snackbar.make(view, "Synchronizing……", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        localBroadcastManager.unregisterReceiver(mBroadcastReceiver);
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


    private void queryWeatherByCode(String countyCode) {
        String address = "http://api.heweather.com/x3/weather?cityid=" +
                countyCode +
                "&key=" +
                Key.KEY;
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String cityName) {
        String address = null;
        try {
            address = "http://api.heweather.com/x3/weather?city=" +
                    URLEncoder.encode(cityName, "UTF-8") +
                    "&key=" +
                    Key.KEY;
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("queryWeatherInfo",e.toString());
        }
        queryFromServer(address, "countyName");
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        String publishTime = prefs.getString("publish_time", "") + getResources().getString(R.string.publish);
        publishTimeText.setText(publishTime);
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    private void queryFromServer(final String address, final String type) {
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    if ("countyName".equals(type) || "countyCode".equals(type)) {
                        Utility.handleWeatherResponse(WeatherActivity.this, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    LogUtil.e("onError", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            publishTimeText.setText("同步失败");
                        }
                    });
                }
            });
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
                publishTimeText.setText("同步中……");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherByCode(weatherCode);
                }
                break;
        }
    }

    private class MBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("BroadCast","MBroadcastReceiver");
            Toast.makeText(WeatherActivity.this, "Sorry, City Not Supported", Toast.LENGTH_SHORT).show();
        }
    }
}
