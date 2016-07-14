package com.software.eric.coolweather.mvc.choosearea;

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
import com.software.eric.coolweather.constants.ExtraConstant;
import com.software.eric.coolweather.di.DaggerChooseAreaComponent;
import com.software.eric.coolweather.entity.Address;
import com.software.eric.coolweather.mvc.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mzz on 2015/10/31.
 */
public class ChooseAreaActivity extends Activity implements ChooseAreaContract.IChooseAreaView {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.list_view)
    ListView listView;

    private ArrayAdapter<Address> adapter;
    private List<Address> dataList = new ArrayList<>();
    private Address selectedAddress;
    private int currentLevel;
    @Inject
    ChooseAreaContract.IChooseAreaPresenter mChooseAreaPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerChooseAreaComponent.builder().build().inject(this);
        mChooseAreaPresenter.acceptView(this);
        boolean isFromWeatherActivity = getIntent().getBooleanExtra(ExtraConstant.FROM_WEATHER_ACTIVITY, false);
        mChooseAreaPresenter.checkIfGoToWeatherActivity(isFromWeatherActivity);
    }

    @Override
    public void initView() {
        // TODO: 2016/6/21 ToolBar supported
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChooseAreaPresenter.onListItemClicked(dataList.get(position));
            }
        });
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
    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void goToWeatherActivity() {
        WeatherActivity.actionStart(this, true);
        finish();
    }

    public static void actionStart(Context context, boolean isFromWeatherActivity) {
        Intent i = new Intent(context, ChooseAreaActivity.class);
        i.putExtra(ExtraConstant.FROM_WEATHER_ACTIVITY, isFromWeatherActivity);
        context.startActivity(i);
    }
}
