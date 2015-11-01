package com.software.eric.coolweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.service.UpdateWeatherInfoService;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    public Button testInternet;
    public Button chooseAreaActivity;
    public Button testInstanceService;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        testInternet = (Button) findViewById(R.id.testInternet);
        chooseAreaActivity = (Button) findViewById(R.id.chooseArea);
        testInstanceService = (Button) findViewById(R.id.testIntentService);

        testInstanceService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("CoolWeather","TestService");
                Intent i = new Intent(MainActivity.this, UpdateWeatherInfoService.class);
                startService(i);
                Toast.makeText(MainActivity.this,"Service Started",Toast.LENGTH_SHORT).show();
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
                        LogUtil.i("CoolWeather","Load Finish");
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
