package com.software.eric.coolweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.weather.view.WeatherActivity;
import com.software.eric.coolweather.model.Key;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public Button testInternet;
    public Button chooseAreaActivity;
    public Button testInstanceService;
    public Button testMain2Activity;
    public Button testJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });

        testInternet = (Button) findViewById(R.id.testInternet);
        chooseAreaActivity = (Button) findViewById(R.id.chooseArea);
        testInstanceService = (Button) findViewById(R.id.testIntentService);
        testMain2Activity = (Button) findViewById(R.id.testMain2Activity);
        testJson = (Button) findViewById(R.id.testJson);

        testJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = null;
                try {
                    address = "http://api.heweather.com/x3/weather?city=" +
                            URLEncoder.encode("大连", "UTF-8") +
                            Key.KEY;
                } catch (UnsupportedEncodingException e) {
                    LogUtil.e("encode", e.toString());
                }
                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Utility.handleWeatherResponse(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        LogUtil.e("CoolWeather", e.getMessage());
                    }
                });
            }
        });

        testMain2Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });

        testInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("CoolWeather", "Clicked");
                String address = "http://www.baidu.com";
                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        LogUtil.i("CoolWeather", "Load Finish");
                    }

                    @Override
                    public void onError(Exception e) {
                        LogUtil.e("CoolWeather", e.getMessage());
                    }
                });
            }
        });

        chooseAreaActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAreaActivity.actionStart(MainActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
