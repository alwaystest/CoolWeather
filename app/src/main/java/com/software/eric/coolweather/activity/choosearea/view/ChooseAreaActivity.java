package com.software.eric.coolweather.activity.choosearea.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.choosearea.presenter.ChooseAreaPresenterImpl;
import com.software.eric.coolweather.activity.choosearea.presenter.IChooseAreaPresenter;
import com.software.eric.coolweather.activity.weather.view.WeatherActivity;
import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mzz on 2015/10/31.
 */
public class ChooseAreaActivity extends Activity implements IChooseAreaView {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<Address> adapter;
    private List<Address> dataList = new ArrayList<>();
    private Address selectedAddress;
    private int currentLevel;
    private boolean isFromWeatherActivity;
    private IChooseAreaPresenter mChooseAreaPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChooseAreaPresenter = new ChooseAreaPresenterImpl(this);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        if (mChooseAreaPresenter.checkCountySelected() && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedAddress = dataList.get(position);
                    mChooseAreaPresenter.queryCities(selectedAddress);
                } else if (currentLevel == LEVEL_CITY) {
                    selectedAddress = dataList.get(position);
                    mChooseAreaPresenter.queryCounties(selectedAddress);
                } else if (currentLevel == LEVEL_COUNTY) {
                    String countyName = dataList.get(position).getName();
                    String countyCode = dataList.get(position).getCode();
                    County county = new County();
                    county.setCode(countyCode);
                    county.setName(countyName);
                    mChooseAreaPresenter.saveSelectedCounty(county);
                    WeatherActivity.actionStart(ChooseAreaActivity.this, true);
                    finish();
                }
            }
        });

        mChooseAreaPresenter.queryProvinces();
    }

    @Override
    public void showProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ChooseAreaActivity.this);
                    progressDialog.setMessage("正在加载");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
                progressDialog.show();
            }
        });
    }

    @Override
    public void closeProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void setList(final List<? extends Address> addressList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (addressList.size() > 0) {
                    dataList.clear();
                    dataList.addAll(addressList);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            mChooseAreaPresenter.queryCities(selectedAddress);
        } else if (currentLevel == LEVEL_CITY) {
            mChooseAreaPresenter.queryProvinces();
        } else if (currentLevel == LEVEL_PROVINCE) {
            finish();
        }
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setCurrentLevel(int level) {
        currentLevel = level;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

    public static void actionStart(Context context, boolean isFromWeatherActivity) {
        Intent i = new Intent(context, ChooseAreaActivity.class);
        i.putExtra("from_weather_activity", isFromWeatherActivity);
        context.startActivity(i);
    }
}
