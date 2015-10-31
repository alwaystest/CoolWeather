package com.software.eric.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.db.CoolWeatherDB;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mzz on 2015/10/31.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> datalist = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datalist);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    LogUtil.i("CoolWeather",selectedProvince.toString());
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    LogUtil.i("CoolWeather",selectedCity.toString());
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            datalist.clear();
            for (Province p : provinceList) {
                datalist.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            datalist.clear();
            for (City c : cityList) {
                datalist.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCounties() {
        countyList = coolWeatherDB.loadCounty(selectedCity.getId());
        if (countyList.size() > 0) {
            datalist.clear();
            for (County c : countyList) {
                datalist.add(c.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" +
                    code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                LogUtil.d("CoolWeather_sendHttpRequest",response);
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(coolWeatherDB, response);
                }else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(coolWeatherDB, response,selectedProvince.getId());
                }else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(coolWeatherDB, response,selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if ("city".equals(type)) {
                                queryCities();
                            }else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e("CoolWeather_sendHttpRequest",e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
//            TODO: is there any difference?
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if(progressDialog !=  null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        }else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }else if (currentLevel == LEVEL_PROVINCE) {
            finish();
        }
    }

    public static void actionStart(Context context){
        Intent i = new Intent(context,ChooseAreaActivity.class);
        context.startActivity(i);
    }
}
